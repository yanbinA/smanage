package com.temple.manage.common.handler;

import com.alibaba.fastjson.TypeReference;
import com.temple.manage.domain.ItemResult;

import java.util.List;

public class ItemResultTypeHandler extends ListTypeHandler<ItemResult>{
    @Override
    protected TypeReference<List<ItemResult>> specificType() {
        return new TypeReference<>(){};
    }
}
