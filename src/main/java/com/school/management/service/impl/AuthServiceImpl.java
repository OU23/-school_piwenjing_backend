package com.school.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.school.management.common.exception.BusinessException;
import com.school.management.dto.LoginDTO;
import com.school.management.dto.LoginVO;
import com.school.management.entity.SysUser;
import com.school.management.enums.UserStatusEnum;
import com.school.management.mapper.SysUserMapper;
import com.school.management.security.JwtUtil;
import com.school.management.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查询用户
        SysUser user = sysUserMapper.selectOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginDTO.getUsername()));

        if (user == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.USER_NOT_FOUND);
        }

        // 检查状态
        if (UserStatusEnum.DISABLED.getCode().equals(user.getStatus())) {
            throw new BusinessException(com.school.management.common.result.ResultCode.USER_DISABLED);
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(com.school.management.common.result.ResultCode.PASSWORD_ERROR);
        }

        // 获取角色和权限
        List<String> roles = sysUserMapper.selectRolesByUserId(user.getId());
        List<String> permissions = sysUserMapper.selectPermissionsByUserId(user.getId());

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRoleType());

        // 构建响应
        LoginVO vo = new LoginVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRoleType(user.getRoleType());
        vo.setAvatar(user.getAvatar());
        vo.setToken(token);
        vo.setPermissions(permissions);

        return vo;
    }

    @Override
    public void logout() {
        // JWT无状态，客户端自行删除Token即可
        log.info("用户登出");
    }

    @Override
    public LoginVO getCurrentUser() {
        // 从SecurityContext获取
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        SysUser user = sysUserMapper.selectOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));

        if (user == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.USER_NOT_FOUND);
        }

        LoginVO vo = new LoginVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRoleType(user.getRoleType());
        vo.setAvatar(user.getAvatar());

        List<String> permissions = sysUserMapper.selectPermissionsByUserId(user.getId());
        vo.setPermissions(permissions);

        return vo;
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(com.school.management.common.result.ResultCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(com.school.management.common.result.ResultCode.PASSWORD_ERROR);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        sysUserMapper.updateById(user);
        log.info("用户 {} 修改密码成功", userId);
    }
}
