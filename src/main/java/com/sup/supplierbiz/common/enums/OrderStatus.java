package com.sup.supplierbiz.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING(1, "待审核"),
    APPROVED(2, "已审核"),
    CLOSED(3, "已关闭");
    @EnumValue
    private final Integer value;
    private final String message;
}
