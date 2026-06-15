package top.daoha.domain.cycle.service;

import org.springframework.stereotype.Service;
import top.daoha.domain.cycle.adapter.repository.ICycleRepository;
import top.daoha.domain.cycle.model.aggregate.IndexInfoAggregate;

import javax.annotation.Resource;

@Service
public class CycleService implements ICycleService {

    @Resource
    private ICycleRepository cycleRepository;

    @Override
    public IndexInfoAggregate getIndexInfoAggregate(Long userId) {
        return cycleRepository.getIndexInfoAggregate(userId);
    }

}
