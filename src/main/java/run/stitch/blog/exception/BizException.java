package run.stitch.blog.exception;

import run.stitch.blog.enums.StatusCodeEnum;

public class BizException extends RuntimeException {
    private final Integer code;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(StatusCodeEnum statusCodeEnum) {
        super(statusCodeEnum.getMessage());
        this.code = statusCodeEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
