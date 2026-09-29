package com.sup.supplierbiz.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200,"操作成功"),
    PARAM_ERROR(4001,"参数错误"),
    NOT_FOUND(4004,"请求资源不存在"),
    SYSTEM_ERROR(5000, "系统内部错误"),
    STOCK_NOT_ENOUGH(4002,"库存不足");
    private final Integer code;
    private final String message;


}
