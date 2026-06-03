package top.daoha.domain.auth.service;

import top.daoha.domain.auth.model.entity.UserEntity;

public interface IAuthService {
    //1 注册新用户信息
    Long checkUserLogin(UserEntity user);

    UserEntity userRegister(UserEntity user);
}
