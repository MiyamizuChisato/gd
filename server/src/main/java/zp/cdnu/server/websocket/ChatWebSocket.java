package zp.cdnu.server.websocket;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zp.cdnu.server.entity.ChatSession;
import zp.cdnu.server.entity.Message;
import zp.cdnu.server.service.IChatSessionService;
import zp.cdnu.server.service.IMessageService;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/sender/{sender}/receiver/{receiver}")
public class ChatWebSocket {
    private static Map<String, ChatWebSocket> onlineUsers = new ConcurrentHashMap<>();
    private Session session;

    private static ObjectMapper objectMapper;

    private static IChatSessionService sessionService;

    private static IMessageService messageService;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        ChatWebSocket.objectMapper = objectMapper;
    }

    @Autowired
    public void setMessageService(IMessageService messageService) {
        ChatWebSocket.messageService = messageService;
    }

    @Autowired
    public void setSessionService(IChatSessionService sessionService) {
        ChatWebSocket.sessionService = sessionService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "sender") int sender, @PathParam(value = "receiver") int receiver) throws IOException {
        this.session = session;
        onlineUsers.put(sender + "-" + receiver, this);
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        Message msg = objectMapper.readValue(message, Message.class);
        ChatWebSocket sendSocket = onlineUsers.get(msg.getSender() + "-" + msg.getReceiver());
        sendSocket.session.getBasicRemote().sendText(message);
        ChatWebSocket receiveSocket = onlineUsers.get(msg.getReceiver() + "-" + msg.getSender());
        if (receiveSocket != null) {
            receiveSocket.session.getBasicRemote().sendText(message);
        } else {
            LambdaQueryWrapper<ChatSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ChatSession::getSender, msg.getReceiver());
            lambdaQueryWrapper.eq(ChatSession::getReceiver, msg.getSender());
            ChatSession one = sessionService.getOne(lambdaQueryWrapper);
            one.setUnread(one.getUnread() + 1);
            sessionService.updateById(one);
            messageService.save(msg);
        }
    }

    @OnClose
    public void onClose(@PathParam(value = "sender") int sender, @PathParam(value = "receiver") int receiver) {
        onlineUsers.remove(sender + "-" + receiver);
    }
}
