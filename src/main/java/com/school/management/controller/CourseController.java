package com.school.management.controller;

import com.school.management.common.result.PageResult;
import com.school.management.common.result.Result;
import com.school.management.dto.CourseDTO;
import com.school.management.dto.CourseQuery;
import com.school.management.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 课程管理控制器
 */
@Tag(name = "课程管理", description = "课程的增删改查和教师分配")
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "分页查询课程列表")
    @GetMapping("/list")
    public Result<PageResult<CourseDTO>> listCourses(CourseQuery query) {
        return Result.success(courseService.listCourses(query));
    }

    @Operation(summary = "根据ID获取课程详情")
    @GetMapping("/{id}")
    public Result<CourseDTO> getCourse(@PathVariable Long id) {
        return Result.success(courseService.getCourseDetail(id));
    }

    @Operation(summary = "添加课程")
    @PostMapping
    public Result<Void> addCourse(@Valid @RequestBody CourseDTO dto) {
        courseService.addCourse(dto);
        return Result.success();
    }

    @Operation(summary = "更新课程信息")
    @PutMapping("/{id}")
    public Result<Void> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO dto) {
        dto.setId(id);
        courseService.updateCourse(dto);
        return Result.success();
    }

    @Operation(summary = "删除课程")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return Result.success();
    }

    @Operation(summary = "分配教师")
    @PostMapping("/{courseId}/teachers/{teacherId}")
    public Result<Void> assignTeacher(@PathVariable Long courseId,
                                       @PathVariable Long teacherId,
                                       @RequestParam(defaultValue = "false") boolean isPrimary) {
        courseService.assignTeacher(courseId, teacherId, isPrimary);
        return Result.success();
    }
}
