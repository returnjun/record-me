package top.daoha.test.sqltest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoha.infrastructure.dao.IDailyBehaviorLogDao;
import top.daoha.infrastructure.dao.po.DailyBehaviorLog;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DailyBehaviorLogDaoTest {

    @Resource
    private IDailyBehaviorLogDao dailyBehaviorLogDao;

    @Test
    public void test_insert() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DailyBehaviorLog record = new DailyBehaviorLog();
        record.setUserId(1L);
        try {
            record.setRecordDate(sdf.parse("2026-06-01"));
        } catch (Exception e) {
            log.error("行为记录日期解析失败", e);
        }
        record.setBehaviorsData("[{\"type\":\"运动\",\"duration\":30},{\"type\":\"喝水\",\"note\":\"已完成\"}]");

        int count = dailyBehaviorLogDao.insert(record);

        log.info("插入行为日志影响行数: {}, 新日志 logId: {}", count, record.getLogId());
    }

    @Test
    public void test_selectById() {
        DailyBehaviorLog record = dailyBehaviorLogDao.selectById(1L);

        log.info("按 ID 查询行为日志结果: {}", record);
    }

    @Test
    public void test_selectByUserId() {
        List<DailyBehaviorLog> list = dailyBehaviorLogDao.selectByUserId(1L);

        log.info("用户行为日志数量: {}", list.size());
        list.forEach(record -> log.info("行为日志: {}", record));
    }

    @Test
    public void test_selectByUserIdAndDate() {
        DailyBehaviorLog record = dailyBehaviorLogDao.selectByUserIdAndDate(1L, "2026-06-01");

        log.info("按用户和日期查询行为日志结果: {}", record);
    }

    @Test
    public void test_updateById() {
        DailyBehaviorLog record = new DailyBehaviorLog();
        record.setLogId(1L);
        record.setBehaviorsData("[{\"type\":\"运动\",\"duration\":60},{\"type\":\"喝水\",\"note\":\"已更新\"}]");

        int count = dailyBehaviorLogDao.updateById(record);

        log.info("更新行为日志影响行数: {}", count);
    }

    @Test
    public void test_deleteById() {
        int count = dailyBehaviorLogDao.deleteById(1L);

        log.info("删除行为日志影响行数: {}", count);
    }

}
