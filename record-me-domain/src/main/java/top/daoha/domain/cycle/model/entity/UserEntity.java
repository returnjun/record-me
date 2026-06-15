package top.daoha.domain.cycle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    // 用户 ID
    private Long userId;

    // 用户名
    private String userName;

    // 头像
    private String avatar;

    // 平均周期间隔天数
    private Integer avgCycleDays;

    // 平均经期持续天数
    private Integer avgPeriodDays;
}
