package com.school.management.dto;

import lombok.Data;

/**
 * 课程查询条件
 */
@Data
public class CourseQuery {
    private String keyword;
    private String courseType;
    private Long deptId;
    private Integer pageNum;
    private Integer pageSize;
}
