package com.school.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教师信息实体
 */
@Data
@TableName("teacher_info")
public class TeacherInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String teacherNo;
    private Long userId;
    private String name;
    private Integer gender;
    private String idCard;
    private String phone;
    private String email;
    private Long deptId;
    private String title;
    private String education;
    private LocalDate hireDate;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
