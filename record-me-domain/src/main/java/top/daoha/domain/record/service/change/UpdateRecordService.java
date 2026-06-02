package top.daoha.domain.record.service.change;

import org.springframework.stereotype.Service;
import top.daoha.domain.record.adapter.repository.IRecordRepository;

import javax.annotation.Resource;

@Service
public class UpdateRecordService implements IUpdateRecordService {

    @Resource
    private IRecordRepository recordRepository;


    @Override
    public Boolean overCycleRecord(Long userId) {
        //这个函数是结束一个周期，当用户点击走的时候
        //修改最新状态的数据
        boolean isSuccess = recordRepository.overCycleRecord(userId);
        if(!isSuccess) return  false;
        //有了这个新的数据后我们获得了最新的一个周期数据需要更新平均时间和间隔时间
        recordRepository.updateAvgData(userId);
        return true;
    }

    @Override
    public Boolean startCycleRecord(Long userId) {
        //这个函数是开始一个周期，当用户点击开始的时候
        //关闭之前最新的那个周期
        boolean isSuccess = recordRepository.closeCycleRecord(userId);
        if(!isSuccess){
            return false;
        }
        //插入一个新的最新周期
        return recordRepository.startCycleRecord(userId);
    }
}
