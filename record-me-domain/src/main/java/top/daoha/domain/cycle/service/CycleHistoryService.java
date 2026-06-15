package top.daoha.domain.cycle.service;

import org.springframework.stereotype.Service;
import top.daoha.domain.cycle.adapter.repository.ICycleHistoryRepository;
import top.daoha.domain.cycle.model.aggregate.CycleHistoryAggregate;
import top.daoha.domain.cycle.model.aggregate.RecordsAggregate;
import top.daoha.domain.cycle.model.entity.CycleRecordEntity;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CycleHistoryService implements ICycleHistoryService {

    @Resource
    private ICycleHistoryRepository cycleHistoryRepository;

    @Override
    public CycleHistoryAggregate getCycleRecordList(Long userId, Integer count) {
        return cycleHistoryRepository.getCycleRecordList(userId, count);
    }

    @Override
    public RecordsAggregate getRecordsList(Long userId, Integer page, Integer pageSize) {
        return cycleHistoryRepository.getRecordsList(userId, page, pageSize);
    }

    @Override
    public Boolean updateCycleRecord(CycleRecordEntity cycleRecordEntity) {
        return cycleHistoryRepository.updateCycleRecord(cycleRecordEntity);
    }
}
