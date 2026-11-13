package com.school.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 排课实体
 */
@Data
@TableName("course_schedule")
public class CourseSchedule implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long courseId;
    private Long teacherId;
    private Long classId;
    private String weekRange;
    private Integer dayOfWeek;
    private Integer startPeriod;
    private Integer endPeriod;
    private String roomId;
    private String semester;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
