package top.daoha.infrastructure.adapter.repository;

import org.springframework.stereotype.Repository;
import top.daoha.domain.record.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.record.service.IRecordService;
import top.daoha.infrastructure.dao.ICycleRecordDao;
import top.daoha.infrastructure.dao.IDailyBehaviorLogDao;
import top.daoha.infrastructure.dao.IDailySymptomDao;
import top.daoha.infrastructure.dao.IUserInfoDao;
import top.daoha.infrastructure.dao.po.CycleRecord;
import top.daoha.infrastructure.dao.po.UserInfo;

import javax.annotation.Resource;


@Repository
public class RecordRepository implements IRecordService {

    @Resource
    private ICycleRecordDao cycleRecordDao;

    @Resource
    private IUserInfoDao userInfoDao;

    @Resource
    private IDailyBehaviorLogDao dailyBehaviorLogDao;

    @Resource
    private IDailySymptomDao dailySymptomDao;

    @Override
    public IndexInfoAggregate getIndexInfoAggregate(Long userId) {
        //查询用户信息
        UserInfo userInfo = userInfoDao.selectById(userId);
        //查询最新周期信息
        CycleRecord cycleRecord = cycleRecordDao.selectActiveByUserId(userId);
        return null;
    }
}
