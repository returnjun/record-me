package top.daoha.domain.identity.adapter.repository;

import top.daoha.domain.identity.model.entity.UserEntity;

import java.util.Date;

public interface ILoginRepository {

    // 根据用户名查询用户信息
    UserEntity queryByUsername(String username);
    // 根据用户ID查询用户信息
    UserEntity queryByUserId(Long userId);
    //个人信息界面来进行修改
    Boolean updataUserInfo(UserEntity user);
    // 注册时写入用户信息
    UserEntity insertUser(UserEntity user);

    // 记录用户登录痕迹
    void updateLoginTrace(Long userId, String loginIp, Date loginTime);

    // 使用 BCrypt 密文更新用户密码
    void updatePassword(Long userId, String encryptedPassword);

    // 逻辑删除用户
    void logicalDeleteUser(Long userId);


}
