package top.daoha.api;

import top.daoha.api.dto.*;
import top.daoha.api.response.Response;

import java.util.List;

public interface MineService {

    Response<UserInfoResposeDTO> getUsrInfo(UserIdRequestDTO user);

    Response<Boolean> changeUserInfo(UserInfoRequestDTO user);

    Response<RecordsResponseDTO> getUserRecord(DataShowRequestDTO dataShowRequestDTO);

    Response<Boolean> changeUserRecord(RecordsResponseDTO.CycleRecord record);
}
