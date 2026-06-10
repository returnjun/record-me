package top.daoha.domain.record.service.change;

import top.daoha.domain.record.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.record.model.entity.SymptomEntity;

public interface IUpdateRecordService {
    //1 结束当前这个周期
    public Boolean overCycleRecord(Long userId);
    //2 开始一个新周期
    public Boolean startCycleRecord(Long userId);
    //3更改当天状态
    public Boolean changeSymptom(SymptomEntity symptomEntity);
}
