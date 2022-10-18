package zp.cdnu.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;

import zp.cdnu.server.common.annotation.UseToken;
import zp.cdnu.server.entity.ChatSession;
import zp.cdnu.server.entity.Message;
import zp.cdnu.server.service.IChatSessionService;
import zp.cdnu.server.service.IMessageService;

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
public class MessageController {

    @Resource
    private IMessageService messageService;

    @Resource
    private IChatSessionService chatSessionService;

    @UseToken
    @GetMapping("/get/unread/sender/{sender}")
    public List<Message> getUnreadMessage(HttpServletRequest request, @PathVariable int sender) {
        int uid = (int) request.getAttribute("uid");
        LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Message::getSender, sender);
        lambdaQueryWrapper.eq(Message::getReceiver, uid);
        List<Message> messages = messageService.list(lambdaQueryWrapper);
        messageService.removeBatchByIds(messages);
        LambdaQueryWrapper<ChatSession> chatSessionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatSessionLambdaQueryWrapper.eq(ChatSession::getReceiver, sender);
        chatSessionLambdaQueryWrapper.eq(ChatSession::getSender, uid);
        ChatSession one = chatSessionService.getOne(chatSessionLambdaQueryWrapper);
        one.setUnread(0);
        chatSessionService.updateById(one);
        return messages;
    }
}
