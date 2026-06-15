package top.daoha.domain.cycle.adapter.repository;

import top.daoha.domain.cycle.model.aggregate.IndexInfoAggregate;

public interface ICycleRepository {

    IndexInfoAggregate getIndexInfoAggregate(Long userId);

    Boolean overCycleRecord(Long userId);

    void updateAvgData(Long userId);

    boolean closeCycleRecord(Long userId);

    Boolean startCycleRecord(Long userId);

}
