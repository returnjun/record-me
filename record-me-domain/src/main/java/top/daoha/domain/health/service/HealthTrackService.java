package top.daoha.domain.health.service;

import org.springframework.stereotype.Service;
import top.daoha.domain.health.adapter.repository.IHealthTrackRepository;
import top.daoha.domain.health.model.entity.DailySymptomEntity;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HealthTrackService implements IHealthTrackService {

    @Resource
    private IHealthTrackRepository healthTrackRepository;

    @Override
    public DailySymptomEntity getTodaySymptom(Long userId, Long cycleId) {
        DailySymptomEntity dailySymptomEntity = healthTrackRepository.getTodaySymptom(userId, cycleId);
        if (dailySymptomEntity == null) {
            dailySymptomEntity = healthTrackRepository.createTodaySymptom(userId, cycleId);
        }
        return dailySymptomEntity;
    }

    @Override
    public Boolean changeSymptom(DailySymptomEntity dailySymptomEntity) {
        return healthTrackRepository.changeSymptom(dailySymptomEntity);
    }

    @Override
    public List<DailySymptomEntity> listByCycleId(Long cycleId) {
        return healthTrackRepository.listByCycleId(cycleId);
    }

}
