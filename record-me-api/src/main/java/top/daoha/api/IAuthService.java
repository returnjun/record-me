package top.daoha.api;

import top.daoha.api.dto.AuthRequestDTO;
import top.daoha.api.dto.AuthResposeDTO;
import top.daoha.api.response.Response;

public interface IAuthService {
    //登录
    public Response<AuthResposeDTO> login(AuthRequestDTO user);
    //注册
    public Response<AuthResposeDTO> register(AuthRequestDTO user);
}
