package run.stitch.blog.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import run.stitch.blog.constant.Role;
import run.stitch.blog.dto.params.LoginParam;
import run.stitch.blog.dto.params.OauthLoginParam;
import run.stitch.blog.dto.UserDTO;
import run.stitch.blog.entity.User;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.repository.UserRepository;
import run.stitch.blog.service.UserAuthService;
import run.stitch.blog.util.Captcha;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static run.stitch.blog.util.StatusCode.*;

@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.oauth.gitee.client-id}")
    private String clientId;
    @Value("${spring.oauth.gitee.client-secret}")
    private String clientSecret;
    @Value("${spring.oauth.gitee.redirect-uri}")
    private String redirectUrl;

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

    @Override
    public UserDTO login(LoginParam loginParam) {
        User user = this.check(loginParam.getUsername(), loginParam.getPassword(), loginParam.getCode());
        return this.login(user);
    }

    @Override
    public UserDTO giteeLogin(OauthLoginParam oauthLoginParam) {
        String code = oauthLoginParam.getCode();
        Map<String, Object> params = new HashMap<>() {{
            put("code", code);
            put("client_id", clientId);
            put("client_secret", clientSecret);
            put("grant_type", "authorization_code");
            put("redirect_uri", redirectUrl);
        }};
        String giteeToken = HttpUtil.post("https://gitee.com/oauth/token", params);
        Map giteeTokenJson = (Map) JSONUtil.parse(giteeToken);
        String giteeAccessToken = (String) giteeTokenJson.get("access_token");
        if (!ObjectUtils.isEmpty(giteeAccessToken)) {
            String userInfo = HttpUtil.get("https://gitee.com/api/v5/user", new HashMap<>() {{
                put("access_token", giteeAccessToken);
            }});
            Map<String, Object> giteeUserInfo = (Map<String, Object>) JSONUtil.parse(userInfo);
            String username = (String) giteeUserInfo.get("login");
            String avatarUrl = (String) giteeUserInfo.get("avatar_url");
            if (!ObjectUtils.isEmpty(username) && !ObjectUtils.isEmpty(avatarUrl)) {
                User user = User.builder().username(username).avatarUrl(avatarUrl).build();
                if (this.saveOrUpdateUser(user)) {
                    return this.login(user);
                }
            }
        }
        return null;
    }

    public Boolean saveOrUpdateUser(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername());
        User dbUser = userRepository.selectOne(wrapper);
        if (ObjectUtils.isEmpty(dbUser)) {
            user.setType(0);
            return userRepository.insert(user) > 0;
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getUsername, user.getUsername());
        return userRepository.update(user, updateWrapper) >= 0;
    }

    private User check(String username, String password, String code) {
        // 检查验证码
        String redisCode = (String) redisTemplate.opsForValue().get(Constants.KAPTCHA_SESSION_KEY);
        if (StringUtils.hasLength(redisCode)) {
            if (redisCode.equalsIgnoreCase(code)) {
                Long expire = redisTemplate.boundHashOps(Constants.KAPTCHA_SESSION_KEY).getExpire();
                if (!ObjectUtils.isEmpty(expire) && expire < 0L) {
                    throw new BizException(EXPIRE_CAPTCHA);
                }
            } else {
                throw new BizException(ERROR_CAPTCHA);
            }
        } else {
            throw new BizException(EXPIRE_CAPTCHA);
        }
        // 检查用户名密码
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getPassword, User::getAvatarUrl)
                .eq(User::getUsername, username);
        User user = userRepository.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(user) && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        throw new BizException(ERROR_USER);
    }

    private UserDTO login(User user) {
        StpUtil.login(user.getId());
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setAvatarUrl(user.getAvatarUrl());
        userDTO.setToken(StpUtil.getTokenValue());
        userDTO.setRole(String.join(",", StpUtil.getRoleList()));
        return userDTO;
    }
}
