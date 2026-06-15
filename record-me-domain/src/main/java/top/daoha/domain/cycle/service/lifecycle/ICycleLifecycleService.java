package top.daoha.domain.cycle.service.lifecycle;

public interface ICycleLifecycleService {

    Boolean overCycleRecord(Long userId);

    Boolean startCycleRecord(Long userId);

}
