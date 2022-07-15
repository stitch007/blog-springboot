package run.stitch.blog.exception;

import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import run.stitch.blog.util.Result;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

import static run.stitch.blog.util.StatusCode.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<?> exceptionHandle(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error(SYSTEM_ERROR);
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
        return Result.error(NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String error = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return Result.error(VALID_ERROR, error);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Result constraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String error = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return Result.error(VALID_ERROR, error);
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result bindException(BindException e) {
        log.error(e.getMessage(), e);
        String error = e.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return Result.error(VALID_ERROR, error);
    }

    // sa-token
    @ExceptionHandler(value = NotLoginException.class)
    @ResponseBody
    public Result<?> notLoginExceptionHandle(NotLoginException e) {
        log.error(e.getMessage(), e);
        return Result.error(NO_LOGIN);
    }

    @ExceptionHandler(value = NotRoleException.class)
    @ResponseBody
    public Result<?> notRoleException(NotRoleException e) {
        log.error(e.getMessage(), e);
        return Result.error(NO_ROLE);
    }

    @ExceptionHandler(value = NotPermissionException.class)
    @ResponseBody
    public Result<?> notPermissionException(NotPermissionException e) {
        log.error(e.getMessage(), e);
        return Result.error(NO_AUTHORIZED);
    }

    @ExceptionHandler(value = DisableLoginException.class)
    @ResponseBody
    public Result<?> disableLoginException(DisableLoginException e) {
        log.error(e.getMessage(), e);
        return Result.error(BAN);
    }
}
