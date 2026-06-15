package top.daoha.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.daoha.infrastructure.dao.po.DailyBehaviorLog;

import java.util.List;

@Mapper
public interface IDailyBehaviorLogDao {

    int insert(DailyBehaviorLog dailyBehaviorLog);

    DailyBehaviorLog selectById(Long logId);

    List<DailyBehaviorLog> selectByUserId(Long userId);

    DailyBehaviorLog selectByUserIdAndDate(Long userId, String recordDate);

    int updateById(DailyBehaviorLog dailyBehaviorLog);

    int deleteById(Long logId);

}
