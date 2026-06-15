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
public class RecordsResponseDTO {

    // 平均经期持续天数
    private Integer recordsCount;

    // 周期记录列表
    private List<CycleRecord> cycleRecords;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CycleRecord {

        // 周期记录 ID
        private Long cycleId;

        // 开始时间
        private Date startDate;

        // 结束时间
        private Date endDate;
    }
}
