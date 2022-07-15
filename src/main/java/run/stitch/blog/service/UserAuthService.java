package run.stitch.blog.service;

import run.stitch.blog.constant.Role;
import run.stitch.blog.dto.params.LoginParam;
import run.stitch.blog.dto.params.OauthLoginParam;
import run.stitch.blog.dto.UserDTO;

import java.awt.image.BufferedImage;

public interface UserAuthService {
    Role getRole(Integer userId);

    BufferedImage createCaptcha();

    UserDTO login(LoginParam loginParam);

    UserDTO giteeLogin(OauthLoginParam oauthLoginParam);
}
