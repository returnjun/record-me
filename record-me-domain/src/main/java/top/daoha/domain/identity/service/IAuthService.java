package top.daoha.domain.identity.service;

import top.daoha.domain.identity.model.entity.UserEntity;

public interface IAuthService {
    //1 检查用户登录的情况
    UserEntity checkUserLogin(UserEntity user);

    UserEntity userRegister(UserEntity user);

    //这个使用于我的主页进行查询用户的
    UserEntity queryUserInfo(Long userId);
    //修改用户个人信息
    Boolean updataUserInfo(UserEntity user);
}
