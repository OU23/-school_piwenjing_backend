package com.school.management.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.management.common.exception.BusinessException;
import com.school.management.common.result.PageResult;
import com.school.management.dto.TeacherDTO;
import com.school.management.dto.TeacherQuery;
import com.school.management.entity.SysUser;
import com.school.management.entity.TeacherInfo;
import com.school.management.enums.UserStatusEnum;
import com.school.management.mapper.SysUserMapper;
import com.school.management.mapper.TeacherInfoMapper;
import com.school.management.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 教师服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherInfoMapper teacherInfoMapper;
    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<TeacherDTO> listTeachers(TeacherQuery query) {
        LambdaQueryWrapper<TeacherInfo> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(TeacherInfo::getName, query.getKeyword())
                    .or().like(TeacherInfo::getTeacherNo, query.getKeyword()));
        }
        if (query.getDeptId() != null) {
            wrapper.eq(TeacherInfo::getDeptId, query.getDeptId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(TeacherInfo::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(TeacherInfo::getCreatedAt);

        Page<TeacherInfo> page = new Page<>(query.getPageNum() != null ? query.getPageNum() : 1,
                query.getPageSize() != null ? query.getPageSize() : 20);
        Page<TeacherInfo> resultPage = teacherInfoMapper.selectPage(page, wrapper);

        List<TeacherDTO> list = resultPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(list, resultPage.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }

    @Override
    public TeacherDTO getTeacherDetail(Long id) {
        TeacherInfo teacher = teacherInfoMapper.selectById(id);
        if (teacher == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.TEACHER_NOT_FOUND);
        }
        return convertToDTO(teacher);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTeacher(TeacherDTO dto) {
        long count = teacherInfoMapper.selectCount(new LambdaQueryWrapper<TeacherInfo>()
                .eq(TeacherInfo::getTeacherNo, dto.getTeacherNo()));
        if (count > 0) {
            throw new BusinessException("教师编号已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getTeacherNo());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRealName(dto.getName());
        user.setGender(dto.getGender());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRoleType("teacher");
        user.setDeptId(dto.getDeptId());
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        sysUserMapper.insert(user);

        TeacherInfo teacher = new TeacherInfo();
        teacher.setTeacherNo(dto.getTeacherNo());
        teacher.setUserId(user.getId());
        teacher.setName(dto.getName());
        teacher.setGender(dto.getGender());
        teacher.setPhone(dto.getPhone());
        teacher.setEmail(dto.getEmail());
        teacher.setDeptId(dto.getDeptId());
        teacher.setTitle(dto.getTitle());
        teacher.setEducation(dto.getEducation());
        teacher.setHireDate(dto.getHireDate());
        teacher.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        teacherInfoMapper.insert(teacher);
        log.info("添加教师成功: {}", dto.getTeacherNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTeacher(TeacherDTO dto) {
        TeacherInfo teacher = teacherInfoMapper.selectById(dto.getId());
        if (teacher == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.TEACHER_NOT_FOUND);
        }

        teacher.setName(dto.getName());
        teacher.setGender(dto.getGender());
        teacher.setPhone(dto.getPhone());
        teacher.setEmail(dto.getEmail());
        teacher.setDeptId(dto.getDeptId());
        teacher.setTitle(dto.getTitle());
        teacher.setEducation(dto.getEducation());
        teacher.setHireDate(dto.getHireDate());
        teacher.setStatus(dto.getStatus());

        teacherInfoMapper.updateById(teacher);

        SysUser user = sysUserMapper.selectById(teacher.getUserId());
        if (user != null) {
            user.setRealName(dto.getName());
            user.setPhone(dto.getPhone());
            user.setEmail(dto.getEmail());
            sysUserMapper.updateById(user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTeacher(Long id) {
        TeacherInfo teacher = teacherInfoMapper.selectById(id);
        if (teacher == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.TEACHER_NOT_FOUND);
        }

        teacherInfoMapper.deleteById(id);
        sysUserMapper.deleteById(teacher.getUserId());
        log.info("删除教师成功: {}", id);
    }

    private TeacherDTO convertToDTO(TeacherInfo teacher) {
        TeacherDTO dto = new TeacherDTO();
        dto.setId(teacher.getId());
        dto.setTeacherNo(teacher.getTeacherNo());
        dto.setUserId(teacher.getUserId());
        dto.setName(teacher.getName());
        dto.setGender(teacher.getGender());
        dto.setPhone(teacher.getPhone());
        dto.setEmail(teacher.getEmail());
        dto.setDeptId(teacher.getDeptId());
        dto.setTitle(teacher.getTitle());
        dto.setEducation(teacher.getEducation());
        dto.setHireDate(teacher.getHireDate());
        dto.setStatus(teacher.getStatus());
        return dto;
    }
}
