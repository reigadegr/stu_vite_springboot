package com.xxx.controller.UsersController;

import com.xxx.MOM.Redis;
import com.xxx.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OnlyRedis {
    private final StringRedisTemplate stringRedisTemplate;
    private static final Logger log = LoggerFactory.getLogger(OnlyRedis.class);

    @RequestMapping(value = "/users/logout", method = RequestMethod.DELETE)
    public Result<?> usersLogout() {
        //把tmp.getRole()(用户类型)以及用户名从redis移除
        try {
            val redis = new Redis(stringRedisTemplate);
            redis.delete("now_user_role");
            redis.delete("now_user_name");
        } catch (Exception e) {
            val err = Result.error("Redis操作出现问题，清除用户缓存失败");
            log.error(err.toString());
            return err;
        }
        val rs = Result.success(null, "清除Redis缓存成功");
        log.info(rs.toString());
        return rs;
    }

    /*
     * 这一部分用来在登录后立即获取登录用户的角色类别，
     * 相应数据给前端
     * 需要开启redis辅助实现
     */
    @RequestMapping(value = "/users/info", method = RequestMethod.GET)
    public Result<?> userInfo() {
        // 必须在timeout限制之内完成
        //admin  || editor 用户的类型，用来控制哪个后台按钮显示。从redis获取
        //也从redis中获取当前登录的用户名
        try {
            val redis = new Redis(stringRedisTemplate);
            val roles = redis.get("now_user_role");
            val username = redis.get("now_user_name");
            if (username == null || roles == null) {
                return Result.error("无法正常获取用户角色或者用户名");
            }
            Map<String, Object> rs_data = new HashMap<>();
            rs_data.put("roles", new String[]{roles});
            //这里的用户名，从redis获取，以便控制菜单按钮权限
            rs_data.put("username", username);
            val rs = Result.success(rs_data, "获取" + roles + "类型用户详情成功");
            log.info(rs.toString());
            return rs;
        } catch (Exception e) {
            val err = Result.error("Redis操作出现问题，获取用户类型失败");
            log.error(err.toString());
            return err;
        }
    }

    @RequestMapping(value = "/users/change", method = RequestMethod.POST)
    public Result<?> userChange(@RequestBody String new_role) {
        if (new_role == null) {
            return Result.error("请求体为空");
        }
        log.info("热切换角色: {}", new_role);
        val new_role2 = new_role.replaceAll("\"", "");
        Map<String, Object> rs_data = new HashMap<>();
        try {
            val redis = new Redis(stringRedisTemplate);
            redis.set("now_user_role", new_role2);
            rs_data.put("roles", new String[]{new_role2});
            //这里的username从redis获取
            val username = redis.get("now_user_name");
            rs_data.put("username", username);
            return Result.success(rs_data, "热切换用户角色成功");
        } catch (Exception e) {
            return Result.error("热切换用户角色失败: " + e);
        }
    }
}
