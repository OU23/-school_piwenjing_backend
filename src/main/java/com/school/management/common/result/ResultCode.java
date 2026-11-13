package com.school.management.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    USER_NOT_FOUND(1001, "用户不存在"),
    PASSWORD_ERROR(1002, "密码错误"),
    USER_DISABLED(1003, "用户已被禁用"),
    TOKEN_EXPIRED(1004, "Token已过期"),
    TOKEN_INVALID(1005, "Token无效"),
    USERNAME_EXISTS(1006, "用户名已存在"),
    STUDENT_NOT_FOUND(2001, "学生不存在"),
    TEACHER_NOT_FOUND(2002, "教师不存在"),
    COURSE_NOT_FOUND(2003, "课程不存在"),
    GRADE_NOT_FOUND(2004, "成绩不存在"),
    PERMISSION_DENIED(3001, "权限不足");

    private final Integer code;
    private final String message;
}
