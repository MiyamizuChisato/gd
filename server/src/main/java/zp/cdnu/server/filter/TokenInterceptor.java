package zp.cdnu.server.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import zp.cdnu.server.common.annotation.UseToken;
import zp.cdnu.server.common.result.ServerException;
import zp.cdnu.server.common.result.Status;
import zp.cdnu.server.utils.AssertUtils;
import zp.cdnu.server.utils.UserUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private AssertUtils assertUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UseToken flag = null;
        boolean pass = true;
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            flag = method.getMethodAnnotation(UseToken.class);
        }
        if (null != flag) {
            String token = request.getHeader("Authorization");
            assertUtils.notNull(token, new ServerException(Status.USER_ERROR));
            pass = UserUtils.tokenValidator(token);
            assertUtils.isTrue(pass, new ServerException(Status.USER_ERROR));
            Integer uid = (Integer) UserUtils.tokenParser(token, "uid");
            request.setAttribute("uid", uid);
        }
        return pass;
    }
}
