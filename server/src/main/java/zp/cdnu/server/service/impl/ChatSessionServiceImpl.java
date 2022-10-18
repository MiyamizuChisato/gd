package zp.cdnu.server.service.impl;

import org.springframework.transaction.annotation.Transactional;
import zp.cdnu.server.entity.ChatSession;
import zp.cdnu.server.mapper.ChatSessionMapper;
import zp.cdnu.server.service.IChatSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author miya
 * @since 2022-09-09
 */
@Service
@Transactional
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements IChatSessionService {

}
