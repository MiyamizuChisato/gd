package zp.cdnu.server.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerException extends RuntimeException {
    private int code;
    private String message;

    public ServerException(Status status) {

        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public ServerException message(String message) {
        this.message = message;
        return this;
    }
}
