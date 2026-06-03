package top.daoha.infrastructure.adapter.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.daoha.domain.auth.adapter.repository.ILoginRepository;
import top.daoha.domain.auth.model.entity.UserEntity;
import top.daoha.infrastructure.dao.IUserInfoDao;
import top.daoha.infrastructure.dao.IUserLoginLogDao;
import top.daoha.infrastructure.dao.po.UserInfo;
import top.daoha.infrastructure.dao.po.UserLoginLog;
import top.daoha.types.enums.ResponseCode;
import top.daoha.types.exception.AppException;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Repository
public class AuthRepository implements ILoginRepository {

    @Resource
    private IUserInfoDao userInfoDao;

    @Resource
    private IUserLoginLogDao userLoginLogDao;

    @Override
    public UserEntity queryByUsername(String username) {
        UserInfo userInfo = userInfoDao.selectByUsername(username);
        if (userInfo == null) {
            return null;
        }
        return UserEntity.builder()
                .userId(userInfo.getUserId())
                .username(userInfo.getUsername())
                .password(userInfo.getPassword())
                .phone(userInfo.getPhone())
                .build();
    }


    @Override
    public UserEntity insertUser(UserEntity user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(user.getUsername());
        // 直接取实体中的密码，此时它已经被 Domain 层加密过了，Repository 不做任何处理
        userInfo.setPassword(user.getPassword());
        userInfo.setPhone(user.getPhone());
        // 2. 执行插入，MyBatis 会通过 useGeneratedKeys 把生成的 ID 赋值给 userInfo.userId
        int count = userInfoDao.insert(userInfo);

        if (count > 0) {
            // 3. 将生成的数据库 ID 回填给 Domain 层的实体，使其成为“完整体”
            user.setUserId(userInfo.getUserId());

            log.info("用户注册成功 userId={}, username={}", user.getUserId(), user.getUsername());

            // 4. 返回完整实体
            return user;
        }

        // 5. 插入失败时，直接抛出你在 types 层定义的业务/系统异常
        log.error("数据库插入用户失败, username={}", user.getUsername());
        throw new AppException(ResponseCode.UN_ERROR.getCode(), "数据库写入失败");
    }

    @Override
    public void updateLoginTrace(Long userId, String loginIp, Date loginTime) {
        UserLoginLog logEntry = new UserLoginLog();
        logEntry.setUserId(userId);
        logEntry.setLoginIp(loginIp);
        logEntry.setLoginTime(loginTime);
        logEntry.setLoginStatus(1);
        userLoginLogDao.insert(logEntry);
        log.info("登录痕迹已记录 userId={}, loginIp={}", userId, loginIp);
    }

    @Override
    public void updatePassword(Long userId, String encryptedPassword) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setPassword(encryptedPassword);
        userInfoDao.updateById(userInfo);
        log.info("密码已更新 userId={}", userId);
    }

    @Override
    public void logicalDeleteUser(Long userId) {
        userInfoDao.deleteById(userId);
        log.info("用户已逻辑删除 userId={}", userId);
    }
}
