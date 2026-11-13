package com.school.management.service;

import com.school.management.dto.LoginDTO;
import com.school.management.dto.LoginVO;

/**
 * 认证服务接口
 */
public interface AuthService {
    /**
     * 用户登录
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 获取当前用户信息
     */
    LoginVO getCurrentUser();

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
}
