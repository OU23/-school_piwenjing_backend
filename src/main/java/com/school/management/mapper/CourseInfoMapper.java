package com.school.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.management.entity.CourseInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 课程Mapper
 */
public interface CourseInfoMapper extends BaseMapper<CourseInfo> {

    @Select("SELECT c.*, GROUP_CONCAT(t.name SEPARATOR ',') as teacher_names " +
            "FROM course_info c " +
            "LEFT JOIN course_teacher ct ON c.id = ct.course_id " +
            "LEFT JOIN teacher_info t ON ct.teacher_id = t.id " +
            "WHERE c.id = #{id} " +
            "GROUP BY c.id")
    Map<String, Object> selectDetailById(Long id);

    @Select("SELECT c.*, COUNT(g.id) as student_count " +
            "FROM course_info c " +
            "LEFT JOIN grade_info g ON c.id = g.course_id " +
            "GROUP BY c.id")
    List<Map<String, Object>> selectWithStatistics();
}
