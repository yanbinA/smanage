package com.temple.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.temple.manage.domain.dto.ImproveDto;
import com.temple.manage.entity.Improve;
import com.baomidou.mybatisplus.extension.service.IService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.ibatis.annotations.Param;

/**
* @author messi
* @description 针对表【s_improve】的数据库操作Service
* @createDate 2022-01-10 21:05:05
*/
public interface ImproveService extends IService<Improve> {

    void submit(ImproveDto improveDto) throws WxErrorException;

    void adopt(ImproveDto improveDto) throws WxErrorException;

    IPage<Improve> approved(IPage<Improve> page, String userId);

    IPage<Improve> follow(IPage<Improve> page, String userId);
}
