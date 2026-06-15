package top.daoha.domain.health.service;

import top.daoha.domain.health.model.entity.DailySymptomEntity;

public interface IHealthTrackService {

    DailySymptomEntity getTodaySymptom(Long userId, Long cycleId);

    Boolean changeSymptom(DailySymptomEntity dailySymptomEntity);

}
