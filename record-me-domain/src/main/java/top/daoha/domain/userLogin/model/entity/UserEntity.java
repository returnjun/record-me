package top.daoha.domain.userLogin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    private String username;
    //用户密码
    private String password;
    //电话
    private String phone;
}
