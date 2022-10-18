package zp.cdnu.server.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.ibatis.annotations.Insert;
import zp.cdnu.server.common.annotation.validate.Login;
import zp.cdnu.server.common.annotation.validate.Register;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Null(groups = {Register.class})
    private Integer uid;

    @NotEmpty(groups = {Register.class})
    @Pattern(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$",
            groups = {Register.class, Login.class})
    private String email;

    @NotEmpty(groups = {Register.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z]{2,12}$",
            groups = {Register.class})
    private String nickname;

    private String sign;
    @NotEmpty(groups = {Register.class, Login.class})
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9a-zA-Z]{8,16}$",
            groups = {Register.class, Login.class})
    private String password;

    private String avatar;

    private String resume;

    private Integer deleted;

    @NotEmpty(message = "验证码不能为空", groups = {Register.class})
    private String code;
}
