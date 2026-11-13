package com.school.management.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色类型
 */
@Getter
@AllArgsConstructor
public enum RoleTypeEnum {
    ADMIN("admin", "系统管理员"),
    TEACHER("teacher", "教师"),
    STUDENT("student", "学生");

    private final String code;
    private final String desc;
}
