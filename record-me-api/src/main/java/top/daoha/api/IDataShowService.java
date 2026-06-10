package top.daoha.api;

import top.daoha.api.dto.DataShowRequestDTO;
import top.daoha.api.dto.DataShowResponseDTO;
import top.daoha.api.response.Response;


public interface IDataShowService {

    Response<DataShowResponseDTO> getIndexInfoAggregate(DataShowRequestDTO dataShowRequestDTO) throws Exception;

}
