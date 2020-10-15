package com.example.webfux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@AllArgsConstructor // 生成所有参数构造方法
@NoArgsConstructor
@Document("user")
public class User {
    @Id

    private String id;
    //@Indexed(unique = true)
    private String username;
    private String orgId;
    private String platformId;
    private String robotId;
    private String requestTime;
}