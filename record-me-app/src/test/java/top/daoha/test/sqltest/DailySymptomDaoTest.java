package top.daoha.test.sqltest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoha.infrastructure.dao.IDailySymptomDao;
import top.daoha.infrastructure.dao.po.DailySymptom;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DailySymptomDaoTest {

    @Resource
    private IDailySymptomDao dailySymptomDao;

    @Test
    public void test_insert() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DailySymptom symptom = new DailySymptom();
        symptom.setCycleId(1L);
        symptom.setUserId(1L);
        try {
            symptom.setRecordDate(sdf.parse("2026-06-01"));
        } catch (Exception e) {
            log.error("症状日期解析失败", e);
        }
        symptom.setFlowLevel(2);
        symptom.setPainLevel(1);
        symptom.setMood("平静");
        symptom.setNotes("测试症状备注");

        int count = dailySymptomDao.insert(symptom);

        log.info("插入症状记录影响行数: {}, 新记录 recordId: {}", count, symptom.getRecordId());
    }

    @Test
    public void test_selectById() {
        DailySymptom symptom = dailySymptomDao.selectById(1L);

        log.info("按 ID 查询症状记录结果: {}", symptom);
    }

    @Test
    public void test_selectByCycleId() {
        List<DailySymptom> list = dailySymptomDao.selectByCycleId(1L);

        log.info("周期症状记录数量: {}", list.size());
        list.forEach(symptom -> log.info("症状记录: {}", symptom));
    }

    @Test
    public void test_selectByUserIdAndDate() {
        List<DailySymptom> list = dailySymptomDao.selectByUserIdAndDate(1L, "2026-06-01");

        log.info("按用户和日期查询症状数量: {}", list.size());
        list.forEach(symptom -> log.info("症状记录: {}", symptom));
    }

    @Test
    public void test_updateById() {
        DailySymptom symptom = new DailySymptom();
        symptom.setRecordId(1L);
        symptom.setFlowLevel(3);
        symptom.setPainLevel(2);
        symptom.setMood("疲惫");
        symptom.setNotes("更新后的症状备注");

        int count = dailySymptomDao.updateById(symptom);

        log.info("更新症状记录影响行数: {}", count);
    }

    @Test
    public void test_deleteById() {
        int count = dailySymptomDao.deleteById(1L);

        log.info("删除症状记录影响行数: {}", count);
    }

}
