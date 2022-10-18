package zp.cdnu.server.controller;

import cn.hutool.captcha.generator.RandomGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import zp.cdnu.server.common.result.ServerException;
import zp.cdnu.server.common.result.Status;
import zp.cdnu.server.config.RabbitConfig;
import zp.cdnu.server.entity.Mail;
import zp.cdnu.server.entity.User;
import zp.cdnu.server.service.IUserService;
import zp.cdnu.server.utils.AssertUtils;
import zp.cdnu.server.utils.UserUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
@RequestMapping("/api/mail")
public class MailController {
    @Resource
    private AssertUtils assertUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private IUserService userService;

    @GetMapping("/verify/{email}")
    public String verifyEmail(@PathVariable String email) {
        assertUtils.isTrue(UserUtils.emailValidate(email), new ServerException(Status.USER_ERROR));
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        assertUtils.isNull(userService.getOne(queryWrapper),
                new ServerException(Status.USER_ERROR).message("邮箱已被注册"));
        String code = new RandomGenerator("0123456789", 6).generate();
        redisTemplate.opsForValue().set("verify::" + email, code, 30, TimeUnit.MINUTES);
        Mail mail = new Mail();
        mail.setTaker(email);
        mail.setTitle("新用户注册");
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        mail.setParams(data);
        rabbitTemplate.convertAndSend(
                RabbitConfig.MAIL_EXCHANGE_NAME,
                RabbitConfig.MAIL_VERIFY_ROUTING_KEY_NAME,
                mail
        );
        return "验证码发送成功，请检查你的收件箱";
    }
}
