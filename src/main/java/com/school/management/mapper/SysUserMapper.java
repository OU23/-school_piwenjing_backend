package com.school.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.management.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT u.*, r.role_code FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.id = ur.user_id " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id " +
            "WHERE u.username = #{username}")
    SysUser selectUserWithRole(@Param("username") String username);

    @Select("SELECT DISTINCT r.role_code FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    @Select("SELECT DISTINCT p.perm_code FROM sys_permission p " +
            "LEFT JOIN sys_role_permission rp ON p.perm_code = rp.perm_code " +
            "LEFT JOIN sys_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}
