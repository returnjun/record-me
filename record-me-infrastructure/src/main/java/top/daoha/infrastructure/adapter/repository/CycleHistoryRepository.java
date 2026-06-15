package top.daoha.infrastructure.adapter.repository;

import org.springframework.stereotype.Repository;
import top.daoha.domain.cycle.adapter.repository.ICycleHistoryRepository;
import top.daoha.domain.cycle.model.aggregate.CycleHistoryAggregate;
import top.daoha.domain.cycle.model.aggregate.RecordsAggregate;
import top.daoha.domain.cycle.model.entity.CycleRecordEntity;
import top.daoha.domain.cycle.model.entity.UserEntity;
import top.daoha.infrastructure.dao.ICycleRecordDao;
import top.daoha.infrastructure.dao.IUserInfoDao;
import top.daoha.infrastructure.dao.po.CycleRecord;
import top.daoha.infrastructure.dao.po.UserInfo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CycleHistoryRepository implements ICycleHistoryRepository {

    @Resource
    private ICycleRecordDao cycleRecordDao;

    @Resource
    private IUserInfoDao userInfoDao;

    @Override
    public CycleHistoryAggregate getCycleRecordList(Long userId, Integer count) {
        // 查询用户基础信息。
        UserInfo userInfoPO = userInfoDao.selectById(userId);
        if (userInfoPO == null) {
            return null;
        }

        UserEntity userEntity = UserEntity.builder()
                .userId(userInfoPO.getUserId())
                .userName(userInfoPO.getUsername())
                .avatar(userInfoPO.getAvatar())
                .avgCycleDays(userInfoPO.getAvgCycleDays())
                .avgPeriodDays(userInfoPO.getAvgPeriodDays())
                .build();

        // 查询最近 count 条周期记录，DAO 已按开始时间从新到旧排序。
        List<CycleRecord> poList = cycleRecordDao.selectByUserId(userId);
        List<CycleRecordEntity> entityList = new ArrayList<>();
        int limit = Math.min(count, poList.size());
        for (int i = 0; i < limit; i++) {
            CycleRecord po = poList.get(i);
            entityList.add(CycleRecordEntity.builder()
                    .userId(po.getUserId())
                    .cycleId(po.getCycleId())
                    .startDate(po.getStartDate())
                    .endDate(po.getEndDate())
                    .isActive(po.getIsActive())
                    .build());
        }

        return CycleHistoryAggregate.builder()
                .user(userEntity)
                .cycleList(entityList)
                .build();
    }

    @Override
    public RecordsAggregate getRecordsList(Long userId, Integer page, Integer pageSize) {
        int totalCount = cycleRecordDao.countByUserId(userId);
        int offset = (page - 1) * pageSize;

        List<CycleRecord> poList = cycleRecordDao.selectByUserIdPage(userId, offset, pageSize);

        List<CycleRecordEntity> entityList = new ArrayList<>();
        for (CycleRecord po : poList) {
            entityList.add(CycleRecordEntity.builder()
                    .userId(po.getUserId())
                    .cycleId(po.getCycleId())
                    .startDate(po.getStartDate())
                    .endDate(po.getEndDate())
                    .isActive(po.getIsActive())
                    .build());
        }

        return RecordsAggregate.builder()
                .cycleList(entityList)
                .count(totalCount)
                .build();
    }

    @Override
    public Boolean updateCycleRecord(CycleRecordEntity cycleRecordEntity) {
        CycleRecord po = new CycleRecord();
        po.setCycleId(cycleRecordEntity.getCycleId());
        po.setStartDate(cycleRecordEntity.getStartDate());
        po.setEndDate(cycleRecordEntity.getEndDate());
        po.setIsActive(cycleRecordEntity.getIsActive());
        int count = cycleRecordDao.updateById(po);
        return count > 0;
    }

}
