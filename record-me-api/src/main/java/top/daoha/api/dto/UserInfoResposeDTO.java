package top.daoha.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResposeDTO {
    // 用户名
    private String username;
    // 头像
    private String avatar;
    // 电话
    private String phone;
    //生日
    private Date birthday;
    //身高
    private BigDecimal height;
    //体重
    private BigDecimal weight;
}
