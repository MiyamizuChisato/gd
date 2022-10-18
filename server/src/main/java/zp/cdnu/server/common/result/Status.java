package zp.cdnu.server.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    SUCCESS(1, "操作成功"),
    FAILED(-1, "内部错误"),
    USER_ERROR(-2, "用户信息错误"),
    VALIDATE_ERROR(-4, "调用请求错误"),
    PATH_ERROR(-400, "未找到请求的资源");
    private final int code;
    private final String message;
}
