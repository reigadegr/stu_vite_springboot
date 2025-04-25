package com.xxx.pojo.special;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private String token;
    public static String mergeToken(String token) {
        return "token-" + token;
    }
}
