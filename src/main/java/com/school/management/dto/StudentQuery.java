package com.school.management.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 学生查询条件
 */
@Data
public class StudentQuery {
    private String keyword;
    private Long classId;
    private Integer status;
    private Integer gender;
    private Integer pageNum;
    private Integer pageSize;
}
