package top.daoha.infrastructure.adapter.repository;

import org.springframework.stereotype.Repository;
import top.daoha.domain.userLogin.adapter.repository.ILoginRepository;
import top.daoha.domain.userLogin.model.entity.UserEntity;

import java.util.Date;

@Repository
public class LoginRepository implements ILoginRepository {

    @Override
    public UserEntity queryByUsername(String username) {
        //
        return null;
    }

    @Override
    public boolean checkUserPassword(UserEntity user) {
        return false;
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return false;
    }

    @Override
    public boolean insertUser(UserEntity user) {
        return false;
    }

    @Override
    public void updateLoginTrace(Long userId, String loginIp, Date loginTime) {

    }

    @Override
    public void updatePassword(Long userId, String encryptedPassword) {

    }

    @Override
    public void logicalDeleteUser(Long userId) {

    }
}
