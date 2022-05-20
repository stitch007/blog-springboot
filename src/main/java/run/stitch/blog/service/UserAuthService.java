package run.stitch.blog.service;

import run.stitch.blog.constant.Role;
import run.stitch.blog.dto.LoginDTO;

import java.awt.image.BufferedImage;

public interface UserAuthService {
    Integer check(LoginDTO loginDTO);

    Role getRole(Integer userId);

    BufferedImage createCaptcha();
}
