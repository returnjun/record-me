package top.daoha.infrastructure.dao.po;

import lombok.Data;

import java.util.Date;

@Data
public class CycleRecord {

    private Long cycleId;
    private Long userId;
    private Date startDate;
    private Date endDate;
    private Integer isActive;
    private Integer isDeleted;
    private Date createTime;
    private Date updateTime;

}
