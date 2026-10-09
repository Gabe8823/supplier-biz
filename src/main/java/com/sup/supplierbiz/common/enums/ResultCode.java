package com.sup.supplierbiz.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一响应状态码枚举
 *
 * @author sup
 * @date 2026-10-09
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    /** 操作成功 */
    SUCCESS(200,"操作成功"),
    /** 参数错误 */
    PARAM_ERROR(4001,"参数错误"),
    /** 库存不足 */
    STOCK_NOT_ENOUGH(4002,"库存不足"),
    /** 用户名或密码错误 */
    LOGIN_FAILED(4003,"用户名或密码错误"),
    /** 请求资源不存在 */
    NOT_FOUND(4004,"请求资源不存在"),
    /** 账号已停用 */
    ACCOUNT_DISABLED(4005,"账号已停用"),
    /** 系统内部错误 */
    SYSTEM_ERROR(5000, "系统内部错误"),
    /** 没有访问权限 */
    UNAUTHORIZED(401,"没有访问权限");
    private final Integer code;
    private final String message;


}
