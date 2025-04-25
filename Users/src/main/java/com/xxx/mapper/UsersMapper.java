package com.xxx.mapper;

import com.xxx.pojo.Users;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UsersMapper {
    @Select("select * from users where username = #{username} AND password = #{password}")
    Users usersLogin(String username, String password);

    @Insert("INSERT INTO users(username, password, type, phone, email, status) VALUES(#{username}, #{password}, #{type},#{phone},#{email},#{status})")
    int insertUser(Users users);

    // 检查用户名是否存在
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int checkUsernameExists(String username);

    @Select("SELECT * FROM users")
    List<Users> getAllUsers();

    @Update("UPDATE users " +
            "SET username = #{username}, " +
            "password = #{password}, " +
            "type = #{type}, " +
            "phone = #{phone}, " +
            "email = #{email}, " +
            "status = #{status} " +
            "WHERE id = #{id}")
    int updateUser(Users users);

    @Delete("DELETE FROM users WHERE id = #{id}")
    Long deleteUserById(Long id);

    @Delete({"<script>", "DELETE FROM users WHERE id IN",
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>", "</script>"})
    Long deleteUserBat(List<Integer> ids);
}
