package zp.cdnu.server.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import zp.cdnu.server.entity.Job;
import zp.cdnu.server.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ScheduleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer sid;

    private Integer jid;

    private Integer aid;

    private Integer bid;

    private Integer progress;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Job job;

    private User user;
}
