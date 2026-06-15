package top.daoha.domain.cycle.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.daoha.domain.cycle.model.entity.NowStatus;
import top.daoha.domain.cycle.model.entity.UserEntity;
import top.daoha.types.enums.RecordStatusEnumVO;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexInfoAggregate {

    private NowStatus nowStatus;
    private UserEntity user;
    private Date PredictedStartTime;
    private Date PredictedEndTime;
    private RecordStatusEnumVO status;

    public void addPredictedTime() {
        // 当前时间毫秒数
        long nowTime = System.currentTimeMillis();
        // 兜底时间：当前时间后 12 小时
        long fallbackTime = nowTime + 12L * 3600 * 1000;

        // 没有结束时间，说明当前仍在生理期内
        if (null == nowStatus.getEndDate()) {
            this.status = RecordStatusEnumVO.COMING;

            // 根据开始时间和平均经期天数预测结束时间
            long calculatedEndTime = nowStatus.getStartDate().getTime()
                    + user.getAvgPeriodDays() * 24L * 3600 * 1000;

            // 如果预测时间已经过期，强制设置为当前时间后 12 小时
            if (calculatedEndTime <= fallbackTime) {
                calculatedEndTime = fallbackTime;
            }
            this.PredictedStartTime = nowStatus.getStartDate();
            this.PredictedEndTime = new Date(calculatedEndTime);

        } else {
            // 有结束时间，说明上一次生理期已经结束
            this.status = RecordStatusEnumVO.COMMON;

            // 根据上次结束时间和平均周期天数预测下次开始时间
            long calculatedStartTime = nowStatus.getEndDate().getTime()
                    + user.getAvgCycleDays() * 24L * 3600 * 1000;

            // 如果预测时间已经过期，强制设置为当前时间后 12 小时
            if (calculatedStartTime <= fallbackTime) {
                calculatedStartTime = fallbackTime;
            }
            this.PredictedEndTime = nowStatus.getEndDate();
            this.PredictedStartTime = new Date(calculatedStartTime);
        }
    }
}
