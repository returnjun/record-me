package top.daoha.domain.dataShow.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleRecordEntity {
    //用户id
    private Long userId;
    //记录id
    private Long cycleId;
    //开始时间
    private Date startDate;
    //结束时间
    private Date endDate;
    //是否为当前最新
    private Integer isActive;
}
