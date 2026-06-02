package top.daoha.domain.userLogin.adapter.repository;

import top.daoha.domain.record.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.userLogin.model.entity.UserEntity;

import java.util.Date;

public interface ILoginRepository {
    // 根据用户名查询或者电话号查询用户信息
    UserEntity queryByUsername(String username);

    //校验用户登陆的时候填写的密码是否正确，使用不可逆的哈希函数BCrypt进行处理再比对
    boolean checkUserPassword(UserEntity user);

    //根据用户名和电话号校验用户注册是否合理
    boolean checkUsernameExists(String username); // 注册前的校验

    // 注册的时候将整个数据插入相应的表
    boolean insertUser(UserEntity user);

    // 登录成功一次就需要更新一下用户登录日志
    void updateLoginTrace(Long userId, String loginIp, Date loginTime);

    //更改用户密码一定使用不可逆的哈希函数BCrypt进行处理后存储
    void updatePassword(Long userId, String encryptedPassword);

    // ---- 删除类 ----
    void logicalDeleteUser(Long userId);
}
