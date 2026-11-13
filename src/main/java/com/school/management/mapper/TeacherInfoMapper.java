package com.school.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.management.entity.TeacherInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 教师Mapper
 */
public interface TeacherInfoMapper extends BaseMapper<TeacherInfo> {

    @Select("SELECT t.*, s.real_name as teacher_name, s.phone as phone " +
            "FROM teacher_info t LEFT JOIN sys_user s ON t.user_id = s.id " +
            "WHERE t.id = #{id}")
    Map<String, Object> selectDetailById(Long id);

    @Select("SELECT DISTINCT t.* FROM teacher_info t " +
            "LEFT JOIN course_teacher ct ON t.id = ct.teacher_id " +
            "WHERE ct.course_id = #{courseId}")
    List<TeacherInfo> selectByCourseId(Long courseId);
}
