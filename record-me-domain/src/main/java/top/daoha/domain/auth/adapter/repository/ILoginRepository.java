package top.daoha.domain.auth.adapter.repository;

import top.daoha.domain.auth.model.entity.UserEntity;

import java.util.Date;

public interface ILoginRepository {
    // 根据用户名查询或者电话号查询用户信息
    UserEntity queryByUsername(String username);

    // 注册的时候将整个数据插入相应的表
    UserEntity insertUser(UserEntity user);

    // 登录成功一次就需要更新一下用户登录日志
    void updateLoginTrace(Long userId, String loginIp, Date loginTime);

    //更改用户密码一定使用不可逆的哈希函数BCrypt进行处理后存储
    void updatePassword(Long userId, String encryptedPassword);

    // ---- 删除类 ----
    void logicalDeleteUser(Long userId);
}
