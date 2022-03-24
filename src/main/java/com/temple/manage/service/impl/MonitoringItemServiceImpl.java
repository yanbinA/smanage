package com.temple.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.temple.manage.entity.MonitoringItem;
import com.temple.manage.service.MonitoringItemService;
import com.temple.manage.mapper.MonitoringItemMapper;
import org.springframework.stereotype.Service;

/**
* @author willso
* @description 针对表【s_monitoring_item(检测项目)】的数据库操作Service实现
* @createDate 2021-12-24 15:42:53
*/
@Service
public class MonitoringItemServiceImpl extends ServiceImpl<MonitoringItemMapper, MonitoringItem>
    implements MonitoringItemService{

}




