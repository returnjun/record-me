package top.daoha.domain.dataShow.service;

import top.daoha.domain.dataShow.model.aggregate.DataShowAggregate;
import top.daoha.domain.dataShow.model.entity.CycleRecordEntity;

import java.util.List;

public interface IDataShowService {
    //1 查询数据查看界面的数据
    public DataShowAggregate getCycleRecordList(Long userId, Integer count);



}
