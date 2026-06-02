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
public class RecordIndexResponseDTO {

    //用户id
    private Long userId;
    //头像
    private String avatar;
    //平均间隔时间
    private Integer avgCycleDays;
    //平均持续时间
    private Integer avgPeriodDays;
    //预测开始时间，生理期内就是开始得时间
    private Date PredictedStartTime;
    //几天前或几天后来
    private String comeDays;
    //预测结束时间，生理期外就是上次结束时间
    private Date PredictedEndTime;
    //几天后或几天前走
    private String goDays;
    //记录当前状态 0：在生理期内，1：不在生理期内
    private int status;
    //发生了什么记录标签
    List<event> events;
    //后期的ai健康分析
    private String aiSuggestion;

    public class event {
        //事件名称
        String eventName;
        //事件程度
        String eventProdure;
    }
}
