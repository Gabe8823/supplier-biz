package com.sup.supplierbiz.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ENABLE(1,"启用"),
    DISABLE(0,"禁用");

    @EnumValue
    private final Integer value;
    private final String message;

}
