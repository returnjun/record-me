package top.daoha.test.sqltest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoha.infrastructure.dao.ICycleRecordDao;
import top.daoha.infrastructure.dao.po.CycleRecord;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CycleRecordDaoTest {

    @Resource
    private ICycleRecordDao cycleRecordDao;

    @Test
    public void test_insert() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        CycleRecord record = new CycleRecord();
        record.setUserId(1L);
        try {
            record.setStartDate(sdf.parse("2026-06-01"));
            record.setEndDate(sdf.parse("2026-06-05"));
        } catch (Exception e) {
            log.error("周期日期解析失败", e);
        }
        record.setIsActive(1);

        int count = cycleRecordDao.insert(record);

        log.info("插入周期记录影响行数: {}, 新周期 cycleId: {}", count, record.getCycleId());
    }

    @Test
    public void test_selectById() {
        CycleRecord record = cycleRecordDao.selectById(1L);

        log.info("按 ID 查询周期记录结果: {}", record);
    }

    @Test
    public void test_selectByUserId() {
        List<CycleRecord> list = cycleRecordDao.selectByUserId(1L);

        log.info("用户周期记录数量: {}", list.size());
        list.forEach(record -> log.info("周期记录: {}", record));
    }

    @Test
    public void test_selectActiveByUserId() {
        CycleRecord record = cycleRecordDao.selectActiveByUserId(1L);

        log.info("当前活跃周期记录: {}", record);
    }

    @Test
    public void test_updateById() {
        CycleRecord record = new CycleRecord();
        record.setCycleId(1L);
        record.setEndDate(new java.util.Date());
        record.setIsActive(0);

        int count = cycleRecordDao.updateById(record);

        log.info("更新周期记录影响行数: {}", count);
    }

    @Test
    public void test_deleteById() {
        int count = cycleRecordDao.deleteById(1L);

        log.info("删除周期记录影响行数: {}", count);
    }

}
