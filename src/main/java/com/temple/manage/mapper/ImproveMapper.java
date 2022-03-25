package com.temple.manage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.temple.manage.entity.Improve;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author messi
* @description 针对表【s_improve】的数据库操作Mapper
* @createDate 2022-01-10 21:05:05
* @Entity com.temple.manage.entity.Improve
*/
public interface ImproveMapper extends BaseMapper<Improve> {
    IPage<Improve> approved(IPage<?> page, @Param("userId") String userId);

    IPage<Improve> follow(IPage<?> page, @Param("userId") String userId);
}




