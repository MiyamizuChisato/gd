package zp.cdnu.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author miya
 * @since 2022-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_session")
@ApiModel(value="ChatSession对象", description="")
public class ChatSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cs_id", type = IdType.AUTO)
    private Integer csId;

    private Integer sender;

    private Integer receiver;

    private Integer unread;


}
