package com.sup.supplierbiz.common.advice;

import com.sup.supplierbiz.common.enums.ResultCode;
import com.sup.supplierbiz.common.exception.BusinessException;
import com.sup.supplierbiz.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author sup
 * @date 2026-10-09
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e){
        log.error("业务异常：code={},message={}",e.getCode(),e.getMessage());
        return  Result.error(e.getCode(),e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidateException(MethodArgumentNotValidException e){
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("参数校验异常：{}",msg);
        return Result.error(ResultCode.PARAM_ERROR, msg);
    }
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        log.error("系统异常：{}",e.getMessage());
        return Result.error(ResultCode.SYSTEM_ERROR);
    }
}
