package top.daoha.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.daoha.infrastructure.dao.po.CycleRecord;

import java.util.List;

@Mapper
public interface ICycleRecordDao {

    int insert(CycleRecord cycleRecord);

    CycleRecord selectById(Long cycleId);

    List<CycleRecord> selectByUserId(Long userId);

    CycleRecord selectActiveByUserId(Long userId);

    int updateById(CycleRecord cycleRecord);

    int deleteById(Long cycleId);

    int overByUserId(Long userId);

    int closeByUserId(Long userId);

    List<CycleRecord> selectCompletedByUserId(Long userId);

    int countByUserId(Long userId);

    List<CycleRecord> selectByUserIdPage(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);

}
