package com.school.management.dto;

import lombok.Data;

/**
 * 登录响应VO
 */
@Data
public class LoginVO {
    private Long id;
    private String username;
    private String realName;
    private String roleType;
    private String avatar;
    private String token;
    private java.util.List<String> permissions;
}
