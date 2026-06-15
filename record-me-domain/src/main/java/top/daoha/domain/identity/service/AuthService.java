package top.daoha.domain.identity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.daoha.domain.identity.adapter.port.IPasswordEncoder;
import top.daoha.domain.identity.adapter.repository.ILoginRepository;
import top.daoha.domain.identity.model.entity.UserEntity;
import top.daoha.types.enums.ResponseCode;
import top.daoha.types.exception.AppException;

import javax.annotation.Resource;

@Slf4j
@Service
public class AuthService implements IAuthService {

    @Resource
    private ILoginRepository loginRepository;

    @Resource
    private IPasswordEncoder passwordEncoder;

    @Override
    public Long checkUserLogin(UserEntity user) {
        // 先检查用户名是否存在
        UserEntity userEntity = loginRepository.queryByUsername(user.getUsername());
        if (userEntity == null) {
            log.info("用户名不存在");
            throw new AppException(ResponseCode.USERNAME_UNEXIT);
        }
        boolean isTrue = userEntity.verifyPassword(user.getPassword(), passwordEncoder);
        if (!isTrue) {
            log.info("密码输入错误");
            throw new AppException(ResponseCode.PASSWORD_ERROR);
        }
        return userEntity.getUserId();
    }

    @Override
    public UserEntity userRegister(UserEntity user) {
        UserEntity userEntity = loginRepository.queryByUsername(user.getUsername());
        if (userEntity != null) {
            log.info("用户名已经存在");
            throw new AppException(ResponseCode.USERNAME_EXIT);
        }

        user.encryptPassword(user.getPassword(), passwordEncoder);

        return loginRepository.insertUser(user);
    }

    @Override
    public UserEntity queryUserInfo(Long userId) {

        return loginRepository.queryByUserId(userId);
    }

    @Override
    public Boolean updataUserInfo(UserEntity user) {
        return loginRepository.updataUserInfo(user);
    }
}
