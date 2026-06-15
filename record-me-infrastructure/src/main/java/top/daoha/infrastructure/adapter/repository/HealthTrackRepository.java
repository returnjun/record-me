package top.daoha.infrastructure.adapter.repository;

import org.springframework.stereotype.Repository;
import top.daoha.domain.health.adapter.repository.IHealthTrackRepository;
import top.daoha.domain.health.model.entity.DailySymptomEntity;
import top.daoha.infrastructure.dao.IDailySymptomDao;
import top.daoha.infrastructure.dao.po.DailySymptom;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public class HealthTrackRepository implements IHealthTrackRepository {

    @Resource
    private IDailySymptomDao dailySymptomDao;

    @Override
    public DailySymptomEntity getTodaySymptom(Long userId, Long cycleId) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        DailySymptom po = dailySymptomDao.selectByCycleIdAndDate(cycleId, today);
        if (po == null) {
            return null;
        }
        return toEntity(po);
    }

    @Override
    public DailySymptomEntity createTodaySymptom(Long userId, Long cycleId) {
        DailySymptom po = new DailySymptom();
        po.setCycleId(cycleId);
        po.setUserId(userId);
        po.setRecordDate(new Date());
        po.setFlowLevel(0);
        po.setPainLevel(0);
        dailySymptomDao.insert(po);
        return toEntity(po);
    }

    @Override
    public Boolean changeSymptom(DailySymptomEntity dailySymptomEntity) {
        DailySymptom po = new DailySymptom();
        po.setCycleId(dailySymptomEntity.getCycleId());
        po.setUserId(dailySymptomEntity.getUserId());
        po.setRecordId(dailySymptomEntity.getRecordId());
        po.setFlowLevel(dailySymptomEntity.getFlowLevel());
        po.setPainLevel(dailySymptomEntity.getPainLevel());
        po.setMood(dailySymptomEntity.getMood());
        po.setNotes(dailySymptomEntity.getNotes());
        int count = dailySymptomDao.updateById(po);
        return count > 0;
    }

    private DailySymptomEntity toEntity(DailySymptom po) {
        DailySymptomEntity entity = new DailySymptomEntity();
        entity.setRecordId(po.getRecordId());
        entity.setCycleId(po.getCycleId());
        entity.setUserId(po.getUserId());
        entity.setRecordDate(po.getRecordDate());
        entity.setFlowLevel(po.getFlowLevel());
        entity.setPainLevel(po.getPainLevel());
        entity.setMood(po.getMood());
        entity.setNotes(po.getNotes());
        entity.setCreateTime(po.getCreateTime());
        entity.setUpdateTime(po.getUpdateTime());
        return entity;
    }

}
