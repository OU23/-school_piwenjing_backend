package com.school.management.service;

import com.school.management.common.result.PageResult;
import com.school.management.dto.StudentDTO;
import com.school.management.dto.StudentQuery;
import com.school.management.entity.StudentInfo;

/**
 * 学生服务接口
 */
public interface StudentService {
    /**
     * 分页查询学生列表
     */
    PageResult<StudentDTO> listStudents(StudentQuery query);

    /**
     * 根据ID获取学生详情
     */
    StudentDTO getStudentDetail(Long id);

    /**
     * 添加学生
     */
    void addStudent(StudentDTO dto);

    /**
     * 更新学生信息
     */
    void updateStudent(StudentDTO dto);

    /**
     * 删除学生
     */
    void deleteStudent(Long id);

    /**
     * 批量导入学生
     */
    void batchImportStudents(java.util.List<StudentDTO> students);
}
