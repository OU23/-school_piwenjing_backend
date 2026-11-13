package com.school.management.dto;

import lombok.Data;

/**
 * 统计数据
 */
@Data
public class StatisticsVO {
    private Long totalStudents;
    private Long totalTeachers;
    private Long totalCourses;
    private Long totalClasses;
    private java.util.List<StudentStatItem> studentByClass;
    private java.util.List<CourseStatItem> courseByType;
}
