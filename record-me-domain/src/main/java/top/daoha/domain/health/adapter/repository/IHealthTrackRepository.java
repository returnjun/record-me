package top.daoha.domain.health.adapter.repository;

import top.daoha.domain.health.model.entity.DailySymptomEntity;

public interface IHealthTrackRepository {

    DailySymptomEntity getTodaySymptom(Long userId, Long cycleId);

    DailySymptomEntity createTodaySymptom(Long userId, Long cycleId);

    Boolean changeSymptom(DailySymptomEntity dailySymptomEntity);

}
