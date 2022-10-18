package zp.cdnu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import zp.cdnu.server.common.result.ServerException;
import zp.cdnu.server.common.result.Status;
import zp.cdnu.server.entity.User;
import zp.cdnu.server.mapper.UserMapper;
import zp.cdnu.server.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zp.cdnu.server.utils.AssertUtils;
import zp.cdnu.server.utils.OssUtils;
import zp.cdnu.server.utils.UserUtils;
import zp.cdnu.server.vo.UserVo;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author miya
 * @since 2022-09-02
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private AssertUtils assertUtils;
    @Resource
    private OssUtils ossUtils;

    @Override
    public String register(User user) {
        user.setPassword(UserUtils.passwordEncode(user.getPassword()));
        baseMapper.insert(user);
        user = baseMapper.selectById(user);
        return UserUtils.tokenGenerator(user);
    }

    @Override
    public String login(User user) {
        String password = user.getPassword();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, user.getEmail());
        user = baseMapper.selectOne(queryWrapper);
        assertUtils.notNull(user, new ServerException(Status.USER_ERROR));
        password = UserUtils.passwordEncode(password);
        assertUtils.equals(password, user.getPassword(), new ServerException(Status.USER_ERROR));
        return UserUtils.tokenGenerator(user);
    }

    @Override
    public String update(User user) {
        if (StringUtils.hasLength(user.getPassword())) {
            user.setPassword(UserUtils.passwordEncode(user.getPassword()));
        }
        baseMapper.updateById(user);
        return UserUtils.tokenGenerator(user);
    }

    @Override
    public String updateAvatar(User user, MultipartFile avatar) throws IOException {
        user = baseMapper.selectById(user.getUid());
        ossUtils.deleteAvatar(user.getAvatar());
        String filename = "https://hire-avatar.oss-cn-hangzhou.aliyuncs.com/" + ossUtils.uploadAvatar(avatar);
        user.setAvatar(filename);
        baseMapper.updateById(user);
        return UserUtils.tokenGenerator(user);
    }

    @Override
    public String updateResume(User user, MultipartFile resume) throws IOException {
        user = baseMapper.selectById(user.getUid());
        ossUtils.deleteResume(user.getResume());
        String filename = "https://hire-resume.oss-cn-hangzhou.aliyuncs.com/" + ossUtils.uploadResume(resume);
        user.setResume(filename);
        baseMapper.updateById(user);
        return filename;
    }
}
