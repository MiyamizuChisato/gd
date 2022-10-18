package zp.cdnu.server.middleware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import zp.cdnu.server.config.RabbitConfig;
import zp.cdnu.server.entity.Mail;
import zp.cdnu.server.utils.MailUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@Slf4j
@Component
public class RabbitMQ {
    @Resource
    private MailUtils mailUtils;

    @RabbitListener(queues = RabbitConfig.MAIL_VERIFY_QUEUE_NAME)
    public void emailVerify(Mail mail) throws MessagingException {
        log.info("接收到验证码消息任务，向[{}]------>发送验证码", mail.getTaker());
        mailUtils.sendVerifyMail(mail);
    }

    @RabbitListener(queues = RabbitConfig.MAIL_RESUME_QUEUE_NAME)
    public void resume(Mail mail) throws MessagingException {
        log.info("接收到简历投递任务，向[{}]------>发送邮件", mail.getTaker());
        mailUtils.sendResume(mail);
    }

    @RabbitListener(queues = RabbitConfig.MAIL_RESUME_PASS_QUEUE_NAME)
    public void resumePass(Mail mail) throws MessagingException {
        log.info("接收到简历通过任务，向[{}]------>发送邮件", mail.getTaker());
        mailUtils.sendResumePass(mail);
    }
}
