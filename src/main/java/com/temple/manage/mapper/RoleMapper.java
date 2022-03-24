package com.temple.manage.mapper;

import com.temple.manage.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author willso
* @description 针对表【s_role】的数据库操作Mapper
* @createDate 2021-12-24 15:42:53
* @Entity com.temple.manage.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> listByUserId(Long id);
}




