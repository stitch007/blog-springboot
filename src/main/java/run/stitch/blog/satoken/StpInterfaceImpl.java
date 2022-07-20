package run.stitch.blog.satoken;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import run.stitch.blog.constant.Role;
import run.stitch.blog.service.UserService;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Role role = userService.getRole(Integer.parseInt(loginId.toString()));
        return List.of(role.toString());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Role role = userService.getRole(Integer.parseInt(loginId.toString()));
        return List.of(role.toString());
    }
}
