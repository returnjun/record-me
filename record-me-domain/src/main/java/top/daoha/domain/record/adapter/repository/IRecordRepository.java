package top.daoha.domain.record.adapter.repository;

import top.daoha.domain.record.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.record.model.entity.SymptomEntity;

public interface IRecordRepository {
    //查询首页信息
    public IndexInfoAggregate getIndexInfoAggregate(Long userId);
    //将最新的周期加上当前在这个结束时间
    Boolean overCycleRecord(Long userId);
    //用来更新平均时长
    void updateAvgData(Long userId);
    //关闭当前周期，只是把这个周期的isactive更新为0
    boolean closeCycleRecord(Long userId);
    //插入一个新的周期记录，isactiive是1，以及开始时间等数据
    Boolean startCycleRecord(Long userId);
    //获得日常状态
    SymptomEntity getSymptomById(Long userid,Long cycleId);
    //插入日常状态
    SymptomEntity InsertSymptom(Long userid,Long cycleId);
    //改变日常状态
    Boolean changeSymptom(SymptomEntity symptomEntity);

}
