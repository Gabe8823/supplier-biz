package com.sup.supplierbiz.common.exception;

import com.sup.supplierbiz.common.enums.ResultCode;
import lombok.Getter;


/**
 * 业务异常
 *
 * @author sup
 * @date 2026-10-09
 */
@Getter
public class BusinessException extends RuntimeException{
    private final Integer code;

    public BusinessException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }
}
