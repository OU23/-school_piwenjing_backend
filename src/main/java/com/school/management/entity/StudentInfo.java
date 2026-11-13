package com.school.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生信息实体
 */
@Data
@TableName("student_info")
public class StudentInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String studentNo;
    private Long userId;
    private String name;
    private Integer gender;
    private String idCard;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private Long classId;
    private Integer entranceYear;
    private String address;
    private String parentName;
    private String parentPhone;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
