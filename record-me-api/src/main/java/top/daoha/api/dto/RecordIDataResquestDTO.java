package top.daoha.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordIDataResquestDTO {
    //用户id
    private Long userId;
    //查看前几个月得数据
    private Integer month;
    //查看前几次的统计数据
    private Integer dataCount;

}
