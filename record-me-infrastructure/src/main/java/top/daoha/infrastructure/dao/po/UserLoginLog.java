package top.daoha.infrastructure.dao.po;

import lombok.Data;

import java.util.Date;

@Data
public class UserLoginLog {

    private Long id;
    private Long userId;
    private String loginIp;
    private String loginDevice;
    private Integer loginStatus;
    private Date loginTime;

}
