package top.daoha.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.daoha.api.MineService;
import top.daoha.api.dto.*;
import top.daoha.api.response.Response;
import top.daoha.domain.cycle.model.aggregate.RecordsAggregate;
import top.daoha.domain.cycle.model.entity.CycleRecordEntity;
import top.daoha.domain.cycle.service.ICycleHistoryService;
import top.daoha.domain.identity.model.entity.UserEntity;
import top.daoha.types.enums.ResponseCode;
import top.daoha.types.exception.AppException;

import javax.annotation.Resource;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/record/mine/")
public class MineController implements MineService {

    @Resource
    private top.daoha.domain.identity.service.IAuthService authService;

    @Resource
    private ICycleHistoryService cycleHistoryService;

    @RequestMapping(value = "getUsrInfo", method = RequestMethod.POST)
    @Override
    public Response<UserInfoResposeDTO> getUsrInfo(@RequestBody UserIdRequestDTO user) {
        try {
            if (user.getUserId() == null) {
                return Response.<UserInfoResposeDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            UserEntity entity = authService.queryUserInfo(user.getUserId());
            if (entity == null) {
                return Response.<UserInfoResposeDTO>builder()
                        .code(ResponseCode.USERNAME_UNEXIT.getCode())
                        .info(ResponseCode.USERNAME_UNEXIT.getInfo())
                        .build();
            }

            UserInfoResposeDTO resp = UserInfoResposeDTO.builder()
                    .username(entity.getUsername())
                    .avatar(entity.getAvatar())
                    .phone(entity.getPhone())
                    .birthday(entity.getBirthday())
                    .height(entity.getHeight())
                    .weight(entity.getWeight())
                    .build();

            log.info("查询用户信息成功 userId={}", user.getUserId());
            return Response.<UserInfoResposeDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(resp)
                    .build();
        } catch (AppException e) {
            log.warn("查询用户信息失败 userId={}, code={}, info={}", user.getUserId(), e.getCode(), e.getInfo());
            return Response.<UserInfoResposeDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("查询用户信息异常 userId={}", user.getUserId(), e);
            return Response.<UserInfoResposeDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "changeUserInfo", method = RequestMethod.POST)
    @Override
    public Response<Boolean> changeUserInfo(@RequestBody UserInfoRequestDTO user) {
        try {
            if (user.getUserId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            UserEntity entity = UserEntity.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .avatar(user.getAvatar())
                    .phone(user.getPhone())
                    .birthday(user.getBirthday())
                    .height(user.getHeight())
                    .weight(user.getWeight())
                    .build();

            Boolean result = authService.updataUserInfo(entity);

            if (Boolean.TRUE.equals(result)) {
                log.info("更新用户信息成功 userId={}", user.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(true)
                        .build();
            } else {
                log.warn("更新用户信息失败 userId={}", user.getUserId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.UPDATE_ZERO.getCode())
                        .info(ResponseCode.UPDATE_ZERO.getInfo())
                        .data(false)
                        .build();
            }
        } catch (AppException e) {
            log.warn("更新用户信息失败 userId={}, code={}, info={}", user.getUserId(), e.getCode(), e.getInfo());
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("更新用户信息异常 userId={}", user.getUserId(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "getUserRecord", method = RequestMethod.POST)
    @Override
    public Response<RecordsResponseDTO> getUserRecord(@RequestBody DataShowRequestDTO dataShowRequestDTO) {
        try {
            if (dataShowRequestDTO.getUserId() == null) {
                return Response.<RecordsResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            Integer page = dataShowRequestDTO.getPage();
            if (page == null || page <= 0) {
                page = 1;
            }
            Integer pageSize = dataShowRequestDTO.getPageSize();
            if (pageSize == null || pageSize <= 0) {
                pageSize = 2;
            }

            RecordsAggregate aggregate = cycleHistoryService.getRecordsList(dataShowRequestDTO.getUserId(), page, pageSize);

            java.util.List<RecordsResponseDTO.CycleRecord> cycleRecords = new java.util.ArrayList<>();
            if (aggregate.getCycleList() != null) {
                for (CycleRecordEntity entity : aggregate.getCycleList()) {
                    RecordsResponseDTO.CycleRecord cr = new RecordsResponseDTO.CycleRecord();
                    cr.setCycleId(entity.getCycleId());
                    cr.setStartDate(entity.getStartDate());
                    cr.setEndDate(entity.getEndDate());
                    cycleRecords.add(cr);
                }
            }

            RecordsResponseDTO resp = RecordsResponseDTO.builder()
                    .recordsCount(aggregate.getCount())
                    .cycleRecords(cycleRecords)
                    .build();

            log.info("查询用户生理期记录成功 userId={}, page={}, 总记录数={}, 返回{}条",
                    dataShowRequestDTO.getUserId(), page, aggregate.getCount(), cycleRecords.size());
            return Response.<RecordsResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(resp)
                    .build();
        } catch (AppException e) {
            log.warn("查询生理期记录失败 userId={}, code={}, info={}", dataShowRequestDTO.getUserId(), e.getCode(), e.getInfo());
            return Response.<RecordsResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("查询生理期记录异常 userId={}", dataShowRequestDTO.getUserId(), e);
            return Response.<RecordsResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "changeUserRecord", method = RequestMethod.POST)
    @Override
    public Response<Boolean> changeUserRecord(@RequestBody RecordsResponseDTO.CycleRecord record) {
        try {
            if (record.getCycleId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            CycleRecordEntity entity = CycleRecordEntity.builder()
                    .cycleId(record.getCycleId())
                    .startDate(record.getStartDate())
                    .endDate(record.getEndDate())
                    .build();

            Boolean result = cycleHistoryService.updateCycleRecord(entity);

            if (Boolean.TRUE.equals(result)) {
                log.info("更新生理期记录成功 cycleId={}", record.getCycleId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(true)
                        .build();
            } else {
                log.warn("更新生理期记录失败 cycleId={}", record.getCycleId());
                return Response.<Boolean>builder()
                        .code(ResponseCode.UPDATE_ZERO.getCode())
                        .info(ResponseCode.UPDATE_ZERO.getInfo())
                        .data(false)
                        .build();
            }
        } catch (AppException e) {
            log.warn("更新生理期记录失败 cycleId={}, code={}, info={}", record.getCycleId(), e.getCode(), e.getInfo());
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("更新生理期记录异常 cycleId={}", record.getCycleId(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }


}
