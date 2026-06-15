package top.daoha.domain.health.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DailySymptomEntity {

    // 症状记录 ID
    private Long recordId;

    // 周期记录 ID
    private Long cycleId;

    // 用户 ID
    private Long userId;

    // 记录日期
    private Date recordDate;

    // 流量大小
    private Integer flowLevel;

    // 痛经程度
    private Integer painLevel;

    // 心情状态
    private String mood;

    // 额外备注
    private String notes;

    // 创建时间
    private Date createTime;

    // 更新时间
    private Date updateTime;
}
