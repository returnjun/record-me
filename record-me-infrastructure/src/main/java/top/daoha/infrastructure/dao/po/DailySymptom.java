package top.daoha.infrastructure.dao.po;

import lombok.Data;

import java.util.Date;

@Data
public class DailySymptom {

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

    // 是否删除
    private Integer isDeleted;

    private Date createTime;
    private Date updateTime;
}
