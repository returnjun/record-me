package top.daoha.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.daoha.infrastructure.dao.po.UserLoginLog;

import java.util.List;

@Mapper
public interface IUserLoginLogDao {

    int insert(UserLoginLog userLoginLog);

    UserLoginLog selectById(Long id);

    List<UserLoginLog> selectByUserId(Long userId);

    UserLoginLog selectLatestByUserId(Long userId);

    List<UserLoginLog> selectByUserIdAndStatus(Long userId, Integer loginStatus);

    int deleteById(Long id);

}
