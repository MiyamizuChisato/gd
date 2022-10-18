package zp.cdnu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import zp.cdnu.server.entity.Job;
import zp.cdnu.server.entity.User;
import zp.cdnu.server.mapper.JobMapper;
import zp.cdnu.server.service.IJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zp.cdnu.server.service.IUserService;
import zp.cdnu.server.vo.JobVo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author miya
 * @since 2022-09-05
 */
@Service
@Transactional
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {
    @Resource
    private IUserService userService;

    @Override
    public IPage<JobVo> pageKeyword(String keyword, int current) {
        IPage<Job> page = new Page<>(current, 12);
        LambdaQueryWrapper<Job> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Job::getTitle, keyword);
        lambdaQueryWrapper.or();
        lambdaQueryWrapper.like(Job::getCompany, keyword);
        lambdaQueryWrapper.or();
        lambdaQueryWrapper.like(Job::getContent, keyword);
        page = baseMapper.selectPage(page, lambdaQueryWrapper);
        List<JobVo> result = new ArrayList<>();
        page.getRecords().forEach(record -> {
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

    @Override
    public List<JobVo> getIndex() {
       return baseMapper.getIndex();
    }
}
