package com.school.management.dto;

import lombok.Data;

/**
 * 教师查询条件
 */
@Data
public class TeacherQuery {
    private String keyword;
    private Long deptId;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;
}
