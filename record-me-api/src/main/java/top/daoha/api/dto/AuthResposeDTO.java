package top.daoha.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResposeDTO {

    //用户id
    private Long userId;
    //用户名
    private String username;
    //用户密码
    private String password;
    //电话
    private String phone;
}
