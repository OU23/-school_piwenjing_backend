package com.school.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.management.entity.StudentInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 学生Mapper
 */
public interface StudentInfoMapper extends BaseMapper<StudentInfo> {

    @Select("SELECT COUNT(*) FROM student_info WHERE class_id = #{classId} AND status = 1")
    int countByClassId(@Param("classId") Long classId);

    @Select("SELECT * FROM student_info si " +
            "LEFT JOIN class_info c ON si.class_id = c.id " +
            "WHERE si.id = #{id}")
    Map<String, Object> selectDetailById(@Param("id") Long id);

    @Select("SELECT * FROM student_info si " +
            "LEFT JOIN class_info c ON si.class_id = c.id " +
            "WHERE si.class_id = #{classId}")
    List<Map<String, Object>> selectByClassId(@Param("classId") Long classId);
}
