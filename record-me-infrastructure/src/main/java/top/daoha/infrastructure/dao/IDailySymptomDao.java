package top.daoha.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.daoha.infrastructure.dao.po.DailySymptom;

import java.util.List;

@Mapper
public interface IDailySymptomDao {

    int insert(DailySymptom dailySymptom);

    DailySymptom selectById(Long recordId);

    List<DailySymptom> selectByCycleId(Long cycleId);

    List<DailySymptom> selectByUserIdAndDate(Long userId, String recordDate);

    DailySymptom selectByCycleIdAndDate(Long cycleId, String recordDate);

    int updateById(DailySymptom dailySymptom);

    int deleteById(Long recordId);

}
