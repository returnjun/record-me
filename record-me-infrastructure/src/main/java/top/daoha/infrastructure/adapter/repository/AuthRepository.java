package top.daoha.infrastructure.adapter.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.daoha.domain.identity.adapter.repository.ILoginRepository;
import top.daoha.domain.identity.model.entity.UserEntity;
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
    public UserEntity queryByUserId(Long userId) {
        UserInfo userInfo = userInfoDao.selectById(userId);
        if (userInfo == null) {
            return null;
        }
        return UserEntity.builder()
                .userId(userInfo.getUserId())
                .username(userInfo.getUsername())
                .password(userInfo.getPassword())
                .phone(userInfo.getPhone())
                .avatar(userInfo.getAvatar())
                .birthday(userInfo.getBirthday())
                .height(userInfo.getHeight())
                .weight(userInfo.getWeight())
                .build();
    }

    @Override
    public Boolean updataUserInfo(UserEntity user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setPhone(user.getPhone());
        userInfo.setBirthday(user.getBirthday());
        userInfo.setHeight(user.getHeight());
        userInfo.setWeight(user.getWeight());
        int count = userInfoDao.updateById(userInfo);
        log.info("用户信息已更新 userId={}", user.getUserId());
        return count > 0;
    }

    @Override
    public UserEntity insertUser(UserEntity user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(user.getUsername());
        userInfo.setPassword(user.getPassword());
        userInfo.setPhone(user.getPhone());

        int count = userInfoDao.insert(userInfo);
        if (count > 0) {
            user.setUserId(userInfo.getUserId());
            log.info("用户注册成功 userId={}, username={}", user.getUserId(), user.getUsername());
            return user;
        }

        log.error("数据库插入用户失败 username={}", user.getUsername());
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
