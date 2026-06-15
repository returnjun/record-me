package top.daoha.infrastructure.adapter.port;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import top.daoha.domain.identity.adapter.port.IPasswordEncoder;

@Component
public class PasswordEncoder implements IPasswordEncoder {

    // BCrypt 密码加密器
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        // BCrypt 会自动生成随机 Salt，并把 Salt 和密文一起保存到结果字符串中
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        // BCrypt 会从密文中解析 Salt，再校验原始密码是否匹配
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
