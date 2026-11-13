package com.school.management.dto;

import lombok.Data;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

/**
 * 成绩录入DTO
 */
@Data
public class GradeDTO {
    private Long id;
    private Long studentId;
    private Long courseId;

    @DecimalMin(value = "0", message = "分数不能小于0")
    @DecimalMax(value = "100", message = "分数不能大于100")
    private java.math.BigDecimal score;
    private java.math.BigDecimal gradePoint;
    private String examType;
    private String semester;
    private String remark;
}
