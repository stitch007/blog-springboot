package run.stitch.blog.exception;

import run.stitch.blog.util.StatusCode;

public class BizException extends RuntimeException {
    private final Integer code;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.code = statusCode.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
