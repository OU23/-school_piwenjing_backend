package com.school.management.controller;

import com.school.management.common.result.PageResult;
import com.school.management.common.result.Result;
import com.school.management.dto.StudentDTO;
import com.school.management.dto.StudentQuery;
import com.school.management.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生管理控制器
 */
@Tag(name = "学生管理", description = "学生信息的增删改查")
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "分页查询学生列表")
    @GetMapping("/list")
    public Result<PageResult<StudentDTO>> listStudents(StudentQuery query) {
        return Result.success(studentService.listStudents(query));
    }

    @Operation(summary = "根据ID获取学生详情")
    @GetMapping("/{id}")
    public Result<StudentDTO> getStudent(@PathVariable Long id) {
        return Result.success(studentService.getStudentDetail(id));
    }

    @Operation(summary = "添加学生")
    @PostMapping
    public Result<Void> addStudent(@Valid @RequestBody StudentDTO dto) {
        studentService.addStudent(dto);
        return Result.success();
    }

    @Operation(summary = "更新学生信息")
    @PutMapping("/{id}")
    public Result<Void> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO dto) {
        dto.setId(id);
        studentService.updateStudent(dto);
        return Result.success();
    }

    @Operation(summary = "删除学生")
    @DeleteMapping("/{id}")
    public Result<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return Result.success();
    }

    @Operation(summary = "批量导入学生")
    @PostMapping("/batch")
    public Result<Void> batchImport(@RequestBody List<StudentDTO> students) {
        studentService.batchImportStudents(students);
        return Result.success();
    }
}
