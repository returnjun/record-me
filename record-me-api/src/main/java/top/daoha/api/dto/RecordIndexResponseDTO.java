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

    // 用户 ID
    private Long userId;

    // 周期记录 ID
    private Long cycleId;

    // 用户名
    private String userName;

    // 头像
    private String avatar;

    // 平均周期间隔天数
    private Integer avgCycleDays;

    // 平均经期持续天数
    private Integer avgPeriodDays;

    // 预测开始时间；生理期内时表示本次开始时间
    private Date PredictedStartTime;

    // 距离来潮还有几天，或已经来潮几天
    private String comeDays;

    // 预测结束时间；生理期外时表示上次结束时间
    private Date PredictedEndTime;

    // 距离结束还有几天，或已经结束几天
    private String goDays;

    // 当前状态：0 生理期内，1 生理期外
    private int status;

    // 事件标签
    private List<event> events;

    // 后续预留的 AI 健康建议
    private String aiSuggestion;

    public class event {

        // 事件名称
        String eventName;

        // 事件程度
        String eventProdure;
    }
}
