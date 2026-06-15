package top.daoha.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SymptomRequestDTO {
    //用户id
    private Long userId;
    //生理期id
    private Long cycleId;
    //每天状态id
    private Long recordId;
    //流量信息
    private Integer flowLevel;
    //疼痛信息
    private Integer painLevel;
    //心情
    private String mood;
    //其他记录
    private String notes;

}
