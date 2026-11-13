package com.school.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.management.entity.GradeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 成绩Mapper
 */
public interface GradeInfoMapper extends BaseMapper<GradeInfo> {

    @Select("SELECT g.*, s.name as student_name, s.student_no, " +
            "c.course_name, c.course_code, c.credits " +
            "FROM grade_info g " +
            "LEFT JOIN student_info s ON g.student_id = s.id " +
            "LEFT JOIN course_info c ON g.course_id = c.id " +
            "WHERE g.student_id = #{studentId} AND g.semester = #{semester} " +
            "ORDER BY c.course_name")
    List<Map<String, Object>> selectByStudentAndSemester(@Param("studentId") Long studentId,
                                                         @Param("semester") String semester);

    @Select("SELECT g.*, s.name as student_name, s.student_no, " +
            "c.course_name, c.course_code " +
            "FROM grade_info g " +
            "LEFT JOIN student_info s ON g.student_id = s.id " +
            "LEFT JOIN course_info c ON g.course_id = c.id " +
            "WHERE g.course_id = #{courseId} AND g.semester = #{semester} " +
            "ORDER BY s.student_no")
    List<Map<String, Object>> selectByCourseAndSemester(@Param("courseId") Long courseId,
                                                         @Param("semester") String semester);

    @Select("SELECT " +
            "COUNT(*) as total, " +
            "AVG(score) as avg_score, " +
            "MAX(score) as max_score, " +
            "MIN(score) as min_score, " +
            "SUM(CASE WHEN score >= 90 THEN 1 ELSE 0 END) as excellent_count, " +
            "SUM(CASE WHEN score >= 80 AND score < 90 THEN 1 ELSE 0 END) as good_count, " +
            "SUM(CASE WHEN score >= 60 AND score < 80 THEN 1 ELSE 0 END) as pass_count, " +
            "SUM(CASE WHEN score < 60 THEN 1 ELSE 0 END) as fail_count " +
            "FROM grade_info WHERE course_id = #{courseId} AND semester = #{semester}")
    Map<String, Object> selectStatistics(@Param("courseId") Long courseId,
                                          @Param("semester") String semester);
}
