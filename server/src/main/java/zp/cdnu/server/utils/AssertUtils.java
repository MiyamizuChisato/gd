package zp.cdnu.server.utils;

import org.springframework.stereotype.Component;
import zp.cdnu.server.common.result.ServerException;

@Component
public class AssertUtils {
    public void isNull(Object o, ServerException e) {
        if (null != o) {
            throw e;
        }
    }

    public void notNull(Object o, ServerException e) {
        if (null == o) {
            throw e;
        }
    }

    public void isTrue(boolean b, ServerException e) {
        if (!b) {
            throw e;
        }
    }

    public void notTrue(boolean b, ServerException e) {
        if (b) {
            throw e;
        }
    }

    public void equals(Object o1, Object o2, ServerException e) {
        if (!o1.equals(o2)) {
            throw e;
        }
    }

    public void notEquals(Object o1, Object o2, ServerException e) {
        if (o1.equals(o2)) {
            throw e;
        }
    }
}
