package run.stitch.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import run.stitch.blog.enums.StatusCodeEnum;

import static run.stitch.blog.enums.StatusCodeEnum.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;

    private String message;

    private T data;

    public static <T> Result<T> ok() {
        return new Result<>(SUCCESS.getCode(), SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(SUCCESS.getCode(), SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> ok(StatusCodeEnum statusCodeEnum) {
        return new Result<>(statusCodeEnum.getCode(), statusCodeEnum.getMessage(), null);
    }

    public static <T> Result<T> ok(StatusCodeEnum statusCodeEnum, T data) {
        return new Result<>(statusCodeEnum.getCode(), statusCodeEnum.getMessage(), data);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> error(StatusCodeEnum statusCodeEnum) {
        return new Result<>(statusCodeEnum.getCode(), statusCodeEnum.getMessage(), null);
    }

    public static <T> Result<T> error(StatusCodeEnum statusCodeEnum, T data) {
        return new Result<>(statusCodeEnum.getCode(), statusCodeEnum.getMessage(), data);
    }
}
