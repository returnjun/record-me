package top.daoha.test.domain.cycle;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoha.domain.cycle.service.lifecycle.ICycleLifecycleService;
import top.daoha.infrastructure.dao.ICycleRecordDao;
import top.daoha.infrastructure.dao.IUserInfoDao;
import top.daoha.infrastructure.dao.po.CycleRecord;
import top.daoha.infrastructure.dao.po.UserInfo;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateRecordServiceTest {

    @Resource
    private ICycleLifecycleService cycleLifecycleService;

    @Resource
    private ICycleRecordDao cycleRecordDao;

    @Resource
    private IUserInfoDao userInfoDao;

    @Test
    public void test_overCycleRecord_alice() {
        CycleRecord beforeActive = cycleRecordDao.selectActiveByUserId(1L);
        assertNotNull("Alice 应该存在一个活跃周期记录", beforeActive);
        assertNull("结束周期前 endDate 应该为空", beforeActive.getEndDate());
        log.info("结束前 Alice 活跃周期: cycleId={}, startDate={}, endDate={}",
                beforeActive.getCycleId(), beforeActive.getStartDate(), beforeActive.getEndDate());

        Boolean result = cycleLifecycleService.overCycleRecord(1L);

        assertTrue("结束周期应该返回 true", result);

        CycleRecord endedRecord = cycleRecordDao.selectById(beforeActive.getCycleId());
        assertNotNull("结束后的周期记录应该仍可查询到", endedRecord);
        assertNotNull("结束周期后 endDate 应该有值", endedRecord.getEndDate());
        assertTrue("结束周期后 isActive 应该为 0",
                endedRecord.getIsActive() != null && endedRecord.getIsActive() == 0);
        log.info("结束后 Alice 周期: cycleId={}, endDate={}, isActive={}",
                endedRecord.getCycleId(), endedRecord.getEndDate(), endedRecord.getIsActive());

        UserInfo alice = userInfoDao.selectById(1L);
        log.info("Alice 用户平均周期: avgCycleDays={}, avgPeriodDays={}",
                alice.getAvgCycleDays(), alice.getAvgPeriodDays());
    }

    @Test
    public void test_startCycleRecord_alice() {
        CycleRecord beforeActive = cycleRecordDao.selectActiveByUserId(1L);
        log.info("开始新周期前 Alice 活跃周期: {}", beforeActive);

        Boolean result = cycleLifecycleService.startCycleRecord(1L);

        assertTrue("开始新周期应该返回 true", result);

        if (beforeActive != null) {
            CycleRecord closedRecord = cycleRecordDao.selectById(beforeActive.getCycleId());
            assertTrue("旧活跃周期的 isActive 应该为 0",
                    closedRecord.getIsActive() != null && closedRecord.getIsActive() == 0);
            log.info("已关闭旧周期: cycleId={}, isActive={}",
                    closedRecord.getCycleId(), closedRecord.getIsActive());
        }

        CycleRecord newActive = cycleRecordDao.selectActiveByUserId(1L);
        assertNotNull("开始新周期后应该存在新的活跃周期", newActive);
        assertNotNull("新周期 startDate 应该有值", newActive.getStartDate());
        assertNull("新周期 endDate 应该为空", newActive.getEndDate());
        assertTrue("新周期 isActive 应该为 1",
                newActive.getIsActive() != null && newActive.getIsActive() == 1);
        log.info("新活跃周期: cycleId={}, startDate={}, isActive={}",
                newActive.getCycleId(), newActive.getStartDate(), newActive.getIsActive());
    }

    @Test
    public void test_overThenStart_full_cycle_alice() {
        Boolean overResult = cycleLifecycleService.overCycleRecord(1L);
        log.info("结束 Alice 当前周期结果: {}", overResult);

        CycleRecord afterOver = cycleRecordDao.selectActiveByUserId(1L);
        log.info("结束后活跃周期查询结果: {}", afterOver);

        Boolean startResult = cycleLifecycleService.startCycleRecord(1L);
        log.info("重新开始 Alice 新周期结果: {}", startResult);

        CycleRecord newActive = cycleRecordDao.selectActiveByUserId(1L);
        assertNotNull("重新开始后应该存在新的活跃周期", newActive);
        assertNull("新周期 endDate 应该为空", newActive.getEndDate());
        assertTrue("新周期 isActive 应该为 1",
                newActive.getIsActive() != null && newActive.getIsActive() == 1);
        log.info("重新开始后的新周期: cycleId={}, startDate={}, endDate={}, isActive={}",
                newActive.getCycleId(), newActive.getStartDate(),
                newActive.getEndDate(), newActive.getIsActive());
    }

    @Test
    public void test_overCycleRecord_bella() {
        CycleRecord beforeActive = cycleRecordDao.selectActiveByUserId(2L);
        assertNotNull("Bella 应该存在一个活跃周期记录", beforeActive);
        log.info("结束前 Bella 活跃周期: cycleId={}, startDate={}, endDate={}",
                beforeActive.getCycleId(), beforeActive.getStartDate(), beforeActive.getEndDate());

        Boolean result = cycleLifecycleService.overCycleRecord(2L);

        assertTrue("结束周期应该返回 true", result);

        CycleRecord endedRecord = cycleRecordDao.selectById(beforeActive.getCycleId());
        assertNotNull("结束周期后 endDate 应该有值", endedRecord.getEndDate());
        log.info("结束后 Bella 周期: cycleId={}, endDate={}, isActive={}",
                endedRecord.getCycleId(), endedRecord.getEndDate(), endedRecord.getIsActive());
    }

}
