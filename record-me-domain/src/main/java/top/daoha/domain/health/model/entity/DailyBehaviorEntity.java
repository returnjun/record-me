package top.daoha.domain.health.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DailyBehaviorEntity {

    private Long logId;
    private Long userId;
    private Date recordDate;
    private String behaviorsData;
    private Date createTime;
    private Date updateTime;

}
