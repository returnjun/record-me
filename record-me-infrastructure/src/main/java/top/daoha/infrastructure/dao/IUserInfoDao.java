package top.daoha.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.daoha.infrastructure.dao.po.UserInfo;

import java.util.List;

@Mapper
public interface IUserInfoDao {

    int insert(UserInfo userInfo);

    UserInfo selectById(Long userId);

    UserInfo selectByUsername(String username);

    List<UserInfo> selectList(UserInfo userInfo);

    int updateById(UserInfo userInfo);

    int deleteById(Long userId);

}
