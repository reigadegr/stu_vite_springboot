package com.xxx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private Integer id;
    private String role;
    private String username;
    private String password;
    // 创建时间，使用Timestamp类型以包含时区信息
    private String create_time;
}
