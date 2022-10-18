package zp.cdnu.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;

import zp.cdnu.server.common.annotation.UseToken;
import zp.cdnu.server.common.result.ServerException;
import zp.cdnu.server.common.result.Status;
import zp.cdnu.server.entity.ChatSession;
import zp.cdnu.server.service.IChatSessionService;
import zp.cdnu.server.utils.AssertUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author miya
 * @since 2022-09-09
 */
@CrossOrigin
@RestController
@RequestMapping("/api/message")
public class ChatSessionController {
    @Resource
    private IChatSessionService chatSessionService;
    @Resource
    private AssertUtils assertUtils;

    @UseToken
    @PostMapping("/validate/sender/{sender}/receiver/{receiver}")
    public void validateSession(HttpServletRequest request, @PathVariable int receiver, @PathVariable int sender) {
        int uid = (int) request.getAttribute("uid");
        assertUtils.equals(uid, sender, new ServerException(Status.USER_ERROR));
        LambdaQueryWrapper<ChatSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ChatSession::getSender, sender);
        lambdaQueryWrapper.eq(ChatSession::getReceiver, receiver);
        ChatSession one = chatSessionService.getOne(lambdaQueryWrapper);
        if (null == one) {
            ChatSession send = new ChatSession();
            send.setSender(sender);
            send.setReceiver(receiver);
            ChatSession receive = new ChatSession();
            receive.setSender(receiver);
            receive.setReceiver(sender);
            chatSessionService.save(send);
            chatSessionService.save(receive);
        }
    }


}
