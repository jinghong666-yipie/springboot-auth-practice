package com.example.auth.runner;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.auth.entity.User;
import com.example.auth.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class DemoUserInitializer implements CommandLineRunner {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public DemoUserInitializer(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Long count = userMapper.selectCount(
                Wrappers.<User>lambdaQuery().eq(User::getUsername, "alice"));
        if (count == 0) {
            User user = new User();
            user.setUsername("alice");
            user.setPassword(passwordEncoder.encode("Passw0rd!"));
            user.setStatus(1);
            userMapper.insert(user);
            System.out.println("已创建演示账号 alice（密码：Passw0rd!），仅用于本地学习。");
        }
    }
}
