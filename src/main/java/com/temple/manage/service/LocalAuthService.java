package com.temple.manage.service;

import com.temple.manage.entity.LocalAuth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author willso
* @description 针对表【s_local_auth】的数据库操作Service
* @createDate 2021-12-24 15:42:53
*/
public interface LocalAuthService extends IService<LocalAuth> {

    void registerUsername(LocalAuth localAuth);

    LocalAuth registerUsername(String username);

    LocalAuth getByLogin(String login);

}
