package top.daoha.domain.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.daoha.domain.auth.adapter.port.IPasswordEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    private Long userId; // 确保有这个主键字段

    private String username;
    //用户密码
    private String password;
    //电话
    private String phone;

    // 实体行为：注册时加密密码
    public void encryptPassword(String rawPassword, IPasswordEncoder encoder) {
        this.password = encoder.encode(rawPassword);
    }

    // 实体行为：验证密码
    public boolean verifyPassword(String rawPassword, IPasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.password);
    }

}
