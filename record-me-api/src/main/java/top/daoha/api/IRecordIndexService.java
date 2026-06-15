package top.daoha.api;

import top.daoha.api.dto.UserIdRequestDTO;
import top.daoha.api.dto.RecordIndexResponseDTO;
import top.daoha.api.dto.SymptomRequestDTO;
import top.daoha.api.dto.SymptomResponseDTO;
import top.daoha.api.response.Response;

public interface IRecordIndexService {

    Response<RecordIndexResponseDTO> getIndexInfoAggregate(UserIdRequestDTO userIdRequestDTO);

    Response<Boolean> overCycleRecord(UserIdRequestDTO userIdRequestDTO);

    Response<Boolean> startCycleRecord(UserIdRequestDTO userIdRequestDTO);

    Response<SymptomResponseDTO> getSymptom(SymptomRequestDTO symptomRequestDTO);

    Response<Boolean> changeSymptom(SymptomRequestDTO symptomRequestDTO);

}
