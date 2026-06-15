package top.daoha.infrastructure.dao.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserInfo {

    private Long userId;
    private String username;
    private String password;

    // 头像
    private String avatar;

    // 电话
    private String phone;

    private Date birthday;
    private BigDecimal height;
    private BigDecimal weight;

    // 平均周期间隔天数
    private Integer avgCycleDays;

    // 平均经期持续天数
    private Integer avgPeriodDays;

    // 是否删除
    private Integer isDeleted;

    private Date createTime;
    private Date updateTime;
}
