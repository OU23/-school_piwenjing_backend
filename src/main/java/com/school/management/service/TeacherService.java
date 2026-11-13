package com.school.management.service;

import com.school.management.common.result.PageResult;
import com.school.management.dto.TeacherDTO;
import com.school.management.dto.TeacherQuery;
import com.school.management.entity.TeacherInfo;

/**
 * 教师服务接口
 */
public interface TeacherService {
    PageResult<TeacherDTO> listTeachers(TeacherQuery query);
    TeacherDTO getTeacherDetail(Long id);
    void addTeacher(TeacherDTO dto);
    void updateTeacher(TeacherDTO dto);
    void deleteTeacher(Long id);
}
