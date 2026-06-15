package top.daoha.domain.cycle.adapter.repository;

import top.daoha.domain.cycle.model.aggregate.CycleHistoryAggregate;
import top.daoha.domain.cycle.model.aggregate.RecordsAggregate;
import top.daoha.domain.cycle.model.entity.CycleRecordEntity;

import java.util.List;

public interface ICycleHistoryRepository {

    CycleHistoryAggregate getCycleRecordList(Long userId, Integer count);
    //根据用户id、页码和条数获得用户的生理期历史记录列表（含总记录数）
    RecordsAggregate getRecordsList(Long userId, Integer page, Integer pageSize);
    //根据用户的修改来更新历史记录
    Boolean updateCycleRecord(CycleRecordEntity cycleRecordEntity);
}
