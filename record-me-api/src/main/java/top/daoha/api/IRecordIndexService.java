package top.daoha.api;

import top.daoha.api.dto.RecordIndexRequestDTO;
import top.daoha.api.dto.RecordIndexResponseDTO;
import top.daoha.api.dto.SymptomRequestDTO;
import top.daoha.api.dto.SymptomResponseDTO;
import top.daoha.api.response.Response;

public interface IRecordIndexService {

    Response<RecordIndexResponseDTO> getIndexInfoAggregate(RecordIndexRequestDTO recordIndexRequestDTO);

    Response<Boolean> overCycleRecord(RecordIndexRequestDTO recordIndexRequestDTO);

    Response<Boolean> startCycleRecord(RecordIndexRequestDTO recordIndexRequestDTO);

    Response<SymptomResponseDTO> getSymptom(SymptomRequestDTO symptomRequestDTO);

    Response<Boolean> changeSymptom(SymptomRequestDTO symptomRequestDTO);

}
