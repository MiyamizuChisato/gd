package zp.cdnu.server.service.impl;

import org.springframework.transaction.annotation.Transactional;
import zp.cdnu.server.entity.Schedule;
import zp.cdnu.server.mapper.ScheduleMapper;
import zp.cdnu.server.service.IScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author miya
 * @since 2022-09-07
 */
@Service
@Transactional
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements IScheduleService {

}
