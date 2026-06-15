package top.daoha.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    INDEX_EXCEPTION("0003", "唯一索引异常"),
    UPDATE_ZERO("0004","更新记录为0"),
    HTTP_EXCEPTION("0005","HTTP接口调用异常"),
    RATE_LIMITER("0006","接口限流异常"),
    PASSWORD_ERROR("0007","密码错误"),
    USERNAME_EXIT("0008","用户已经存在"),
    USERNAME_UNEXIT("0009","用户不存在"),
    ;

    private String code;
    private String info;

}
