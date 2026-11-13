package com.school.management.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 学生状态
 */
@Getter
@AllArgsConstructor
public enum StudentStatusEnum {
    SUSPENDED(0, "休学"),
    ACTIVE(1, "在读"),
    GRADUATED(2, "毕业"),
    DROPPED(3, "退学");

    private final Integer code;
    private final String desc;
}
