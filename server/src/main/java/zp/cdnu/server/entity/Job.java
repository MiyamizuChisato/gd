package zp.cdnu.server.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author miya
 * @since 2022-09-05
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("job")
@ApiModel(value = "Job对象", description = "")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "jid", type = IdType.AUTO)
    private Integer jid;

    private Integer uid;

    private String title;

    private String content;

    private String company;

    private String wage;

    private Integer hot;

    @TableLogic
    @TableField("is_deleted")
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
