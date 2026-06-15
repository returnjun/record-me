package top.daoha.test.domain.cycle;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoha.domain.cycle.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.cycle.service.ICycleService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecordServiceTest {

    @Resource
    private ICycleService cycleService;

    @Test
    public void test_getIndexInfoAggregate_alice() {
        IndexInfoAggregate aggregate = cycleService.getIndexInfoAggregate(1L);

        log.info("Alice 首页周期信息:");
        log.info("  用户档案: {}", aggregate.getUser());
        log.info("  当前周期状态: {}", aggregate.getNowStatus());
        log.info("  状态提示: {}", aggregate.getStatus().getInfo());
        log.info("  预测结束时间: {}", aggregate.getPredictedEndTime());
    }

    @Test
    public void test_getIndexInfoAggregate_bella() {
        IndexInfoAggregate aggregate = cycleService.getIndexInfoAggregate(2L);

        log.info("Bella 首页周期信息:");
        log.info("  用户档案: {}", aggregate.getUser());
        log.info("  当前周期状态: {}", aggregate.getNowStatus());
        log.info("  状态提示: {}", aggregate.getStatus().getInfo());
        log.info("  预测结束时间: {}", aggregate.getPredictedEndTime());
    }

    @Test
    public void test_predictedEndTime_calculation() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        IndexInfoAggregate aliceAggregate = cycleService.getIndexInfoAggregate(1L);
        long aliceStartTime = aliceAggregate.getNowStatus().getStartDate().getTime();
        long aliceExpectedEnd = aliceStartTime + 28L * 24 * 3600 * 1000;
        assertEquals("Alice 的预测结束日期应该等于 startDate + 28 天",
                sdf.format(new java.util.Date(aliceExpectedEnd)),
                sdf.format(aliceAggregate.getPredictedEndTime()));

        IndexInfoAggregate bellaAggregate = cycleService.getIndexInfoAggregate(2L);
        long bellaStartTime = bellaAggregate.getNowStatus().getStartDate().getTime();
        long bellaExpectedEnd = bellaStartTime + 30L * 24 * 3600 * 1000;
        assertEquals("Bella 的预测结束日期应该等于 startDate + 30 天",
                sdf.format(new java.util.Date(bellaExpectedEnd)),
                sdf.format(bellaAggregate.getPredictedEndTime()));

        log.info("Alice 预测结束日期: {}", sdf.format(aliceAggregate.getPredictedEndTime()));
        log.info("Bella 预测结束日期: {}", sdf.format(bellaAggregate.getPredictedEndTime()));
    }

}
