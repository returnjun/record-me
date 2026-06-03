package top.daoha.domain.auth.adapter.port;

public interface IPasswordEncoder {
    //对密码进行加密处理
    String encode(String rawPassword);
    //验证密码正确否
    boolean matches(String rawPassword, String encodedPassword);
}
