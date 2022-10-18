package zp.cdnu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import zp.cdnu.server.entity.User;

import java.io.IOException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author miya
 * @since 2022-09-02
 */
public interface IUserService extends IService<User> {

    String register(User user);

    String login(User user);

    String update(User user);

    String updateAvatar(User user, MultipartFile avatar) throws IOException;

    String updateResume(User user, MultipartFile resume) throws IOException;
}
