package com.temple.manage.service;

import com.temple.manage.domain.dto.ImproveDto;
import com.temple.manage.entity.Improve;
import com.baomidou.mybatisplus.extension.service.IService;
import me.chanjar.weixin.common.error.WxErrorException;

/**
* @author messi
* @description 针对表【s_improve】的数据库操作Service
* @createDate 2022-01-10 21:05:05
*/
public interface ImproveService extends IService<Improve> {

    void submit(ImproveDto improveDto) throws WxErrorException;

    void adopt(ImproveDto improveDto) throws WxErrorException;
}
