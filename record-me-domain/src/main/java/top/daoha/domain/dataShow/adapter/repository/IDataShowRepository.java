package top.daoha.domain.dataShow.adapter.repository;

import top.daoha.domain.dataShow.model.aggregate.DataShowAggregate;
import top.daoha.domain.dataShow.model.entity.CycleRecordEntity;
import java.util.List;

public interface IDataShowRepository {
    //查询数据界面的最新的count条数据
    DataShowAggregate getCycleRecordList(Long userId, Integer count);
}
