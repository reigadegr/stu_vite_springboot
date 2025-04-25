package com.xxx.controller.UsersController;

import com.xxx.mapper.UsersMapper;
import com.xxx.pojo.Users;
import com.xxx.result.Result;
import com.xxx.services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JmeterTest {
    private final UsersService usersService;
    private final UsersMapper usersMapper;

    @RequestMapping(value = "/users/login/{admin_id}/{password}", method = RequestMethod.GET)
    public Result<?> verifyLoginInfo(@PathVariable String admin_id, @PathVariable String password) {
        log.info("登录接口被压测!");
        Users data = new Users();
        data.setUsername(admin_id);
        data.setPassword(password);
        val rs = usersService.usersLogin(data);
        if (rs == null) {
            return Result.error("用户名或密码错误");
        }
        return Result.success(rs, "登录成功");
    }

    @RequestMapping(value = "/users/getall/jmt", method = RequestMethod.GET)
    public Result<?> getAllUsers(@RequestParam(defaultValue = "1") Integer currentPage,
                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("开始获取用户数据");
        int get_end_index = currentPage * size;
        val get_start_index = get_end_index - size + 1;
        List<Users> userList = usersMapper.getAllUsers();
        int total = userList.size();
        if (get_end_index > total) {
            get_end_index = total;
        }
        List<Users> subList = userList.subList(get_start_index - 1, get_end_index);
        Map<String, Object> data = new HashMap<>();
        data.put("list", subList);
        data.put("total", total);
        log.info("获取所有用户成功");
        return Result.success(data, "获取所有用户成功");
    }
}
