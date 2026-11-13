package com.school.management.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * 课程信息DTO
 */
@Data
public class CourseDTO {
    private Long id;
    @NotBlank(message = "课程编码不能为空")
    private String courseCode;
    @NotBlank(message = "课程名称不能为空")
    private String courseName;
    private String courseType;

    @DecimalMin(value = "0.1", message = "学分必须大于0")
    private java.math.BigDecimal credits;

    @Min(value = 1, message = "学时必须大于0")
    private Integer hours;
    private Long deptId;
    private String description;
    private Integer status;
    private List<Long> teacherIds;
}
