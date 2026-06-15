package top.daoha.infrastructure.dao.po;

import lombok.Data;

import java.util.Date;

@Data
public class DailyBehaviorLog {

    // 行为日志 ID
    private Long logId;

    // 用户 ID
    private Long userId;

    // 记录日期
    private Date recordDate;

    // 当日行为数据
    private String behaviorsData;

    // 是否删除
    private Integer isDeleted;

    private Date createTime;
    private Date updateTime;
}
