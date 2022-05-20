package run.stitch.blog.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import run.stitch.blog.constant.Role;
import run.stitch.blog.dto.LoginDTO;
import run.stitch.blog.entity.User;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.repository.UserRepository;
import run.stitch.blog.service.UserAuthService;
import run.stitch.blog.util.Captcha;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Integer check(LoginDTO loginDTO) {
        // 检查验证码
        String redisCode = (String) redisTemplate.opsForValue().get(Constants.KAPTCHA_SESSION_KEY);
        if (StringUtils.hasLength(redisCode)) {
            if (redisCode.equalsIgnoreCase(loginDTO.getCode())) {
                Long expire = redisTemplate.boundHashOps(Constants.KAPTCHA_SESSION_KEY).getExpire();
                if (!ObjectUtils.isEmpty(expire) && expire < 0L) {
                    throw new BizException(401, "验证码已过期");
                }
            } else {
                throw new BizException(401, "验证码错误");
            }
        } else {
            throw new BizException(401, "验证码已过期");
        }
        // 检查用户名密码
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getPassword)
                .eq(User::getUsername, loginDTO.getUsername())
                .eq(User::getDeleted, 0);
        User user = userRepository.selectOne(wrapper);
        if (user != null && BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            return user.getId();
        }
        throw new BizException(401, "用户名或密码错误");
    }

    @Override
    public Role getRole(Integer userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getType)
                .eq(User::getId, userId);
        User user = userRepository.selectOne(wrapper);
        return switch (user.getType()) {
            case 0 -> Role.USER;
            case 1 -> Role.ADMIN;
            default -> Role.UNKNOWN;
        };
    }

    @Override
    public BufferedImage createCaptcha() {
        BufferedImage image = new BufferedImage(200, 60, BufferedImage.TYPE_INT_RGB);
        String randomText = Captcha.drawRandomText(image);
        redisTemplate.opsForValue().set(Constants.KAPTCHA_SESSION_KEY, randomText, 60, TimeUnit.SECONDS);
        return image;
    }
}
