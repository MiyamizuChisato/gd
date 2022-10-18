package zp.cdnu.server.mapper;

import org.apache.ibatis.annotations.Select;
import zp.cdnu.server.entity.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import zp.cdnu.server.vo.JobVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author miya
 * @since 2022-09-05
 */
public interface JobMapper extends BaseMapper<Job> {
    @Select("SELECT  * FROM JOB ORDER BY RAND() LIMIT 9")
    List<JobVo> getIndex();
}
