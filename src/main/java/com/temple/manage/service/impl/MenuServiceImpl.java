package com.temple.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.temple.manage.entity.Menu;
import com.temple.manage.service.MenuService;
import com.temple.manage.mapper.MenuMapper;
import org.springframework.stereotype.Service;

/**
* @author willso
* @description 针对表【s_menu】的数据库操作Service实现
* @createDate 2021-12-24 15:42:53
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

}




