package com.sup.supplierbiz.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逻辑删除状态枚举
 *
 * @author sup
 * @date 2026-10-09
 */
@Getter
@AllArgsConstructor
public enum Deleted {
    /** 已删除 */
    DELETED(1,"删除"),
    /** 未删除 */
    UNDELETED(0,"未删");
    @EnumValue
    private final Integer value;
    private final String message;
}
