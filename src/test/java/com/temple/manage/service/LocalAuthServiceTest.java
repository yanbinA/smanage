package com.temple.manage.service;
import com.temple.manage.entity.LocalAuth;
import com.temple.manage.mapper.LocalAuthMapper;
import com.temple.manage.service.impl.LocalAuthServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * <p>
 * LocalAuthService测试类
 * </p>
 *
 * @author messi
 * @description
 * @date 2022-12-01 0:55
 * @verison V1.0.0
 */
public class LocalAuthServiceTest {
    private LocalAuthServiceImpl localAuthService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private LocalAuthMapper localAuthMapper;

    @Before
    public void setup() {
        roleService = mock(RoleService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        localAuthMapper = mock(LocalAuthMapper.class);
        localAuthService = new LocalAuthServiceImpl(passwordEncoder, roleService, localAuthMapper);
    }

    @Test
    public void save() {
        when(passwordEncoder.encode("123456")).thenReturn("654321");
        when(localAuthMapper.insert(any(LocalAuth.class))).thenReturn(1);
        LocalAuth localAuth = new LocalAuth();
        localAuth.setUserId(1L);
        localAuth.setUsername("user");
        localAuth.setPhoneNo("13823582947");
        localAuth.setPassword("123456");
        localAuth.setCreateTime(LocalDateTime.now());
        localAuth.setUpdateTime(LocalDateTime.now());
        localAuthService.registerUsername(localAuth);
        verify(passwordEncoder).encode("123456");
        LocalAuth same = same(localAuth);
        localAuth.setUserId(2L);
        verify(localAuthMapper).insert(same);
    }
}
