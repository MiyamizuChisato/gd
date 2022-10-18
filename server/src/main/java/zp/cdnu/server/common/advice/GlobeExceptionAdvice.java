package zp.cdnu.server.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zp.cdnu.server.common.result.R;
import zp.cdnu.server.common.result.ServerException;
import zp.cdnu.server.common.result.Status;

@Slf4j
@RestControllerAdvice
public class GlobeExceptionAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Object> methodArgsNotValidExceptionHandler() {
        return R.state(Status.VALIDATE_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Object> methodNotSupportedExceptionHandler() {
        return R.state(Status.PATH_ERROR);
    }

    @ExceptionHandler(ServerException.class)
    public R<String> recruitExceptionHandler(ServerException e) {
        R<String> result = R.state(e);
        log.error("timestamp------>[{}]------>发生业务异常------>[{}]", result.getTimestamp(), e.getMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception e) {
        R<String> result = R.state(Status.FAILED);
        result.setData(e.getMessage());
        log.error("timestamp------>[{}]------>发生程序异常------>[{}]", result.getTimestamp(), e.getMessage());
        return result;
    }
}
