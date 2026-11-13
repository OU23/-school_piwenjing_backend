package com.school.management.service;

import com.school.management.common.result.PageResult;
import com.school.management.dto.CourseDTO;
import com.school.management.dto.CourseQuery;
import com.school.management.entity.CourseInfo;

/**
 * 课程服务接口
 */
public interface CourseService {
    PageResult<CourseDTO> listCourses(CourseQuery query);
    CourseDTO getCourseDetail(Long id);
    void addCourse(CourseDTO dto);
    void updateCourse(CourseDTO dto);
    void deleteCourse(Long id);
    void assignTeacher(Long courseId, Long teacherId, boolean isPrimary);
}
