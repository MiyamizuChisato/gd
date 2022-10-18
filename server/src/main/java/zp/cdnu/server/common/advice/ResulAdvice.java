package zp.cdnu.server.common.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import zp.cdnu.server.common.annotation.Body;
import zp.cdnu.server.common.result.R;
import zp.cdnu.server.common.result.Status;

import javax.annotation.Resource;

@RestControllerAdvice(basePackages = "zp.cdnu.server.controller")
public class ResulAdvice implements ResponseBodyAdvice<Object> {
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !(returnType.hasMethodAnnotation(Body.class) || returnType.getParameterType().isAssignableFrom(R.class));
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {
            return objectMapper.writeValueAsString(R.state(Status.SUCCESS).data(body));
        }
        return R.state(Status.SUCCESS).data(body);
    }
}