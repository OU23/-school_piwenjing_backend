package com.school.management.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.management.common.exception.BusinessException;
import com.school.management.common.result.PageResult;
import com.school.management.dto.CourseDTO;
import com.school.management.dto.CourseQuery;
import com.school.management.entity.CourseInfo;
import com.school.management.entity.CourseTeacher;
import com.school.management.mapper.CourseInfoMapper;
import com.school.management.mapper.CourseTeacherMapper;
import com.school.management.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseInfoMapper courseInfoMapper;
    private final CourseTeacherMapper courseTeacherMapper;

    @Override
    public PageResult<CourseDTO> listCourses(CourseQuery query) {
        LambdaQueryWrapper<CourseInfo> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(CourseInfo::getCourseName, query.getKeyword())
                    .or().like(CourseInfo::getCourseCode, query.getKeyword()));
        }
        if (StrUtil.isNotBlank(query.getCourseType())) {
            wrapper.eq(CourseInfo::getCourseType, query.getCourseType());
        }
        if (query.getDeptId() != null) {
            wrapper.eq(CourseInfo::getDeptId, query.getDeptId());
        }

        wrapper.orderByDesc(CourseInfo::getCreatedAt);

        Page<CourseInfo> page = new Page<>(query.getPageNum() != null ? query.getPageNum() : 1,
                query.getPageSize() != null ? query.getPageSize() : 20);
        Page<CourseInfo> resultPage = courseInfoMapper.selectPage(page, wrapper);

        List<CourseDTO> list = resultPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(list, resultPage.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }

    @Override
    public CourseDTO getCourseDetail(Long id) {
        CourseInfo course = courseInfoMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.COURSE_NOT_FOUND);
        }
        return convertToDTO(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCourse(CourseDTO dto) {
        long count = courseInfoMapper.selectCount(new LambdaQueryWrapper<CourseInfo>()
                .eq(CourseInfo::getCourseCode, dto.getCourseCode()));
        if (count > 0) {
            throw new BusinessException("课程编码已存在");
        }

        CourseInfo course = new CourseInfo();
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setCourseType(dto.getCourseType() != null ? dto.getCourseType() : "必修");
        course.setCredits(dto.getCredits());
        course.setHours(dto.getHours());
        course.setDeptId(dto.getDeptId());
        course.setDescription(dto.getDescription());
        course.setStatus(1);

        courseInfoMapper.insert(course);

        // 分配教师
        if (dto.getTeacherIds() != null && !dto.getTeacherIds().isEmpty()) {
            for (int i = 0; i < dto.getTeacherIds().size(); i++) {
                CourseTeacher ct = new CourseTeacher();
                ct.setCourseId(course.getId());
                ct.setTeacherId(dto.getTeacherIds().get(i));
                ct.setIsPrimary(i == 0 ? 1 : 0);
                courseTeacherMapper.insert(ct);
            }
        }

        log.info("添加课程成功: {}", dto.getCourseCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCourse(CourseDTO dto) {
        CourseInfo course = courseInfoMapper.selectById(dto.getId());
        if (course == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.COURSE_NOT_FOUND);
        }

        course.setCourseName(dto.getCourseName());
        course.setCourseType(dto.getCourseType());
        course.setCredits(dto.getCredits());
        course.setHours(dto.getHours());
        course.setDeptId(dto.getDeptId());
        course.setDescription(dto.getDescription());
        course.setStatus(dto.getStatus());

        courseInfoMapper.updateById(course);
        log.info("更新课程成功: {}", dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourse(Long id) {
        CourseInfo course = courseInfoMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.COURSE_NOT_FOUND);
        }

        courseInfoMapper.deleteById(id);
        courseTeacherMapper.delete(new LambdaQueryWrapper<CourseTeacher>().eq(CourseTeacher::getCourseId, id));
        log.info("删除课程成功: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTeacher(Long courseId, Long teacherId, boolean isPrimary) {
        courseTeacherMapper.delete(new LambdaQueryWrapper<CourseTeacher>()
                .eq(CourseTeacher::getCourseId, courseId)
                .eq(CourseTeacher::getTeacherId, teacherId));

        CourseTeacher ct = new CourseTeacher();
        ct.setCourseId(courseId);
        ct.setTeacherId(teacherId);
        ct.setIsPrimary(isPrimary ? 1 : 0);
        courseTeacherMapper.insert(ct);
        log.info("分配教师成功: courseId={}, teacherId={}", courseId, teacherId);
    }

    private CourseDTO convertToDTO(CourseInfo course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCourseCode(course.getCourseCode());
        dto.setCourseName(course.getCourseName());
        dto.setCourseType(course.getCourseType());
        dto.setCredits(course.getCredits());
        dto.setHours(course.getHours());
        dto.setDeptId(course.getDeptId());
        dto.setDescription(course.getDescription());
        dto.setStatus(course.getStatus());

        List<CourseTeacher> teachers = courseTeacherMapper.selectList(
                new LambdaQueryWrapper<CourseTeacher>().eq(CourseTeacher::getCourseId, course.getId()));
        dto.setTeacherIds(teachers.stream().map(CourseTeacher::getTeacherId).collect(Collectors.toList()));

        return dto;
    }
}
