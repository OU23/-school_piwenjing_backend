package com.school.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.school.management.mapper")
public class SchoolManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolManagementApplication.class, args);
        System.out.println("========================================");
        System.out.println("   学校管理系统启动成功！");
        System.out.println("   API文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("========================================");
    }
}
