package top.daoha.domain.identity.adapter.port;

public interface IPasswordEncoder {

    // 加密原始密码
    String encode(String rawPassword);

    // 校验原始密码和密文是否匹配
    boolean matches(String rawPassword, String encodedPassword);
}
