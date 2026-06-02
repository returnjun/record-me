package top.daoha.domain.userLogin.service.register;

import org.springframework.stereotype.Service;
import top.daoha.domain.userLogin.adapter.repository.ILoginRepository;

import javax.annotation.Resource;

@Service
public class registerService implements IRegisterService {

    @Resource
    private ILoginRepository loginRepository;


}
