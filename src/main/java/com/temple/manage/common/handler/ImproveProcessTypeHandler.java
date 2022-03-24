package com.temple.manage.common.handler;

import com.alibaba.fastjson.TypeReference;
import com.temple.manage.entity.ImproveProcess;

import java.util.List;

/**
 * <p>
 * mybatis中List<ImproveProcess>类型转换处理类
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.handler
 * @description mybatis中List<ImproveProcess>类型转换处理类
 * @date 2022-01-12 1:25
 * @verison V1.0.0
 */
public class ImproveProcessTypeHandler extends ListTypeHandler<ImproveProcess>{
    @Override
    protected TypeReference<List<ImproveProcess>> specificType() {
        return new TypeReference<>(){};
    }
}
