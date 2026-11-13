package com.school.management.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.management.common.exception.BusinessException;
import com.school.management.common.result.PageResult;
import com.school.management.dto.StudentDTO;
import com.school.management.dto.StudentQuery;
import com.school.management.entity.StudentInfo;
import com.school.management.entity.SysUser;
import com.school.management.enums.UserStatusEnum;
import com.school.management.mapper.StudentInfoMapper;
import com.school.management.mapper.SysUserMapper;
import com.school.management.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentInfoMapper studentInfoMapper;
    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<StudentDTO> listStudents(StudentQuery query) {
        LambdaQueryWrapper<StudentInfo> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(StudentInfo::getName, query.getKeyword())
                    .or().like(StudentInfo::getStudentNo, query.getKeyword()));
        }
        if (query.getClassId() != null) {
            wrapper.eq(StudentInfo::getClassId, query.getClassId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(StudentInfo::getStatus, query.getStatus());
        }
        if (query.getGender() != null) {
            wrapper.eq(StudentInfo::getGender, query.getGender());
        }

        wrapper.orderByDesc(StudentInfo::getCreatedAt);

        Page<StudentInfo> page = new Page<>(query.getPageNum() != null ? query.getPageNum() : 1,
                query.getPageSize() != null ? query.getPageSize() : 20);
        Page<StudentInfo> resultPage = studentInfoMapper.selectPage(page, wrapper);

        List<StudentDTO> list = resultPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(list, resultPage.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }

    @Override
    public StudentDTO getStudentDetail(Long id) {
        StudentInfo student = studentInfoMapper.selectById(id);
        if (student == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.STUDENT_NOT_FOUND);
        }
        return convertToDTO(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudent(StudentDTO dto) {
        long count = studentInfoMapper.selectCount(new LambdaQueryWrapper<StudentInfo>()
                .eq(StudentInfo::getStudentNo, dto.getStudentNo()));
        if (count > 0) {
            throw new BusinessException("学号已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getStudentNo());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRealName(dto.getName());
        user.setGender(dto.getGender());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRoleType("student");
        user.setClassId(dto.getClassId());
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        sysUserMapper.insert(user);

        StudentInfo student = new StudentInfo();
        student.setStudentNo(dto.getStudentNo());
        student.setUserId(user.getId());
        student.setName(dto.getName());
        student.setGender(dto.getGender());
        student.setIdCard(dto.getIdCard());
        student.setBirthDate(dto.getBirthDate());
        student.setPhone(dto.getPhone());
        student.setEmail(dto.getEmail());
        student.setClassId(dto.getClassId());
        student.setEntranceYear(dto.getEntranceYear());
        student.setAddress(dto.getAddress());
        student.setParentName(dto.getParentName());
        student.setParentPhone(dto.getParentPhone());
        student.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        studentInfoMapper.insert(student);
        log.info("添加学生成功: {}", dto.getStudentNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStudent(StudentDTO dto) {
        StudentInfo student = studentInfoMapper.selectById(dto.getId());
        if (student == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.STUDENT_NOT_FOUND);
        }

        student.setName(dto.getName());
        student.setGender(dto.getGender());
        student.setIdCard(dto.getIdCard());
        student.setBirthDate(dto.getBirthDate());
        student.setPhone(dto.getPhone());
        student.setEmail(dto.getEmail());
        student.setClassId(dto.getClassId());
        student.setAddress(dto.getAddress());
        student.setParentName(dto.getParentName());
        student.setParentPhone(dto.getParentPhone());
        student.setStatus(dto.getStatus());

        studentInfoMapper.updateById(student);

        SysUser user = sysUserMapper.selectById(student.getUserId());
        if (user != null) {
            user.setRealName(dto.getName());
            user.setPhone(dto.getPhone());
            user.setEmail(dto.getEmail());
            sysUserMapper.updateById(user);
        }
        log.info("更新学生信息成功: {}", dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudent(Long id) {
        StudentInfo student = studentInfoMapper.selectById(id);
        if (student == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.STUDENT_NOT_FOUND);
        }

        studentInfoMapper.deleteById(id);
        sysUserMapper.deleteById(student.getUserId());
        log.info("删除学生成功: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchImportStudents(List<StudentDTO> students) {
        for (StudentDTO dto : students) {
            try {
                addStudent(dto);
            } catch (Exception e) {
                log.warn("导入学生失败: {}", dto.getStudentNo(), e);
            }
        }
        log.info("批量导入学生完成，共{}条", students.size());
    }

    private StudentDTO convertToDTO(StudentInfo student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setStudentNo(student.getStudentNo());
        dto.setUserId(student.getUserId());
        dto.setName(student.getName());
        dto.setGender(student.getGender());
        dto.setIdCard(student.getIdCard());
        dto.setBirthDate(student.getBirthDate());
        dto.setPhone(student.getPhone());
        dto.setEmail(student.getEmail());
        dto.setClassId(student.getClassId());
        dto.setEntranceYear(student.getEntranceYear());
        dto.setAddress(student.getAddress());
        dto.setParentName(student.getParentName());
        dto.setParentPhone(student.getParentPhone());
        dto.setStatus(student.getStatus());
        return dto;
    }
}
