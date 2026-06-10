package top.daoha.domain.dataShow.service;

import org.springframework.stereotype.Service;
import top.daoha.domain.dataShow.adapter.repository.IDataShowRepository;
import top.daoha.domain.dataShow.model.aggregate.DataShowAggregate;

import javax.annotation.Resource;

@Service
public class DataShowService implements IDataShowService {

    @Resource
    private IDataShowRepository dataShowRepository;


    @Override
    public DataShowAggregate getCycleRecordList(Long userId, Integer count) {

        return dataShowRepository.getCycleRecordList(userId,count);
    }
}
