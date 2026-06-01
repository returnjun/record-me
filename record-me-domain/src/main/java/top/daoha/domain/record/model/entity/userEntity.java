package top.daoha.domain.record.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class userEntity {
    //用户id
    private Long userId;
    //头像
    private String avatar;
    //平均间隔时间
    private Integer avgCycleDays;
    //平均持续时间
    private Integer avgPeriodDays;
}
