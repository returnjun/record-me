package top.daoha.domain.identity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.daoha.domain.identity.adapter.port.IPasswordEncoder;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    // 用户 ID
    private Long userId;

    // 用户名
    private String username;

    // 用户密码
    private String password;

    // 电话
    private String phone;
    //头像
    private String avatar;
    //生日
    private Date birthday;
    //身高
    private BigDecimal height;
    //体重
    private BigDecimal weight;

    // 注册时加密密码
    public void encryptPassword(String rawPassword, IPasswordEncoder encoder) {
        this.password = encoder.encode(rawPassword);
    }

    // 登录时验证密码
    public boolean verifyPassword(String rawPassword, IPasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.password);
    }
}
