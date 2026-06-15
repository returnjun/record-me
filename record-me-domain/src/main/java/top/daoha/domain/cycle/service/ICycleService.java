package top.daoha.domain.cycle.service;

import top.daoha.domain.cycle.model.aggregate.IndexInfoAggregate;

public interface ICycleService {

    IndexInfoAggregate getIndexInfoAggregate(Long userId);

}
