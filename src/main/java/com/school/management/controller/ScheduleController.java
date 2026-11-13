package com.school.management.controller;

import com.school.management.common.result.Result;
import com.school.management.entity.CourseSchedule;
import com.school.management.mapper.CourseScheduleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 排课管理控制器
 */
@Tag(name = "排课管理", description = "课程安排和课表查询")
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final CourseScheduleMapper scheduleMapper;

    @Operation(summary = "查询班级课表")
    @GetMapping("/class/{classId}")
    public Result<List<Map<String, Object>>> getByClass(@PathVariable Long classId,
                                                          @RequestParam String semester) {
        return Result.success(scheduleMapper.selectByClassAndSemester(classId, semester));
    }

    @Operation(summary = "查询教师课表")
    @GetMapping("/teacher/{teacherId}")
    public Result<List<Map<String, Object>>> getByTeacher(@PathVariable Long teacherId,
                                                            @RequestParam String semester) {
        return Result.success(scheduleMapper.selectByTeacherAndSemester(teacherId, semester));
    }

    @Operation(summary = "添加排课")
    @PostMapping
    public Result<Void> addSchedule(@RequestBody CourseSchedule schedule) {
        scheduleMapper.insert(schedule);
        return Result.success();
    }

    @Operation(summary = "更新排课")
    @PutMapping("/{id}")
    public Result<Void> updateSchedule(@PathVariable Long id, @RequestBody CourseSchedule schedule) {
        schedule.setId(id);
        scheduleMapper.updateById(schedule);
        return Result.success();
    }

    @Operation(summary = "删除排课")
    @DeleteMapping("/{id}")
    public Result<Void> deleteSchedule(@PathVariable Long id) {
        scheduleMapper.deleteById(id);
        return Result.success();
    }
}
