package run.stitch.blog.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    SUCCESS(20000, "操作成功"),
    NO_LOGIN(40100, "用户未登录"),
    LOGIN_FAIL(40101, "登录失败"),
    ERROR_USER(40102, "用户名或密码错误"),
    EXPIRE_CAPTCHA(40103, "验证码已过期"),
    ERROR_CAPTCHA(40104, "验证码错误"),
    NO_AUTHORIZED(40300, "无权限"),
    NO_ROLE(40301, "角色不存在"),
    BAN(40302, "账号被封禁"),
    NOT_FOUND(40400, "资源不存在"),
    SYSTEM_ERROR(50000, "系统异常"),
    FAIL(51000, "操作失败"),
    EXISTED(51001, "资源已存在"),
    NO_EXISTED(51002, "资源不存在"),
    VALID_ERROR(52000, "参数校验失败");

    private final Integer code;

    private final String message;
}
