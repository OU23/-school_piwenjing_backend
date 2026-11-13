package com.school.management.controller;

import com.school.management.common.result.Result;
import com.school.management.dto.StatisticsVO;
import com.school.management.entity.*;
import com.school.management.mapper.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计看板控制器
 */
@Tag(name = "数据统计", description = "系统数据看板统计")
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StudentInfoMapper studentInfoMapper;
    private final TeacherInfoMapper teacherInfoMapper;
    private final CourseInfoMapper courseInfoMapper;
    private final SysUserMapper sysUserMapper;

    @Operation(summary = "获取系统统计数据")
    @GetMapping("/dashboard")
    public Result<StatisticsVO> getDashboard() {
        StatisticsVO vo = new StatisticsVO();

        // 总学生数
        vo.setTotalStudents((long) studentInfoMapper.selectCount(null));

        // 总教师数
        vo.setTotalTeachers((long) teacherInfoMapper.selectCount(null));

        // 总课程数
        vo.setTotalCourses((long) courseInfoMapper.selectCount(null));

        // 总班级数
        long totalClasses = studentInfoMapper.selectList(null).stream()
                .map(StudentInfo::getClassId)
                .distinct()
                .count();
        vo.setTotalClasses(totalClasses);

        // 按班级统计学生
        List<StudentInfo> students = studentInfoMapper.selectList(null);
        Map<Long, Long> classCount = new HashMap<>();
        for (StudentInfo s : students) {
            if (s.getClassId() != null) {
                classCount.merge(s.getClassId(), 1L, Long::sum);
            }
        }
        vo.setStudentByClass(classCount.entrySet().stream()
                .map(e -> new com.school.management.dto.StudentStatItem("班级" + e.getKey(), e.getValue()))
                .collect(java.util.stream.Collectors.toList()));

        // 按课程类型统计
        List<CourseInfo> courses = courseInfoMapper.selectList(null);
        Map<String, com.school.management.dto.CourseStatItem> typeCount = new HashMap<>();
        for (CourseInfo c : courses) {
            typeCount.computeIfAbsent(c.getCourseType(), k ->
                    new com.school.management.dto.CourseStatItem(k, 0L, java.math.BigDecimal.ZERO));
            typeCount.get(c.getCourseType()).setCount(typeCount.get(c.getCourseType()).getCount() + 1);
            typeCount.get(c.getCourseType()).setTotalCredits(
                    typeCount.get(c.getCourseType()).getTotalCredits().add(c.getCredits()));
        }
        vo.setCourseByType(new java.util.ArrayList<>(typeCount.values()));

        return Result.success(vo);
    }

    @Operation(summary = "获取操作日志")
    @GetMapping("/logs")
    public Result<List<Map<String, Object>>> getLogs() {
        return Result.success(sysUserMapper.selectList(null).stream()
                .limit(50)
                .map(u -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", u.getId());
                    map.put("username", u.getUsername());
                    map.put("roleType", u.getRoleType());
                    return map;
                })
                .collect(java.util.stream.Collectors.toList()));
    }
}
