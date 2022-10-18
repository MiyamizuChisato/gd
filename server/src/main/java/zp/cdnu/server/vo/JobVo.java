package zp.cdnu.server.vo;

import lombok.Data;
import zp.cdnu.server.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class JobVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer jid;

    private Integer uid;

    private String title;

    private String content;

    private String company;

    private String wage;

    private Integer hot;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private User user;
}
