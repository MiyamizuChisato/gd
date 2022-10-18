package zp.cdnu.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import zp.cdnu.server.common.annotation.UseToken;
import zp.cdnu.server.common.annotation.validate.Login;
import zp.cdnu.server.common.annotation.validate.Register;
import zp.cdnu.server.common.result.ServerException;
import zp.cdnu.server.common.result.Status;
import zp.cdnu.server.entity.ChatSession;
import zp.cdnu.server.entity.User;
import zp.cdnu.server.service.IChatSessionService;
import zp.cdnu.server.service.IUserService;
import zp.cdnu.server.utils.AssertUtils;
import zp.cdnu.server.vo.UserVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author miya
 * @since 2022-09-02
 */
@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private IUserService userService;
    @Resource
    private AssertUtils assertUtils;

    @Resource
    private IChatSessionService chatSessionService;

    @PostMapping("/register")
    public String register(@RequestBody @Validated(Register.class) UserVo userVo) {
        String key = "verify::" + userVo.getEmail();
        String code = (String) redisTemplate.opsForValue().get(key);
        assertUtils.notNull(code, new ServerException(Status.USER_ERROR).message("无验证信息"));
        assertUtils.equals(code, userVo.getCode(), new ServerException(Status.USER_ERROR).message("验证码错误"));
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        String token = userService.register(user);
        redisTemplate.delete(key);
        return token;
    }

    @PostMapping("/login")
    public String login(@RequestBody @Validated(Login.class) UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        return userService.login(user);
    }

    @UseToken
    @GetMapping("/get/user")
    public UserVo getUserByUid(HttpServletRequest request) {
        int uid = (int) request.getAttribute("uid");
        User user = userService.getById(uid);
        user.setPassword(null);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @GetMapping("/watch/uid/{uid}")
    public UserVo watchUserByUid(@PathVariable int uid) {
        User user = userService.getById(uid);
        user.setPassword(null);
        user.setResume(null);
        user.setEmail("非本人不予查看");
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @UseToken
    @PutMapping("/put/user")
    public String updateUser(@RequestBody @Validated UserVo userVo, HttpServletRequest request) {
        int uid = (int) request.getAttribute("uid");
        assertUtils.equals(uid, userVo.getUid(), new ServerException(Status.USER_ERROR));
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        return userService.update(user);
    }

    @UseToken
    @PutMapping("/put/avatar")
    public String updateUser(@RequestParam("avatar") MultipartFile avatar, HttpServletRequest request) throws IOException {
        int uid = (int) request.getAttribute("uid");
        User user = new User();
        user.setUid(uid);
        return userService.updateAvatar(user, avatar);
    }

    @UseToken
    @PutMapping("/put/resume")
    public String updateResumeByToken(@RequestParam("resume") MultipartFile resume, HttpServletRequest request) throws IOException {
        int uid = (int) request.getAttribute("uid");
        User user = new User();
        user.setUid(uid);
        return userService.updateResume(user, resume);
    }


    @UseToken
    @GetMapping("/get/receivers")
    public List<Map<String, Object>> getChatList(HttpServletRequest request) {
        int uid = (int) request.getAttribute("uid");
        LambdaQueryWrapper<ChatSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ChatSession::getSender, uid);
        List<ChatSession> sessions = chatSessionService.list(lambdaQueryWrapper);
        List<Map<String, Object>> list = new ArrayList<>();
        sessions.forEach(session -> {
            User user = userService.getById(session.getReceiver());
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("session", session);
            list.add(map);
        });
        return list;
    }
}
