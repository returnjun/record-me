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
    public UserEntity checkUserLogin(UserEntity user) {
        String loginAccount = user.getUsername();
        UserEntity userEntity = loginRepository.queryByUsername(loginAccount);
        if (userEntity == null) {
            userEntity = loginRepository.queryByPhone(loginAccount);
        }
        if (userEntity == null) {
            log.info("用户名或手机号不存在 loginAccount={}", loginAccount);
            throw new AppException(ResponseCode.USERNAME_UNEXIT);
        }
        boolean isTrue = userEntity.verifyPassword(user.getPassword(), passwordEncoder);
        if (!isTrue) {
            log.info("密码输入错误");
            throw new AppException(ResponseCode.PASSWORD_ERROR);
        }
        return userEntity;
    }

    @Override
    public UserEntity userRegister(UserEntity user) {
        UserEntity userEntity = loginRepository.queryByUsername(user.getUsername());
        if (userEntity != null) {
            log.info("用户名已经存在");
            throw new AppException(ResponseCode.USERNAME_EXIT);
        }

        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            UserEntity phoneUserEntity = loginRepository.queryByPhone(user.getPhone().trim());
            if (phoneUserEntity != null) {
                log.info("手机号已经存在");
                throw new AppException(ResponseCode.INDEX_EXCEPTION.getCode(), "手机号已经存在");
            }
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
