package com.school.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 学生统计项
 */
@Data
@AllArgsConstructor
public class StudentStatItem {
    private String className;
    private Long count;
}
