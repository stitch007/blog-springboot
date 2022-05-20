package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import run.stitch.blog.dto.LoginDTO;
import run.stitch.blog.service.UserAuthService;
import run.stitch.blog.util.Result;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RestController
public class UserAuthController {
    @Autowired
    UserAuthService userAuthService;

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO) {
        Integer userId = userAuthService.check(loginDTO);
        if (userId != null) {
            StpUtil.login(userId);
            return Result.ok("登录成功", new HashMap<>() {{
                put("token", StpUtil.getTokenValue());
                put("role", String.join(",", StpUtil.getRoleList()));
            }});
        }
        return Result.error(401, "登录失败");
    }

    @SaCheckLogin
    @GetMapping("/logout")
    public Result<String> logout() {
        StpUtil.logout();
        return Result.ok("退出成功");
    }

    @GetMapping("/captcha")
    public void createCaptcha(HttpServletResponse response) throws IOException {
        // 禁止缓存 响应png格式图片
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(userAuthService.createCaptcha(), "png", out);
        out.flush();
        out.close();
    }
}
