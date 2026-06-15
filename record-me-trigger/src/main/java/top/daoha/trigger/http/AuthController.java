package top.daoha.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.daoha.api.IAuthService;
import top.daoha.api.dto.AuthRequestDTO;
import top.daoha.api.dto.AuthResposeDTO;
import top.daoha.api.response.Response;
import top.daoha.domain.identity.model.entity.UserEntity;
import top.daoha.types.enums.ResponseCode;
import top.daoha.types.exception.AppException;

import javax.annotation.Resource;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/record/auth/")
public class AuthController implements IAuthService {

    @Resource
    private top.daoha.domain.identity.service.IAuthService authService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @Override
    public Response<AuthResposeDTO> login(@RequestBody AuthRequestDTO user) {
        try {
            if (user.getUsername() == null || user.getPassword() == null
                    || user.getUsername().trim().isEmpty() || user.getPassword().trim().isEmpty()) {
                return Response.<AuthResposeDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("账号或密码不能为空")
                        .build();
            }

            UserEntity userEntity = UserEntity.builder()
                    .username(user.getUsername().trim())
                    .password(user.getPassword())
                    .build();

            UserEntity loggedInUser = authService.checkUserLogin(userEntity);

            log.info("用户登录成功 loginAccount={}, userId={}", user.getUsername(), loggedInUser.getUserId());
            AuthResposeDTO resp = AuthResposeDTO.builder()
                    .userId(loggedInUser.getUserId())
                    .username(loggedInUser.getUsername())
                    .password(null)
                    .phone(loggedInUser.getPhone())
                    .build();
            return Response.<AuthResposeDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(resp)
                    .build();
        } catch (AppException e) {
            log.warn("登录失败 username={}, code={}, info={}", user.getUsername(), e.getCode(), e.getInfo());
            return Response.<AuthResposeDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("登录异常 username={}", user.getUsername(), e);
            return Response.<AuthResposeDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @Override
    public Response<AuthResposeDTO> register(@RequestBody AuthRequestDTO user) {
        try {
            if (user.getUsername() == null || user.getPassword() == null
                    || user.getUsername().trim().isEmpty() || user.getPassword().trim().isEmpty()) {
                return Response.<AuthResposeDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("用户名或密码不能为空")
                        .build();
            }

            String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s]).{8,}$";
            if (!user.getPassword().matches(passwordRegex)) {
                return Response.<AuthResposeDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("密码复杂度不足，请求被拒绝")
                        .build();
            }

            UserEntity userEntity = UserEntity.builder()
                    .username(user.getUsername().trim())
                    .password(user.getPassword())
                    .phone(user.getPhone() == null ? null : user.getPhone().trim())
                    .build();

            UserEntity registered = authService.userRegister(userEntity);

            log.info("用户注册成功 userId={}, username={}", registered.getUserId(), registered.getUsername());

            AuthResposeDTO resp = AuthResposeDTO.builder()
                    .userId(registered.getUserId())
                    .username(registered.getUsername())
                    .password(null)
                    .phone(registered.getPhone())
                    .build();

            return Response.<AuthResposeDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(resp)
                    .build();

        } catch (AppException e) {
            log.warn("注册失败 username={}, code={}, info={}", user.getUsername(), e.getCode(), e.getInfo());
            return Response.<AuthResposeDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("注册异常 username={}", user.getUsername(), e);
            return Response.<AuthResposeDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
