package top.daoha.domain.cycle.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.daoha.domain.cycle.model.entity.CycleRecordEntity;
import top.daoha.domain.cycle.model.entity.UserEntity;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleHistoryAggregate {

    private List<CycleRecordEntity> cycleList;

    private UserEntity user;

}
