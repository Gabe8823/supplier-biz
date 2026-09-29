package com.sup.supplierbiz.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Deleted {
    DELETED(1,"删除"),
    UNDELETED(0,"未删");
    @EnumValue
    private final Integer value;
    private final String message;
}
