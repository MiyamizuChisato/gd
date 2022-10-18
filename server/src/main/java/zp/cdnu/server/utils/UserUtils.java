package zp.cdnu.server.utils;

import cn.hutool.core.codec.Rot;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import zp.cdnu.server.entity.User;
import zp.cdnu.server.vo.UserVo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UserUtils {
    private static final String SECRET = "ao1sakura";

    public static final String REGEX_EMAIL = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";

    public static String passwordEncode(String password) {
        password = Rot.encode13(password);
        return SecureUtil.md5(password);
    }

    public static String tokenGenerator(User user) {
        Map<String, Object> payload = new HashMap<>();
        DateTime now = DateTime.now();
        DateTime outTime = now.offsetNew(DateField.DAY_OF_WEEK, 1);
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, outTime);
        payload.put(JWTPayload.NOT_BEFORE, now);
        payload.put("uid", user.getUid());
        payload.put("avatar", user.getAvatar());
        payload.put("nickname", user.getNickname());
        return JWTUtil.createToken(payload, SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean tokenValidator(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return jwt.setKey(SECRET.getBytes(StandardCharsets.UTF_8)).verify() && jwt.validate(0);
    }

    public static Object tokenParser(String token, String key) {
        JWT jwt = JWTUtil.parseToken(token);
        return jwt.getPayload(key);
    }

    public static boolean emailValidate(String email) {
        return Validator.isMatchRegex(REGEX_EMAIL, email);
    }
}
