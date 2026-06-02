package top.daoha.domain.userLogin.service;

import org.springframework.stereotype.Service;


import top.daoha.domain.userLogin.adapter.repository.ILoginRepository;
import top.daoha.domain.userLogin.model.entity.UserEntity;

import javax.annotation.Resource;

@Service
public class LoginService implements IloginService {

    @Resource
    private ILoginRepository loginRepository;


    @Override
    public boolean checkUserPassword(UserEntity user) {
        return loginRepository.checkUserPassword(user);
    }
}
