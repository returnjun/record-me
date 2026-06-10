package top.daoha.domain.dataShow.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.daoha.domain.dataShow.model.entity.CycleRecordEntity;
import top.daoha.domain.dataShow.model.entity.UserEntity;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataShowAggregate {
    private List<CycleRecordEntity> cycleList;
    private UserEntity user;
}
