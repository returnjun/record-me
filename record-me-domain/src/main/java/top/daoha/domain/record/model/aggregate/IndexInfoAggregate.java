package top.daoha.domain.record.model.aggregate;

import lombok.*;
import top.daoha.domain.record.model.entity.nowStatus;
import top.daoha.domain.record.model.entity.userEntity;
import top.daoha.types.enums.RecordStatusEnumVO;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexInfoAggregate {
    private nowStatus nowStatus;
    private userEntity user;
    private Date PredictedStartTime;
    private Date PredictedEndTime;
    private RecordStatusEnumVO status;

    void addPredictedTime() {
        // 获取当前时间的毫秒数
        long nowTime = System.currentTimeMillis();
        // 提前计算好备用时间：当前时间 + 12小时
        long fallbackTime = nowTime + 12L * 3600 * 1000;

        // 修复：如果没有结束时间 (== null)，说明在生理期中
        if (null == nowStatus.getEndDate()) {
            this.status = RecordStatusEnumVO.COMING;

            // 计算预测结束时间
            long calculatedEndTime = nowStatus.getStartDate().getTime()
                    + user.getAvgCycleDays() * 24L * 3600 * 1000;

            // 校验：如果算出来的时间早于或等于当前时间，则强制设置为 当前+12小时
            if (calculatedEndTime <= fallbackTime) {
                calculatedEndTime = fallbackTime;
            }

            this.PredictedEndTime = new Date(calculatedEndTime);

        } else {
            // 修复：如果有结束时间 (!= null)，说明上一个生理期结束
            this.status = RecordStatusEnumVO.COMMON;

            // 计算下一次预测开始时间
            long calculatedStartTime = nowStatus.getEndDate().getTime()
                    + user.getAvgPeriodDays() * 24L * 3600 * 1000;

            // 校验：如果算出来的时间早于或等于当前时间，则强制设置为 当前+12小时
            if (calculatedStartTime <= fallbackTime) {
                calculatedStartTime = fallbackTime;
            }

            this.PredictedStartTime = new Date(calculatedStartTime);
        }
    }

}
