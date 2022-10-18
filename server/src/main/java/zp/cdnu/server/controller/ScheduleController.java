package zp.cdnu.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import zp.cdnu.server.common.annotation.UseToken;
import zp.cdnu.server.common.result.ServerException;
import zp.cdnu.server.common.result.Status;
import zp.cdnu.server.config.RabbitConfig;
import zp.cdnu.server.entity.Job;
import zp.cdnu.server.entity.Mail;
import zp.cdnu.server.entity.Schedule;
import zp.cdnu.server.entity.User;
import zp.cdnu.server.service.IJobService;
import zp.cdnu.server.service.IScheduleService;
import zp.cdnu.server.service.IUserService;
import zp.cdnu.server.utils.AssertUtils;
import zp.cdnu.server.vo.ScheduleVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
 * @since 2022-09-07
 */
@CrossOrigin
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    @Resource
    private IScheduleService scheduleService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private AssertUtils assertUtils;
    @Resource
    private IUserService userService;
    @Resource
    private IJobService jobService;

    @UseToken
    @PostMapping("/send/resume")
    public void sendResume(@RequestBody Schedule schedule, HttpServletRequest request) {
        int uid = (int) request.getAttribute("uid");
        User user = userService.getById(uid);
        assertUtils.notNull(user.getResume(), new ServerException(Status.USER_ERROR).message("请先上传一份简历"));
        assertUtils.equals(uid, schedule.getBid(), new ServerException(Status.USER_ERROR));
        assertUtils.notEquals(uid, schedule.getAid(), new ServerException(Status.USER_ERROR));
        LambdaQueryWrapper<Schedule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Schedule::getBid, uid);
        lambdaQueryWrapper.eq(Schedule::getJid, schedule.getJid());
        Schedule one = scheduleService.getOne(lambdaQueryWrapper);
        assertUtils.isNull(one, new ServerException(Status.USER_ERROR).message("请勿重复投递简历"));
        schedule.setProgress(1);
        scheduleService.save(schedule);
        Mail mail = new Mail();
        User receiver = userService.getById(schedule.getAid());
        mail.setTaker(receiver.getEmail());
        mail.setTitle("你收到了一份简历投递");
        Map<String, Object> data = new HashMap<>();
        Job job = jobService.getById(schedule.getJid());
        data.put("resume", user.getResume());
        data.put("company", job.getCompany());
        data.put("title", job.getTitle());
        mail.setParams(data);
        rabbitTemplate.convertAndSend(
                RabbitConfig.MAIL_EXCHANGE_NAME,
                RabbitConfig.MAIL_RESUME_ROUTING_KEY_NAME,
                mail
        );

    }

    @UseToken
    @GetMapping("/get/list/uid/{id}")
    public List<ScheduleVo> listByUid(HttpServletRequest request, @PathVariable int id) {
        int uid = (int) request.getAttribute("uid");
        assertUtils.equals(uid, id, new ServerException(Status.USER_ERROR));
        LambdaQueryWrapper<Schedule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Schedule::getBid, uid);
        List<Schedule> list = scheduleService.list(lambdaQueryWrapper);
        List<ScheduleVo> result = new ArrayList<>();
        list.forEach(item -> {
            ScheduleVo scheduleVo = new ScheduleVo();
            BeanUtils.copyProperties(item, scheduleVo);
            Job job = jobService.getById(item.getJid());
            User user = userService.getById(item.getAid());
            scheduleVo.setJob(job);
            scheduleVo.setUser(user);
            result.add(scheduleVo);
        });
        return result;
    }

    @UseToken
    @PutMapping("/put/jid/{jid}/bid/{bid}/progress/{progress}")
    public void progress(HttpServletRequest request, @PathVariable int jid, @PathVariable int progress, @PathVariable int bid) {
        int aid = (int) request.getAttribute("uid");
        LambdaQueryWrapper<Schedule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Schedule::getAid, aid);
        lambdaQueryWrapper.eq(Schedule::getBid, bid);
        lambdaQueryWrapper.eq(Schedule::getProgress, 1);
        lambdaQueryWrapper.eq(Schedule::getJid, jid);
        Schedule one = scheduleService.getOne(lambdaQueryWrapper);
        assertUtils.notNull(one, new ServerException(Status.USER_ERROR));
        one.setProgress(progress);
        scheduleService.updateById(one);
        if (progress == 2) {
            Mail mail = new Mail();
            User receiver = userService.getById(bid);
            mail.setTaker(receiver.getEmail());
            mail.setTitle("简历通过筛选啦");
            Map<String, Object> data = new HashMap<>();
            Job job = jobService.getById(jid);
            data.put("company", job.getCompany());
            data.put("title", job.getTitle());
            mail.setParams(data);
            rabbitTemplate.convertAndSend(
                    RabbitConfig.MAIL_EXCHANGE_NAME,
                    RabbitConfig.MAIL_RESUME_PASS_ROUTING_KEY_NAME,
                    mail
            );
        }
    }

    @UseToken
    @GetMapping("/get/list/jid/{id}")
    public List<Map<String, Object>> listByJid(HttpServletRequest request, @PathVariable int id) {
        int uid = (int) request.getAttribute("uid");
        LambdaQueryWrapper<Schedule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Schedule::getAid, uid);
        lambdaQueryWrapper.eq(Schedule::getProgress, 1);
        lambdaQueryWrapper.eq(Schedule::getJid, id);
        List<Schedule> schedules = scheduleService.list(lambdaQueryWrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        schedules.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            User user = userService.getById(item.getBid());
            map.put("schedule", item);
            map.put("user", user);
            result.add(map);
        });
        return result;
    }
}

