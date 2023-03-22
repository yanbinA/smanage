package com.temple.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.temple.manage.entity.LocalAuth;
import com.temple.manage.service.LocalAuthService;
import com.temple.manage.mapper.LocalAuthMapper;
import com.temple.manage.service.RoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
* @author willso
* @description 针对表【s_local_auth】的数据库操作Service实现
* @createDate 2021-12-24 15:42:53
*/
@Service
public class LocalAuthServiceImpl extends ServiceImpl<LocalAuthMapper, LocalAuth>
    implements LocalAuthService{

    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public LocalAuthServiceImpl(PasswordEncoder passwordEncoder, RoleService roleService, LocalAuthMapper localAuthMapper) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.baseMapper = localAuthMapper;
    }

    @Override
    public void registerUsername(LocalAuth localAuth) {
        String password = this.passwordEncoder.encode(localAuth.getPassword());
        localAuth.setPassword(password);
        this.baseMapper.insert(localAuth);
    }

    @Override
    public LocalAuth registerUsername(String username) {
        LocalAuth localAuth = new LocalAuth();
        localAuth.setPassword(this.passwordEncoder.encode("123456"));
        localAuth.setUserId(0L);
        localAuth.setUsername(username);
        baseMapper.insert(localAuth);
        return localAuth;
    }

    @Override
    public LocalAuth getByLogin(String login) {
        LambdaQueryWrapper<LocalAuth> wrapper = new LambdaQueryWrapper<>();
//      if (new PhoneValidator().isValid(login, null)) {
//         wrapper.eq(LocalAuth::getPhoneNo, login);
//         LocalAuth localAuth = localAuthService.getOne(wrapper);
//         return Optional.ofNullable(localAuth)
//                 .map(user -> createSpringSecurityUser(login, user))
//                 .orElseThrow(() -> new UsernameNotFoundException("User with phone " + login + " was not found in the database"));
//      }

//      if (new EmailValidator().isValid(login, null)) {
//         wrapper.eq(LocalAuth::getMailbox, login);
//         LocalAuth localAuth = localAuthService.getOne(wrapper);
//         return Optional.ofNullable(localAuth)
//                 .map(user -> createSpringSecurityUser(login, user))
//                 .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
//      }

        wrapper.eq(LocalAuth::getUsername, login)
                .last("limit 1");
        LocalAuth localAuth = this.getOne(wrapper);
        if (localAuth == null) {
            localAuth = this.registerUsername(login);
        }
        localAuth.setRoles(roleService.listByUserId(localAuth.getId()));
        return localAuth;
    }


}