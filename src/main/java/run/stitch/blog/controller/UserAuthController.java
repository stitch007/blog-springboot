package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import run.stitch.blog.dto.params.LoginParam;
import run.stitch.blog.dto.params.OauthLoginParam;
import run.stitch.blog.dto.UserDTO;
import run.stitch.blog.service.UserAuthService;
import run.stitch.blog.util.Result;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static run.stitch.blog.util.StatusCode.*;

@RestController
public class UserAuthController {
    @Autowired
    UserAuthService userAuthService;

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @PostMapping("/login")
    public Result<?> login(@RequestBody @Validated LoginParam loginParam) {
        UserDTO userDTO = userAuthService.login(loginParam);
        if (ObjectUtils.isEmpty(userDTO)) {
            return Result.error(LOGIN_FAIL);
        }
        return Result.ok("登录成功", userDTO);
    }

    @PostMapping("/login/gitee")
    public Result<?> giteeLogin(@RequestBody @Validated OauthLoginParam oauthLoginParam) {
        UserDTO userDTO = userAuthService.giteeLogin(oauthLoginParam);
        if (ObjectUtils.isEmpty(userDTO)) {
            return Result.error(LOGIN_FAIL);
        }
        return Result.ok("登录成功", userDTO);
    }

    @SaCheckLogin
    @GetMapping("/logout")
    public Result<String> logout() {
        StpUtil.logout();
        return Result.ok();
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
