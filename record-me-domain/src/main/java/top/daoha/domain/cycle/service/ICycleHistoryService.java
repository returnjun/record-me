package top.daoha.domain.cycle.service;

import top.daoha.domain.cycle.model.aggregate.CycleHistoryAggregate;
import top.daoha.domain.cycle.model.aggregate.RecordsAggregate;
import top.daoha.domain.cycle.model.entity.CycleRecordEntity;

import java.util.List;

public interface ICycleHistoryService {

    CycleHistoryAggregate getCycleRecordList(Long userId, Integer count);
    //获得这个记录列表（分页）
    RecordsAggregate getRecordsList(Long userId, Integer page, Integer pageSize);
    //修改这个记录信息
    Boolean updateCycleRecord(CycleRecordEntity cycleRecordEntity);
}
