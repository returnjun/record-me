package top.daoha.api;

import top.daoha.api.dto.AiAdviceResponseDTO;
import top.daoha.api.dto.UserIdRequestDTO;
import top.daoha.api.response.Response;

public interface IAiService {

    Response<AiAdviceResponseDTO> getHealthAdvice(UserIdRequestDTO userIdRequestDTO);

    Response<AiAdviceResponseDTO> getPersona(UserIdRequestDTO userIdRequestDTO);
}
