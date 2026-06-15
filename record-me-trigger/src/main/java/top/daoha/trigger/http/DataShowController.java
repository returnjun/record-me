package top.daoha.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.daoha.api.IDataShowService;
import top.daoha.api.dto.DataShowRequestDTO;
import top.daoha.api.dto.DataShowResponseDTO;
import top.daoha.api.response.Response;
import top.daoha.domain.cycle.model.aggregate.CycleHistoryAggregate;
import top.daoha.domain.cycle.model.entity.CycleRecordEntity;
import top.daoha.domain.cycle.model.entity.UserEntity;
import top.daoha.domain.cycle.service.ICycleHistoryService;
import top.daoha.types.enums.ResponseCode;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/record/data/")
public class DataShowController implements IDataShowService {

    @Resource
    private ICycleHistoryService cycleHistoryService;

    @RequestMapping(value = "query_cycle_list", method = RequestMethod.POST)
    @Override
    public Response<DataShowResponseDTO> getIndexInfoAggregate(@RequestBody DataShowRequestDTO dataShowRequestDTO) throws Exception {
        try {
            if (dataShowRequestDTO.getUserId() == null) {
                return Response.<DataShowResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            Integer count = dataShowRequestDTO.getCount();
            if (count == null || count <= 0) {
                count = 5;
            }

            CycleHistoryAggregate aggregate = cycleHistoryService.getCycleRecordList(dataShowRequestDTO.getUserId(), count);
            UserEntity user = aggregate.getUser();

            List<DataShowResponseDTO.CycleRecord> cycleRecordList = new ArrayList<>();
            if (aggregate.getCycleList() != null) {
                for (CycleRecordEntity entity : aggregate.getCycleList()) {
                    DataShowResponseDTO.CycleRecord cr = new DataShowResponseDTO.CycleRecord();
                    cr.setCycleId(entity.getCycleId());
                    cr.setStartDate(entity.getStartDate());
                    cr.setEndDate(entity.getEndDate());
                    cycleRecordList.add(cr);
                }
            }

            DataShowResponseDTO resp = DataShowResponseDTO.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .avatar(user.getAvatar())
                    .avgCycleDays(user.getAvgCycleDays())
                    .avgPeriodDays(user.getAvgPeriodDays())
                    .status(0)
                    .cycleRecords(cycleRecordList)
                    .build();

            log.info("查询周期列表成功 userId={}, count={}", dataShowRequestDTO.getUserId(), count);
            return Response.<DataShowResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(resp)
                    .build();
        } catch (Exception e) {
            log.error("查询周期列表异常 userId={}", dataShowRequestDTO.getUserId(), e);
            return Response.<DataShowResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}
