package com.sup.supplierbiz.common.result;

import com.sup.supplierbiz.common.enums.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.sup.supplierbiz.common.enums.ResultCode.SUCCESS;

/**
 * 统一响应封装
 *
 * @author sup
 * @date 2026-10-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "统一响应体")
public class Result<T> {
    @Schema(description = "状态码，200成功")
    private Integer code;
    @Schema(description = "提示信息")
    private String message;
    @Schema(description = "业务数据")
    private T data;

    public static<T> Result<T> success(T data){
        return new Result<>(ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),data);
    }
    public static<T> Result<T> success(){
        return new Result<>(SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),null);
    }
    public static<T> Result<T> error(ResultCode resultCode){
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }
    public static<T> Result<T> error(ResultCode resultCode ,String message){
        return new Result<>(resultCode.getCode(),message,null);
    }
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
