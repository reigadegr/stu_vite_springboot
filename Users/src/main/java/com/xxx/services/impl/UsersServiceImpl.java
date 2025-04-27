package com.xxx.services.impl;

import com.xxx.mapper.UsersMapper;
import com.xxx.pojo.Users;
import com.xxx.services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import com.xxx.MOM.Redis;
import com.xxx.pojo.special.Token;
import com.xxx.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersMapper usersMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Users> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);


    @Override
    public Result<?> usersLogin(Users request) {
        if (request == null) {
            return Result.error("请求体为空");
        }
        val username = request.getUsername();
        val password = request.getPassword();
        val tmp = usersMapper.usersLogin(username, password);
        if (tmp == null) {
            log.info("响应体为空，大概率是用户名或密码错误");
            return Result.error("用户名或密码错误");
        }
        val nowToken = new Token();
        nowToken.setToken(Token.mergeToken(tmp.getRole()));
        //把tmp.getRole()(用户类型)放到redis
        try {
            val redis = new Redis(stringRedisTemplate);
            redis.set("now_user_role", tmp.getRole());
            redis.set("now_user_name", tmp.getUsername());
        } catch (Exception e) {
            val err = Result.error("redis操作出现问题");
            log.error(String.valueOf(err));
            return err;
        }
        val rs = Result.success(nowToken, tmp.getRole() + "类型用户登录成功");
        log.info(rs.toString());
        return rs;
    }

    @Override
    public Result<?> insertUser(Users users) {
        val check = usersMapper.checkUsernameExists(users.getUsername());
        if (check != 0) {
            return Result.error("已有相同用户");
        }
        val username = users.getUsername();
        val password = users.getPassword();
        val type = users.getRole();
        String[] fields = {username, password, type};
        for (val field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return Result.error("必要参数为空, 操作失败");
            }
        }
        try {
            val rs = usersMapper.insertUser(users);
            val str = String.valueOf(rs);
            return Result.success(str, "操作成功");
        } catch (Exception e) {
            return Result.error("数据库操作出现问题: " + e);
        }
    }

    @Override
    public Result<?> getAllUsers(@RequestParam(defaultValue = "1") Integer currentPage,
                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("开始获取用户数据");
        int get_end_index = currentPage * size;
        int get_start_index = get_end_index - size + 1;

        //添加到redis
        val key = "usersList";
        //创建操作对象
        final ListOperations<String, Users> listOps = redisTemplate.opsForList();
        List<Users> redis_userList = listOps.range(key, 0, -1);
        assert redis_userList != null;
        if (redis_userList.isEmpty()) {
            System.out.println("用户服务：没数据，先从数据库拿-存redis");
            List<Users> UserList = usersMapper.getAllUsers();
            // 倒序排列
            Collections.reverse(UserList);
            listOps.leftPushAll(key, UserList);
            redis_userList = listOps.range(key, 0, -1);
        }
        System.out.println("redis有数据了");

        assert redis_userList != null;
        int total = redis_userList.size();
        if (get_end_index > total) {
            get_end_index = total;
        }
        List<Users> subList = redis_userList.subList(get_start_index - 1, get_end_index);

        //必须添加total，以及list键值对 否则前端无法显示数据到表格！
        Map<String, Object> data = new HashMap<>();
        data.put("list", subList);
        data.put("total", total);
        log.info("获取所有用户成功");
        return Result.success(data, "获取所有用户成功");
    }

    @Override
    public Result<?> updateUser(Users users) {
        log.info("接受到了修改请求");
        val rs = usersMapper.updateUser(users);
        return Result.success(rs, "修改成功");
    }

    @Override
    public Result<?> deleteUserById(Long id) {
        log.info("接受到删除请求");
        val rs = usersMapper.deleteUserById(id);
        return Result.success(rs, "删除成功");
    }

    @Override
    public Result<?> deleteUserBat(List<Integer> ids) {
        val rs = usersMapper.deleteUserBat(ids);
        return Result.success(rs, "批量删除成功");
    }
}
