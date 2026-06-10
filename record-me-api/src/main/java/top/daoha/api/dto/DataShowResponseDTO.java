package top.daoha.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataShowResponseDTO {

    //用户id
    private Long userId;
    //用户名
    private String userName;
    //头像
    private String avatar;
    //平均间隔时间
    private Integer avgCycleDays;
    //平均持续时间
    private Integer avgPeriodDays;
    //记录当前状态 0：在生理期内，1：不在生理期内
    private int status;
    //发生了什么记录标签
    List<CycleRecord> cycleRecords;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CycleRecord {
        //周期记录id
        private Long cycleId;
        //开始时间
        private Date startDate;
        //结束时间
        private Date endDate;
    }
}
