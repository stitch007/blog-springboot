package run.stitch.blog.exception;

import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import run.stitch.blog.util.Result;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<?> exceptionHandle(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error(500, "服务器内部错误");
    }

    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public Result<?> bizExceptionHandle(BizException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseBody
    public Result<?> noHandlerFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error(404, "资源不存在");
    }

    // sa-token
    @ExceptionHandler(value = NotLoginException.class)
    @ResponseBody
    public Result<?> notLoginExceptionHandle(NotLoginException e) {
        log.error(e.getMessage(), e);
        return Result.error(401, "未登录");
    }

    @ExceptionHandler(value = NotRoleException.class)
    @ResponseBody
    public Result<?> notRoleException(NotRoleException e) {
        log.error(e.getMessage(), e);
        return Result.error(403, "角色不存在");
    }

    @ExceptionHandler(value = NotPermissionException.class)
    @ResponseBody
    public Result<?> notPermissionException(NotPermissionException e) {
        log.error(e.getMessage(), e);
        return Result.error(403, "无权限");
    }

    @ExceptionHandler(value = DisableLoginException.class)
    @ResponseBody
    public Result<?> disableLoginException(DisableLoginException e) {
        log.error(e.getMessage(), e);
        return Result.error(403, "账号被封禁");
    }
}
