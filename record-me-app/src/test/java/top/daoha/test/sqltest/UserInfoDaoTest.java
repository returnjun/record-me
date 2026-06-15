package top.daoha.test.sqltest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoha.infrastructure.dao.IUserInfoDao;
import top.daoha.infrastructure.dao.po.UserInfo;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoDaoTest {

    @Resource
    private IUserInfoDao userInfoDao;

    @Test
    public void test_insert() {
        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("123456");
        user.setAvatar("https://avatar.example.com/default.png");
        user.setPhone("13800138000");
        user.setAvgCycleDays(28);
        user.setAvgPeriodDays(5);

        int count = userInfoDao.insert(user);

        log.info("插入用户影响行数: {}, 新用户 userId: {}", count, user.getUserId());
    }

    @Test
    public void test_selectById() {
        UserInfo user = userInfoDao.selectById(1L);

        log.info("按 ID 查询用户结果: {}", user);
    }

    @Test
    public void test_selectByUsername() {
        UserInfo user = userInfoDao.selectByUsername("testuser");

        log.info("按用户名查询用户结果: {}", user);
    }

    @Test
    public void test_selectList() {
        List<UserInfo> list = userInfoDao.selectList(null);

        log.info("用户列表数量: {}", list.size());
        list.forEach(user -> log.info("用户信息: {}", user));
    }

    @Test
    public void test_updateById() {
        UserInfo user = new UserInfo();
        user.setUserId(1L);
        user.setPhone("13900139000");
        user.setAvgCycleDays(30);

        int count = userInfoDao.updateById(user);

        log.info("更新用户影响行数: {}", count);
    }

    @Test
    public void test_deleteById() {
        int count = userInfoDao.deleteById(1L);

        log.info("删除用户影响行数: {}", count);
    }

}
