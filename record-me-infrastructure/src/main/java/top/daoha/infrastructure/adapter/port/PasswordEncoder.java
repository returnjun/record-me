package top.daoha.infrastructure.adapter.port;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import top.daoha.domain.auth.adapter.port.IPasswordEncoder; // 注意：之前建议过 domain 层路径最好去掉 adapter 这一层级

@Component
public class PasswordEncoder implements IPasswordEncoder {

    // 实例化 BCrypt 加密器
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        // 直接调用 BCrypt 的加密方法，它会自动生成随机 Salt 并和密文拼在一起
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        // BCrypt 会自动从 encodedPassword 中提取 Salt 并对 rawPassword 进行验证
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}