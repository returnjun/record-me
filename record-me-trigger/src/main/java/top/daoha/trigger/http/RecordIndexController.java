package top.daoha.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.daoha.api.IRecordIndexService;
import top.daoha.api.dto.UserIdRequestDTO;
import top.daoha.api.dto.RecordIndexResponseDTO;
import top.daoha.api.dto.SymptomRequestDTO;
import top.daoha.api.dto.SymptomResponseDTO;
import top.daoha.api.response.Response;
import top.daoha.domain.cycle.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.cycle.service.ICycleService;
import top.daoha.domain.cycle.service.lifecycle.ICycleLifecycleService;
import top.daoha.domain.health.model.entity.DailySymptomEntity;
import top.daoha.domain.health.service.IHealthTrackService;
import top.daoha.types.enums.RecordStatusEnumVO;
import top.daoha.types.enums.ResponseCode;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/record/index/")
public class RecordIndexController implements IRecordIndexService {

    @Resource
    private ICycleService cycleService;

    @Resource
    private ICycleLifecycleService cycleLifecycleService;

    @Resource
    private IHealthTrackService healthTrackService;

    @RequestMapping(value = "query_user_info", method = RequestMethod.POST)
    @Override
    public Response<RecordIndexResponseDTO> getIndexInfoAggregate(@RequestBody UserIdRequestDTO userIdRequestDTO) {
        try {
            if (userIdRequestDTO.getUserId() == null) {
                return Response.<RecordIndexResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            IndexInfoAggregate aggregate = cycleService.getIndexInfoAggregate(userIdRequestDTO.getUserId());
            aggregate.addPredictedTime();

            // 使用 LocalDate 抹平时分秒影响，只计算自然日间隔
            LocalDate today = LocalDate.now();
            String comeDays = "";
            String goDays = "";

            if (RecordStatusEnumVO.COMING == aggregate.getStatus()) {
                // 生理期内：开始时间是实际来潮日期，结束时间是预测结束日期
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
                // 生理期外：开始时间是预测下次来潮日期，结束时间是上次结束日期
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

            RecordIndexResponseDTO dto = RecordIndexResponseDTO.builder()
                    .userId(aggregate.getUser().getUserId())
                    .cycleId(aggregate.getNowStatus().getCycleId())
                    .avatar(aggregate.getUser().getAvatar())
                    .avgCycleDays(aggregate.getUser().getAvgCycleDays())
                    .avgPeriodDays(aggregate.getUser().getAvgPeriodDays())
                    .PredictedStartTime(aggregate.getPredictedStartTime())
                    .PredictedEndTime(aggregate.getPredictedEndTime())
                    .comeDays(comeDays)
                    .goDays(goDays)
                    .status(aggregate.getStatus() != null ? aggregate.getStatus().getCode() : 1)
                    .events(new ArrayList<>())
                    .aiSuggestion(null)
                    .build();

            log.info("查询首页信息成功 userId={}", userIdRequestDTO.getUserId());
            return Response.<RecordIndexResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(dto)
                    .build();

        } catch (Exception e) {
            log.error("查询首页信息失败 userId={}", userIdRequestDTO.getUserId(), e);
            return Response.<RecordIndexResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "over_cycle_record", method = RequestMethod.POST)
    @Override
    public Response<Boolean> overCycleRecord(@RequestBody UserIdRequestDTO userIdRequestDTO) {
        try {
            if (userIdRequestDTO.getUserId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            Boolean result = cycleLifecycleService.overCycleRecord(userIdRequestDTO.getUserId());

            if (Boolean.TRUE.equals(result)) {
                log.info("结束周期成功 userId={}", userIdRequestDTO.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(true)
                        .build();
            } else {
                log.warn("结束周期失败，更新记录为 0 userId={}", userIdRequestDTO.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.UPDATE_ZERO.getCode())
                        .info(ResponseCode.UPDATE_ZERO.getInfo())
                        .data(false)
                        .build();
            }
        } catch (Exception e) {
            log.error("结束周期异常 userId={}", userIdRequestDTO.getUserId(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "start_cycle_record", method = RequestMethod.POST)
    @Override
    public Response<Boolean> startCycleRecord(@RequestBody UserIdRequestDTO userIdRequestDTO) {
        try {
            if (userIdRequestDTO.getUserId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            Boolean result = cycleLifecycleService.startCycleRecord(userIdRequestDTO.getUserId());

            if (Boolean.TRUE.equals(result)) {
                log.info("开始新周期成功 userId={}", userIdRequestDTO.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(true)
                        .build();
            } else {
                log.warn("开始新周期失败，更新记录为 0 userId={}", userIdRequestDTO.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.UPDATE_ZERO.getCode())
                        .info(ResponseCode.UPDATE_ZERO.getInfo())
                        .data(false)
                        .build();
            }
        } catch (Exception e) {
            log.error("开始新周期异常 userId={}", userIdRequestDTO.getUserId(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "query_symptom", method = RequestMethod.POST)
    @Override
    public Response<SymptomResponseDTO> getSymptom(@RequestBody SymptomRequestDTO dto) {
        try {
            if (dto.getUserId() == null || dto.getCycleId() == null) {
                return Response.<SymptomResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            DailySymptomEntity entity = healthTrackService.getTodaySymptom(dto.getUserId(), dto.getCycleId());

            SymptomResponseDTO resp = SymptomResponseDTO.builder()
                    .recordId(entity.getRecordId())
                    .cycleId(entity.getCycleId())
                    .userId(entity.getUserId())
                    .recordDate(entity.getRecordDate())
                    .flowLevel(entity.getFlowLevel())
                    .painLevel(entity.getPainLevel())
                    .mood(entity.getMood())
                    .notes(entity.getNotes())
                    .createTime(entity.getCreateTime())
                    .updateTime(entity.getUpdateTime())
                    .build();

            log.info("查询今日症状成功 userId={}, cycleId={}, recordId={}",
                    dto.getUserId(), dto.getCycleId(), entity.getRecordId());
            return Response.<SymptomResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(resp)
                    .build();
        } catch (Exception e) {
            log.error("查询今日症状异常 userId={}, cycleId={}", dto.getUserId(), dto.getCycleId(), e);
            return Response.<SymptomResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "change_symptom", method = RequestMethod.POST)
    @Override
    public Response<Boolean> changeSymptom(@RequestBody SymptomRequestDTO dto) {
        try {
            if (dto.getCycleId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            DailySymptomEntity entity = new DailySymptomEntity();
            entity.setUserId(dto.getUserId());
            entity.setCycleId(dto.getCycleId());
            entity.setRecordId(dto.getRecordId());
            entity.setFlowLevel(dto.getFlowLevel());
            entity.setPainLevel(dto.getPainLevel());
            entity.setMood(dto.getMood());
            entity.setNotes(dto.getNotes());

            Boolean result = healthTrackService.changeSymptom(entity);

            if (Boolean.TRUE.equals(result)) {
                log.info("更新今日症状成功 recordId={}", dto.getRecordId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(true)
                        .build();
            } else {
                log.warn("更新今日症状失败 recordId={}", dto.getRecordId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.UPDATE_ZERO.getCode())
                        .info(ResponseCode.UPDATE_ZERO.getInfo())
                        .data(false)
                        .build();
            }
        } catch (Exception e) {
            log.error("更新今日症状异常 recordId={}", dto.getRecordId(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
