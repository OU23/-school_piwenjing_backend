package com.school.management.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * 教师信息DTO
 */
@Data
public class TeacherDTO {
    private Long id;
    private String teacherNo;
    private Long userId;
    private String name;
    private Integer gender;
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;
    private Long deptId;
    private String title;
    private String education;
    private java.time.LocalDate hireDate;
    private Integer status;
}
