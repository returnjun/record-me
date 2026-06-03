package top.daoha.domain.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import top.daoha.domain.auth.adapter.port.IPasswordEncoder;
import top.daoha.domain.auth.adapter.repository.ILoginRepository;
import top.daoha.domain.auth.model.entity.UserEntity;
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
        //首先检查用户名是否存在
        UserEntity userEntity = loginRepository.queryByUsername(user.getUsername());
        if(userEntity==null){
            log.info("用户名不存在");
            throw new AppException(ResponseCode.USERNAME_UNEXIT);
        }
        boolean istrue = userEntity.verifyPassword(user.getPassword(), passwordEncoder);
        if(!istrue){
            log.info("密码输入错误");
            throw new AppException(ResponseCode.PASSWORD_ERROR);
        }
        return userEntity.getUserId();
    }

    @Override
    public UserEntity userRegister(UserEntity user) {

        UserEntity userEntity = loginRepository.queryByUsername(user.getUsername());
        if(userEntity!=null){
            log.info("用户名已经存在了");
            throw new AppException(ResponseCode.USERNAME_EXIT);
        }

        user.encryptPassword(user.getPassword(), passwordEncoder);

        return loginRepository.insertUser(user);
    }
}
