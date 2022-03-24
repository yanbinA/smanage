package com.temple.manage.service.impl;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.temple.manage.entity.Role;
import com.temple.manage.service.RoleService;
import com.temple.manage.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @author willso
* @description 针对表【s_role】的数据库操作Service实现
* @createDate 2021-12-24 15:42:53
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{
    @Override
    public List<Role> listByUserId(Long id) {
        List<Role> roles = baseMapper.listByUserId(id);
        if (CollectionUtils.isEmpty(roles)) {
            roles = new ArrayList<>();
            Role role = new Role();
            role.setName("ROLE_AUDIT");
            roles.add(role);
        }
        return roles;
    }
}




