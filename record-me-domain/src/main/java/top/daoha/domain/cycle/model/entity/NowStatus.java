package top.daoha.domain.cycle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NowStatus {

    // 用户 ID
    private Long userId;

    // 周期记录 ID
    private Long cycleId;

    // 开始时间
    private Date startDate;

    // 结束时间
    private Date endDate;

    // 是否当前最新周期
    private Integer isActive;
}
