package zp.cdnu.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import zp.cdnu.server.common.annotation.UseToken;
import zp.cdnu.server.common.result.ServerException;
import zp.cdnu.server.common.result.Status;
import zp.cdnu.server.config.CacheConfig;
import zp.cdnu.server.entity.Job;
import zp.cdnu.server.entity.Schedule;
import zp.cdnu.server.entity.User;
import zp.cdnu.server.service.IJobService;
import zp.cdnu.server.service.IScheduleService;
import zp.cdnu.server.service.IUserService;
import zp.cdnu.server.utils.AssertUtils;
import zp.cdnu.server.vo.JobVo;

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
 * @since 2022-09-05
 */
@CrossOrigin
@RestController
@RequestMapping("/api/job")
public class JobController {
    @Resource
    private IJobService jobService;
    @Resource
    private IUserService userService;

    @Resource
    private IScheduleService scheduleService;

    @Resource
    private AssertUtils assertUtils;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @UseToken
    @PostMapping("/publish")
    public int publish(@RequestBody Job job, HttpServletRequest request) {
        int uid = (int) request.getAttribute("uid");
        if (job.getUid() != null) {
            assertUtils.equals(job.getUid(), uid, new ServerException(Status.USER_ERROR));
        }
        job.setUid(uid);
        jobService.saveOrUpdate(job);
        redisTemplate.delete("GetJobById::GetJobById-" + job.getJid());
        return job.getJid();
    }

    @GetMapping("/get/id/{id}")
    @Cacheable(value = "GetJobById", key = "'GetJobById-'+#root.args[0]", cacheManager = "cacheManager5Min")
    public Map<String, Object> getById(@PathVariable int id) {
        Job job = jobService.getById(id);
        job.setHot(job.getHot() + 1);
        jobService.updateById(job);
        assertUtils.notNull(job, new ServerException(Status.PATH_ERROR));
        User user = userService.getById(job.getUid());
        Map<String, Object> map = new HashMap<>();
        map.put("job", job);
        map.put("user", user);
        return map;
    }

    @UseToken
    @GetMapping("/check/id/{jid}")
    public Job getById(HttpServletRequest request, @PathVariable int jid) {
        int uid = (int) request.getAttribute("uid");
        Job job = new Job();
        job.setJid(jid);
        job = jobService.getById(jid);
        assertUtils.equals(uid, job.getUid(), new ServerException(Status.USER_ERROR));
        return job;
    }

    @GetMapping("/get/list/uid/{uid}")
    public List<Job> getByList(@PathVariable int uid) {
        LambdaQueryWrapper<Job> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Job::getUid, uid);
        return jobService.list(lambdaQueryWrapper);
    }

    @UseToken
    @DeleteMapping("/delete/id/{jid}")
    public void deleteById(@PathVariable int jid, HttpServletRequest request) {
        int uid = (int) request.getAttribute("uid");
        Job job = jobService.getById(jid);
        assertUtils.equals(job.getUid(), uid, new ServerException(Status.USER_ERROR));
        jobService.removeById(jid);
        LambdaQueryWrapper<Schedule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Schedule::getJid, jid);
        scheduleService.remove(lambdaQueryWrapper);
    }

    @GetMapping("/get/page/{current}")
//    @Cacheable(value = "JobPage", key = "'JobPage-'+#root.args[0]")
    public IPage<JobVo> getByPage(@PathVariable int current) {
        IPage<Job> page = new Page<>(current, 12);
        LambdaQueryWrapper<Job> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Job::getCreateTime);
        jobService.page(page, lambdaQueryWrapper);
        List<Job> records = page.getRecords();
        List<JobVo> result = new ArrayList<>();
        records.forEach(record -> {
            User user = userService.getById(record.getUid());
            JobVo jobVo = new JobVo();
            BeanUtils.copyProperties(record, jobVo);
            jobVo.setUser(user);
            result.add(jobVo);
        });
        Page<JobVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        voPage.setRecords(result);
        return voPage;
    }

    @GetMapping("/search/{keyword}/{current}")
    public IPage<JobVo> getByKeyword(@PathVariable String keyword, @PathVariable int current) {
        return jobService.pageKeyword(keyword, current);
    }

    @GetMapping("/get/index")
    public List<JobVo> getIndex() {
        List<JobVo> jobVoList = jobService.getIndex();
        jobVoList.forEach(item -> {
            User user = userService.getById(item.getUid());
            item.setUser(user);
        });
        return jobVoList;
    }
}