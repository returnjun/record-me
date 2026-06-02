package top.daoha.domain.userLogin.service;

import top.daoha.domain.record.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.userLogin.model.entity.UserEntity;

public interface IloginService {
    //1 注册新用户信息
    boolean checkUserPassword(UserEntity user);
}
