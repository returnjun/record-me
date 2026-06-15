package top.daoha.domain.cycle.service.lifecycle;

import org.springframework.stereotype.Service;
import top.daoha.domain.cycle.adapter.repository.ICycleRepository;

import javax.annotation.Resource;

@Service
public class CycleLifecycleService implements ICycleLifecycleService {

    @Resource
    private ICycleRepository cycleRepository;

    @Override
    public Boolean overCycleRecord(Long userId) {
        boolean isSuccess = cycleRepository.overCycleRecord(userId);
        if (!isSuccess) {
            return false;
        }
        cycleRepository.updateAvgData(userId);
        return true;
    }

    @Override
    public Boolean startCycleRecord(Long userId) {
        cycleRepository.closeCycleRecord(userId);
        return cycleRepository.startCycleRecord(userId);
    }

}
