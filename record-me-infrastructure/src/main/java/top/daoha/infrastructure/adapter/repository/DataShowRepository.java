package top.daoha.infrastructure.adapter.repository;

import org.springframework.stereotype.Repository;
import top.daoha.domain.dataShow.adapter.repository.IDataShowRepository;
import top.daoha.domain.dataShow.model.aggregate.DataShowAggregate;
import top.daoha.domain.dataShow.model.entity.CycleRecordEntity;
import top.daoha.domain.dataShow.model.entity.UserEntity;
import top.daoha.infrastructure.dao.ICycleRecordDao;
import top.daoha.infrastructure.dao.IDailyBehaviorLogDao;
import top.daoha.infrastructure.dao.IDailySymptomDao;
import top.daoha.infrastructure.dao.IUserInfoDao;
import top.daoha.infrastructure.dao.po.CycleRecord;
import top.daoha.infrastructure.dao.po.UserInfo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Repository
public class DataShowRepository implements IDataShowRepository {

    @Resource
    private ICycleRecordDao cycleRecordDao;

    @Resource
    private IUserInfoDao userInfoDao;

    @Resource
    private IDailySymptomDao dailySymptomDao;

    @Resource
    private IDailyBehaviorLogDao dailyBehaviorLogDao;


    @Override
    public DataShowAggregate getCycleRecordList(Long userId, Integer count) {
        //1在userInfoDao根据userid查询用户信息
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

        //2在cycleRecordDao查询用户的最新的count的记录信息，按照startDate时间顺序从最新到最旧排列
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

        //组合成DataShowAggregate返回
        return DataShowAggregate.builder()
                .user(userEntity)
                .cycleList(entityList)
                .build();
    }
}
