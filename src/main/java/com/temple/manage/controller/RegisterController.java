package com.temple.manage.controller;

import com.temple.manage.common.api.R;
import com.temple.manage.domain.dto.UserRegisterDto;
import com.temple.manage.entity.LocalAuth;
import com.temple.manage.entity.User;
import com.temple.manage.service.LocalAuthService;
import com.temple.manage.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/register")
public class RegisterController {
    private final UserService userService;
    private final LocalAuthService localAuthService;

    public RegisterController(UserService userService, LocalAuthService localAuthService) {
        this.userService = userService;
        this.localAuthService = localAuthService;
    }

    @PostMapping()
    @Transactional(rollbackFor = Exception.class)
    public R register(@Valid @RequestBody UserRegisterDto userRegisterDto){
        User user = new User();
        LocalAuth localAuth = new LocalAuth();
        BeanUtils.copyProperties(userRegisterDto, user);
        BeanUtils.copyProperties(userRegisterDto, localAuth);
        userService.save(user);
        localAuth.setUserId(user.getId());
        localAuthService.registerUsername(localAuth);
        return R.success();
    }
}