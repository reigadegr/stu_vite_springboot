package com.xxx.services;

import com.xxx.pojo.Users;
import com.xxx.result.Result;

import java.util.List;

public interface UsersService {
    Result<?> usersLogin(Users request);

    Result<?> insertUser(Users users);

    Result<?> getAllUsers(Integer currentPage, Integer size);

    Result<?> updateUser(Users users);

    Result<?> deleteUserById(Long id);

    Result<?> deleteUserBat(List<Integer> ids);
}
