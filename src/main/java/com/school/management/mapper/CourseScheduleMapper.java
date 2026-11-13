package com.school.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.management.entity.CourseSchedule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 排课Mapper
 */
public interface CourseScheduleMapper extends BaseMapper<CourseSchedule> {

    @Select("SELECT cs.*, c.course_name, c.course_code, t.name as teacher_name, cl.class_name " +
            "FROM course_schedule cs " +
            "LEFT JOIN course_info c ON cs.course_id = c.id " +
            "LEFT JOIN teacher_info t ON cs.teacher_id = t.id " +
            "LEFT JOIN class_info cl ON cs.class_id = cl.id " +
            "WHERE cs.class_id = #{classId} AND cs.semester = #{semester} " +
            "ORDER BY cs.day_of_week, cs.start_period")
    List<Map<String, Object>> selectByClassAndSemester(@Param("classId") Long classId,
                                                        @Param("semester") String semester);

    @Select("SELECT cs.*, c.course_name, c.course_code, t.name as teacher_name, cl.class_name " +
            "FROM course_schedule cs " +
            "LEFT JOIN course_info c ON cs.course_id = c.id " +
            "LEFT JOIN teacher_info t ON cs.teacher_id = t.id " +
            "LEFT JOIN class_info cl ON cs.class_id = cl.id " +
            "WHERE cs.teacher_id = #{teacherId} AND cs.semester = #{semester} " +
            "ORDER BY cs.day_of_week, cs.start_period")
    List<Map<String, Object>> selectByTeacherAndSemester(@Param("teacherId") Long teacherId,
                                                          @Param("semester") String semester);
}
