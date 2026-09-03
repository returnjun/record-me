package top.daoha.domain.health.service;

import top.daoha.domain.health.model.entity.DailySymptomEntity;

import java.util.List;

public interface IHealthTrackService {

    DailySymptomEntity getTodaySymptom(Long userId, Long cycleId);

    Boolean changeSymptom(DailySymptomEntity dailySymptomEntity);

    List<DailySymptomEntity> listByCycleId(Long cycleId);

}
