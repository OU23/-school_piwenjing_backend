package com.school.management.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.management.common.result.PageResult;
import com.school.management.common.result.Result;
import com.school.management.dto.GradeDTO;
import com.school.management.entity.GradeInfo;
import com.school.management.mapper.GradeInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 成绩管理控制器
 */
@Tag(name = "成绩管理", description = "成绩的录入、查询和统计")
@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeInfoMapper gradeInfoMapper;

    @Operation(summary = "查询学生某学期成绩")
    @GetMapping("/student/{studentId}")
    public Result<List<Map<String, Object>>> getByStudent(@PathVariable Long studentId,
                                                           @RequestParam String semester) {
        return Result.success(gradeInfoMapper.selectByStudentAndSemester(studentId, semester));
    }

    @Operation(summary = "查询课程某学期成绩")
    @GetMapping("/course/{courseId}")
    public Result<List<Map<String, Object>>> getByCourse(@PathVariable Long courseId,
                                                           @RequestParam String semester) {
        return Result.success(gradeInfoMapper.selectByCourseAndSemester(courseId, semester));
    }

    @Operation(summary = "成绩统计")
    @GetMapping("/statistics/{courseId}")
    public Result<Map<String, Object>> getStatistics(@PathVariable Long courseId,
                                                       @RequestParam String semester) {
        return Result.success(gradeInfoMapper.selectStatistics(courseId, semester));
    }

    @Operation(summary = "录入/更新成绩")
    @PostMapping
    public Result<Void> addGrade(@RequestBody GradeDTO dto) {
        upsertGrade(dto);
        return Result.success();
    }

    @Operation(summary = "批量录入成绩")
    @PostMapping("/batch")
    public Result<Void> batchAdd(@RequestBody List<GradeDTO> grades) {
        for (GradeDTO dto : grades) {
            if (dto.getScore() != null) {
                upsertGrade(dto);
            }
        }
        return Result.success();
    }

    /**
     * 成绩采用"存/取/改"唯一键(student_id, course_id, semester)对应的单条记录：
     * score 为空 -> 删除该记录；已存在 -> 更新；不存在 -> 新增。
     */
    private void upsertGrade(GradeDTO dto) {
        GradeInfo existing = gradeInfoMapper.selectOne(new LambdaQueryWrapper<GradeInfo>()
                .eq(GradeInfo::getStudentId, dto.getStudentId())
                .eq(GradeInfo::getCourseId, dto.getCourseId())
                .eq(GradeInfo::getSemester, dto.getSemester()));

        // 成绩为空则删除已有记录
        if (dto.getScore() == null) {
            if (existing != null) {
                gradeInfoMapper.deleteById(existing.getId());
            }
            return;
        }

        GradeInfo grade = new GradeInfo();
        grade.setStudentId(dto.getStudentId());
        grade.setCourseId(dto.getCourseId());
        grade.setScore(dto.getScore());
        grade.setGradePoint(dto.getGradePoint());
        grade.setExamType(dto.getExamType());
        grade.setSemester(dto.getSemester());
        grade.setRemark(dto.getRemark());

        if (existing != null) {
            grade.setId(existing.getId());
            gradeInfoMapper.updateById(grade);
        } else {
            gradeInfoMapper.insert(grade);
        }
    }
}
