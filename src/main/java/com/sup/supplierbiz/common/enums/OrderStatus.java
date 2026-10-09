package com.sup.supplierbiz.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author sup
 * @date 2026-10-09
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    /** 待审核 */
    PENDING(1, "待审核"),
    /** 已审核 */
    APPROVED(2, "已审核"),
    /** 已关闭 */
    CLOSED(3, "已关闭");
    @EnumValue
    private final Integer value;
    private final String message;
}
