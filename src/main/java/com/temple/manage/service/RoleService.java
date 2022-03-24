package com.temple.manage.service;

import com.temple.manage.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author willso
* @description 针对表【s_role】的数据库操作Service
* @createDate 2021-12-24 15:42:53
*/
public interface RoleService extends IService<Role> {

    List<Role> listByUserId(Long id);
}
