package top.daoha.api;

import top.daoha.api.dto.RecordIndexRequestDTO;
import top.daoha.api.dto.RecordIndexResponseDTO;
import top.daoha.api.response.Response;

public interface IRecordIndexService {

    //查询首页信息
    public Response<RecordIndexResponseDTO> getIndexInfoAggregate(RecordIndexRequestDTO recordIndexRequestDTO);

    //查询首页信息
    public Response<Boolean> overCycleRecord(RecordIndexRequestDTO recordIndexRequestDTO);

    //查询首页信息
    public Response<Boolean> startCycleRecord(RecordIndexRequestDTO recordIndexRequestDTO);

}
