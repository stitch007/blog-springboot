package run.stitch.blog.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static run.stitch.blog.util.StatusCode.*;

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

    public static <T> Result<T> ok(StatusCode statusCode) {
        return new Result<>(statusCode.getCode(), statusCode.getMessage(), null);
    }

    public static <T> Result<T> ok(StatusCode statusCode, T data) {
        return new Result<>(statusCode.getCode(), statusCode.getMessage(), data);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> error(StatusCode statusCode) {
        return new Result<>(statusCode.getCode(), statusCode.getMessage(), null);
    }

    public static <T> Result<T> error(StatusCode statusCode, T data) {
        return new Result<>(statusCode.getCode(), statusCode.getMessage(), data);
    }
}
