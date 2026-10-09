package com.sup.supplierbiz.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 启用/禁用状态枚举
 *
 * @author sup
 * @date 2026-10-09
 */
@Getter
@AllArgsConstructor
public enum Status {
    /** 启用 */
    ENABLE(1,"启用"),
    /** 禁用 */
    DISABLE(0,"禁用");

    @EnumValue
    private final Integer value;
    private final String message;

}
