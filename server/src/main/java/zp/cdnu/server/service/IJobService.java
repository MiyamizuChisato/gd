package zp.cdnu.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import zp.cdnu.server.entity.Job;
import com.baomidou.mybatisplus.extension.service.IService;
import zp.cdnu.server.vo.JobVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author miya
 * @since 2022-09-05
 */
public interface IJobService extends IService<Job> {

    IPage<JobVo> pageKeyword(String keyword, int current);

    List<JobVo> getIndex();
}
