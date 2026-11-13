package com.school.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程教师关联实体
 */
@Data
@TableName("course_teacher")
public class CourseTeacher implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long courseId;
    private Long teacherId;
    private Integer isPrimary;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
