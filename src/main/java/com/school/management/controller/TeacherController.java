package com.school.management.controller;

import com.school.management.common.result.PageResult;
import com.school.management.common.result.Result;
import com.school.management.dto.TeacherDTO;
import com.school.management.dto.TeacherQuery;
import com.school.management.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 教师管理控制器
 */
@Tag(name = "教师管理", description = "教师信息的增删改查")
@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(summary = "分页查询教师列表")
    @GetMapping("/list")
    public Result<PageResult<TeacherDTO>> listTeachers(TeacherQuery query) {
        return Result.success(teacherService.listTeachers(query));
    }

    @Operation(summary = "根据ID获取教师详情")
    @GetMapping("/{id}")
    public Result<TeacherDTO> getTeacher(@PathVariable Long id) {
        return Result.success(teacherService.getTeacherDetail(id));
    }

    @Operation(summary = "添加教师")
    @PostMapping
    public Result<Void> addTeacher(@Valid @RequestBody TeacherDTO dto) {
        teacherService.addTeacher(dto);
        return Result.success();
    }

    @Operation(summary = "更新教师信息")
    @PutMapping("/{id}")
    public Result<Void> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDTO dto) {
        dto.setId(id);
        teacherService.updateTeacher(dto);
        return Result.success();
    }

    @Operation(summary = "删除教师")
    @DeleteMapping("/{id}")
    public Result<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return Result.success();
    }
}
