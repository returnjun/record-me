package top.daoha.infrastructure.adapter.repository;

import org.springframework.stereotype.Repository;
import top.daoha.domain.record.adapter.repository.IRecordRepository;
import top.daoha.domain.record.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.record.model.entity.NowStatus;
import top.daoha.domain.record.model.entity.UserEntity;
import top.daoha.infrastructure.dao.ICycleRecordDao;
import top.daoha.infrastructure.dao.IDailyBehaviorLogDao;
import top.daoha.infrastructure.dao.IDailySymptomDao;
import top.daoha.infrastructure.dao.IUserInfoDao;
import top.daoha.infrastructure.dao.po.CycleRecord;
import top.daoha.infrastructure.dao.po.UserInfo;

import javax.annotation.Resource;


@Repository
public class RecordRepository implements IRecordRepository {

    @Resource
    private ICycleRecordDao cycleRecordDao;

    @Resource
    private IUserInfoDao userInfoDao;

    @Override
    public IndexInfoAggregate getIndexInfoAggregate(Long userId) {
        //查询用户信息
        UserInfo userInfo = userInfoDao.selectById(userId);
        if (userInfo == null) {
            throw new RuntimeException("该用户不存在，请检查用户ID是否正确");
            // 注：实际项目中建议替换为你们的自定义业务异常，例如 throw new BusinessException("该用户不存在");
        }
        //查询最新周期信息
        CycleRecord cycleRecord = cycleRecordDao.selectActiveByUserId(userId);
        if (cycleRecord == null) {
            throw new RuntimeException("未查询到该用户的最新周期信息");
        }
        boolean isUsernameBlank = userInfo.getUsername() == null || userInfo.getUsername().trim().isEmpty();
        boolean isPasswordBlank = userInfo.getPassword() == null || userInfo.getPassword().trim().isEmpty();
        boolean isCycleDaysNull = userInfo.getAvgCycleDays() == null;
        boolean isPeriodDaysNull = userInfo.getAvgPeriodDays() == null;

        // 如果任何一个必填项为空，则抛出异常或返回错误信息
        if (isUsernameBlank || isPasswordBlank || isCycleDaysNull || isPeriodDaysNull) {
            throw new RuntimeException("必填信息不完整：用户名、密码、平均间隔或持续时间不能为空");
        }
        IndexInfoAggregate indexInfoAggregate = IndexInfoAggregate.builder()
                .user(UserEntity.builder()
                        .userId(userInfo.getUserId())
                        .userName(userInfo.getUsername())
                        .avatar(userInfo.getAvatar())
                        .avgCycleDays(userInfo.getAvgCycleDays())
                        .avgPeriodDays(userInfo.getAvgPeriodDays())
                        .build())
                .nowStatus(NowStatus.builder()
                        .userId(cycleRecord.getUserId())
                        .isActive(cycleRecord.getIsActive())
                        .startDate(cycleRecord.getStartDate())
                        .endDate(cycleRecord.getEndDate())
                        .build())
                .build();
        indexInfoAggregate.addPredictedTime();

        return indexInfoAggregate;
    }

    @Override
    public Boolean overCycleRecord(Long userId) {
        int count = cycleRecordDao.overByUserId(userId);
        return count > 0;
    }

    @Override
    public void updateAvgData(Long userId) {
        java.util.List<CycleRecord> completedList = cycleRecordDao.selectCompletedByUserId(userId);
        if (completedList == null || completedList.size() < 2) {
            return;
        }
        long totalPeriodDays = 0;
        long totalCycleDays = 0;
        for (int i = 0; i < completedList.size(); i++) {
            CycleRecord cr = completedList.get(i);
            long periodDays = (cr.getEndDate().getTime() - cr.getStartDate().getTime()) / (24L * 3600 * 1000);
            totalPeriodDays += periodDays;
            if (i > 0) {
                CycleRecord prev = completedList.get(i - 1);
                long cycleDays = (cr.getStartDate().getTime() - prev.getStartDate().getTime()) / (24L * 3600 * 1000);
                totalCycleDays += cycleDays;
            }
        }
        int avgPeriodDays = (int) (totalPeriodDays / completedList.size());
        int avgCycleDays = (int) (totalCycleDays / (completedList.size() - 1));

        UserInfo updateUser = new UserInfo();
        updateUser.setUserId(userId);
        updateUser.setAvgCycleDays(avgCycleDays);
        updateUser.setAvgPeriodDays(avgPeriodDays);
        userInfoDao.updateById(updateUser);
    }

    @Override
    public boolean closeCycleRecord(Long userId) {
        int count = cycleRecordDao.closeByUserId(userId);
        return count > 0;
    }

    @Override
    public Boolean startCycleRecord(Long userId) {
        CycleRecord newRecord = new CycleRecord();
        newRecord.setUserId(userId);
        newRecord.setStartDate(new java.util.Date());
        newRecord.setIsActive(1);
        int count = cycleRecordDao.insert(newRecord);
        return count > 0;
    }
}
