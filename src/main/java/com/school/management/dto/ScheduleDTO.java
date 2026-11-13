package com.school.management.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * 排课DTO
 */
@Data
public class ScheduleDTO {
    private Long id;
    private Long courseId;
    private Long teacherId;
    private Long classId;
    private String weekRange;

    @Min(value = 1, message = "星期范围1-7")
    private Integer dayOfWeek;
    @Min(value = 1, message = "起始节次最小为1")
    private Integer startPeriod;
    @Min(value = 1, message = "结束节次最小为1")
    private Integer endPeriod;
    private String roomId;
    @NotBlank(message = "学期不能为空")
    private String semester;
    private Integer status;
}
