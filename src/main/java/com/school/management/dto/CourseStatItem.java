package com.school.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 课程统计项
 */
@Data
@AllArgsConstructor
public class CourseStatItem {
    private String courseType;
    private Long count;
    private java.math.BigDecimal totalCredits;
}
