package top.daoha.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.daoha.api.IRecordIndexService;
import top.daoha.api.dto.RecordIndexRequestDTO;
import top.daoha.api.dto.RecordIndexResponseDTO;
import top.daoha.api.response.Response;
import top.daoha.domain.record.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.record.service.IRecordService;
import top.daoha.domain.record.service.change.IUpdateRecordService;
import top.daoha.types.enums.RecordStatusEnumVO;
import top.daoha.types.enums.ResponseCode;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/record/index/")
public class RecordIndexController implements IRecordIndexService {

    @Resource
    private IRecordService recordService;

    @Resource
    private IUpdateRecordService updateRecordService;

    @RequestMapping(value = "query_user_info", method = RequestMethod.POST)
    @Override
    public Response<RecordIndexResponseDTO> getIndexInfoAggregate(@RequestBody RecordIndexRequestDTO recordIndexRequestDTO) {
        try {
            if (recordIndexRequestDTO.getUserId() == null) {
                return Response.<RecordIndexResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            IndexInfoAggregate aggregate = recordService.getIndexInfoAggregate(recordIndexRequestDTO.getUserId());
            aggregate.addPredictedTime();

            // 获取当前日期的 LocalDate (抹平了时分秒的影响，专门用于计算天数)
            LocalDate today = LocalDate.now();
            String comeDays = "";
            String goDays = "";

            // 根据不同状态，计算几天前/几天后
            if (RecordStatusEnumVO.COMING == aggregate.getStatus()) {
                // 【情况1：在生理期内】
                // 预测开始时间(PredictedStartTime) = 实际哪天来的 (过去)
                // 预测结束时间(PredictedEndTime) = 估计哪天走 (未来)
                if (aggregate.getPredictedStartTime() != null) {
                    LocalDate startDate = aggregate.getPredictedStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long daysAgo = ChronoUnit.DAYS.between(startDate, today);
                    comeDays = daysAgo == 0 ? "今天" : Math.abs(daysAgo) + "天前";
                }

                if (aggregate.getPredictedEndTime() != null) {
                    LocalDate endDate = aggregate.getPredictedEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long daysLater = ChronoUnit.DAYS.between(today, endDate);
                    goDays = daysLater == 0 ? "今天" : Math.abs(daysLater) + "天后";
                }
            } else {
                // 【情况2：不在生理期内】
                // 预测开始时间(PredictedStartTime) = 估计下次哪天来 (未来)
                // 预测结束时间(PredictedEndTime) = 上次哪天走的 (过去)
                if (aggregate.getPredictedStartTime() != null) {
                    LocalDate nextStartDate = aggregate.getPredictedStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long daysLater = ChronoUnit.DAYS.between(today, nextStartDate);
                    comeDays = daysLater == 0 ? "今天" : Math.abs(daysLater) + "天后";
                }

                if (aggregate.getPredictedEndTime() != null) {
                    LocalDate lastEndDate = aggregate.getPredictedEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long daysAgo = ChronoUnit.DAYS.between(lastEndDate, today);
                    goDays = daysAgo == 0 ? "今天" : Math.abs(daysAgo) + "天前";
                }
            }

            // 构建返回的 DTO
            RecordIndexResponseDTO dto = RecordIndexResponseDTO.builder()
                    .userId(aggregate.getUser().getUserId())
                    .userName(aggregate.getUser().getUserName())
                    .avatar(aggregate.getUser().getAvatar())
                    .avgCycleDays(aggregate.getUser().getAvgCycleDays())
                    .avgPeriodDays(aggregate.getUser().getAvgPeriodDays())
                    .PredictedStartTime(aggregate.getPredictedStartTime()) // 保持你DTO中首字母大写的命名
                    .PredictedEndTime(aggregate.getPredictedEndTime())
                    .comeDays(comeDays)
                    .goDays(goDays)
                    .status(aggregate.getStatus() != null ? aggregate.getStatus().getCode() : 1)
                    .events(new ArrayList<>()) // 以后再扩展
                    .aiSuggestion(null)        // 以后再扩展
                    .build();

            log.info("查询首页信息成功 userId={}", recordIndexRequestDTO.getUserId());
            return Response.<RecordIndexResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(dto)
                    .build();

        } catch (Exception e) {
            log.error("查询首页信息失败 userId={}", recordIndexRequestDTO.getUserId(), e.getMessage());
            return Response.<RecordIndexResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "over_cycle_record", method = RequestMethod.POST)
    @Override
    public Response<Boolean> overCycleRecord(@RequestBody RecordIndexRequestDTO recordIndexRequestDTO) {
        try {
            if (recordIndexRequestDTO.getUserId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            Boolean result = updateRecordService.overCycleRecord(recordIndexRequestDTO.getUserId());

            if (Boolean.TRUE.equals(result)) {
                log.info("结束周期成功 userId={}", recordIndexRequestDTO.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(true)
                        .build();
            } else {
                log.warn("结束周期失败，更新记录为0 userId={}", recordIndexRequestDTO.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.UPDATE_ZERO.getCode())
                        .info(ResponseCode.UPDATE_ZERO.getInfo())
                        .data(false)
                        .build();
            }
        } catch (Exception e) {
            log.error("结束周期异常 userId={}", recordIndexRequestDTO.getUserId(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "start_cycle_record", method = RequestMethod.POST)
    @Override
    public Response<Boolean> startCycleRecord(@RequestBody RecordIndexRequestDTO recordIndexRequestDTO) {
        try {
            if (recordIndexRequestDTO.getUserId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            Boolean result = updateRecordService.startCycleRecord(recordIndexRequestDTO.getUserId());

            if (Boolean.TRUE.equals(result)) {
                log.info("开始新周期成功 userId={}", recordIndexRequestDTO.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(true)
                        .build();
            } else {
                log.warn("开始新周期失败，更新记录为0 userId={}", recordIndexRequestDTO.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.UPDATE_ZERO.getCode())
                        .info(ResponseCode.UPDATE_ZERO.getInfo())
                        .data(false)
                        .build();
            }
        } catch (Exception e) {
            log.error("开始新周期异常 userId={}", recordIndexRequestDTO.getUserId(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}
