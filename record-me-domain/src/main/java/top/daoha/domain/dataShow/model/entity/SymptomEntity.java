package top.daoha.domain.dataShow.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SymptomEntity {
    //记录id
    private Long recordId;
    //周期id
    private Long cycleId;
    //用户id
    private Long userId;
    //记录时间
    private Date recordDate;
    //流量大小
    private Integer flowLevel;
    //痛经程度
    private Integer painLevel;
    //心情状况
    private String mood;
    //额外记录
    private String notes;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}
