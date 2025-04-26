package com.xxx.controller.UsersController;

import com.xxx.pojo.Users;
import com.xxx.result.Result;
import com.xxx.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VisitMySQL {
    private final UsersService usersService;

    @RequestMapping(value = "/users/getall", method = RequestMethod.GET)
    public Result<?> getAllUsers(@RequestParam(defaultValue = "1") Integer currentPage,
                                 @RequestParam(defaultValue = "10") Integer size) {
        return usersService.getAllUsers(currentPage, size);
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public Result<?> usersLogin(@RequestBody Users request) {
        return usersService.usersLogin(request);
    }

    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    public Result<?> insertUser(@RequestBody Users users) {
        return usersService.insertUser(users);
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.PUT)
    public Result<?> updateUser(@RequestBody Users users) {
        return usersService.updateUser(users);
    }

    @RequestMapping(value = "/users/rm/{id}", method = RequestMethod.DELETE)
    public Result<?> deleteUserById(@PathVariable Long id) {
        return usersService.deleteUserById(id);
    }

    @RequestMapping(value = "/users/rmbatch", method = RequestMethod.POST)
    public Result<?> deleteUserBat(@RequestBody List<Integer> ids) {
        return usersService.deleteUserBat(ids);
    }
}
