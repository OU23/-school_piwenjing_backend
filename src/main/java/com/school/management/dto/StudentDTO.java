package com.school.management.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

/**
 * 学生信息DTO
 */
@Data
public class StudentDTO {
    private Long id;
    private String studentNo;
    private Long userId;
    private String name;
    private Integer gender;

    @Pattern(regexp = "^(\\d{17}[xX\\d])$", message = "身份证号格式不正确")
    private String idCard;
    private java.time.LocalDate birthDate;
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;
    private Long classId;
    private Integer entranceYear;
    private String address;
    private String parentName;
    private String parentPhone;
    private Integer status;
}
