package com.temple.manage.controller;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.temple.manage.common.api.R;
import com.temple.manage.common.validators.group.Modify;
import com.temple.manage.domain.dto.FactoryAreaDto;
import com.temple.manage.domain.dto.FactoryAreaPlanDto;
import com.temple.manage.domain.vo.FactoryAreaVo;
import com.temple.manage.domain.vo.FactoryPlanVo;
import com.temple.manage.domain.vo.FactoryRemarkVo;
import com.temple.manage.entity.FactoryArea;
import com.temple.manage.entity.FactoryPlan;
import com.temple.manage.entity.FactoryRemark;
import com.temple.manage.service.FactoryAreaService;
import com.temple.manage.service.FactoryPlanService;
import com.temple.manage.service.FactoryRemarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/area")
@Tag(name = "车间")
@Validated
public class FactoryAreaController {
    private final FactoryAreaService factoryAreaService;
    private final FactoryPlanService factoryPlanService;
    private final FactoryRemarkService factoryRemarkService;

    public FactoryAreaController(FactoryAreaService factoryAreaService, FactoryPlanService factoryPlanService, FactoryRemarkService factoryRemarkService) {
        this.factoryAreaService = factoryAreaService;
        this.factoryPlanService = factoryPlanService;
        this.factoryRemarkService = factoryRemarkService;
    }

    @GetMapping("/list")
    @Operation(summary = "查询所有车间", security = @SecurityRequirement(name = "Authorization"))
    public R<List<FactoryAreaVo>> list() {
        LambdaQueryWrapper<FactoryArea> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FactoryArea::getIsDeleted, false);
        List<FactoryArea> areas = this.factoryAreaService.list(wrapper);
        return R.success(BeanUtil.copyToList(areas, FactoryAreaVo.class));
    }

    @PostMapping()
    @Operation(summary = "添加车间", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> insert(@Validated @RequestBody FactoryAreaDto factoryAreaDto) {
        FactoryArea factoryArea = new FactoryArea();
        BeanUtils.copyProperties(factoryAreaDto, factoryArea);
        return R.success(this.factoryAreaService.save(factoryArea));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改车间信息", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> update(@Validated(value = Modify.class) @RequestBody FactoryAreaDto factoryAreaDto) {
        FactoryArea factoryArea = new FactoryArea();
        BeanUtils.copyProperties(factoryAreaDto, factoryArea);
        return R.success(this.factoryAreaService.updateById(factoryArea));
    }

    @PostMapping("/modifyPlan")
    @Operation(summary = "修改车间平面图", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> updatePlan(@RequestBody FactoryAreaPlanDto factoryAreaDto) {
        FactoryArea factoryArea = new FactoryArea();
        BeanUtils.copyProperties(factoryAreaDto, factoryArea);
        return R.success(this.factoryAreaService.updateById(factoryArea));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除车间", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> delete(@NotNull @RequestParam("id") Integer id) {
        return R.success(this.factoryAreaService.removeById(id));
    }

    @PostMapping("/saveFactoryPlan")
    @Operation(summary = "保存工厂平面图", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> saveFactoryPlan(@Validated @RequestBody FactoryPlanVo planVo) {
        FactoryPlan factoryPlan = new FactoryPlan();
        factoryPlan.setId(planVo.getId());
        factoryPlan.setUrl(planVo.getUrl());
        return R.success(this.factoryPlanService.saveOrUpdate(factoryPlan));
    }

    @GetMapping("/factoryPlan")
    @Operation(summary = "获取工厂平面图", security = @SecurityRequirement(name = "Authorization"))
    public R<FactoryPlanVo> getFactoryPlan() {
        LambdaQueryWrapper<FactoryPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(FactoryPlan::getId);
        FactoryPlan factoryPlan = this.factoryPlanService.getOne(wrapper, false);
        FactoryPlanVo planVo = new FactoryPlanVo();
        if (factoryPlan == null) {
            return R.success(planVo);
        }
        planVo.setId(factoryPlan.getId());
        planVo.setUrl(factoryPlan.getUrl());
        return R.success(planVo);
    }

    @PostMapping("/saveRemark")
    @Operation(summary = "保存评分描述", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> saveRemark(@Validated @RequestBody FactoryRemarkVo remarkVo) {
        FactoryRemark factoryRemark = new FactoryRemark();
        factoryRemark.setId(remarkVo.getId());
        factoryRemark.setCleanRemark(remarkVo.getCleanRemark());
        factoryRemark.setLocalRemark(remarkVo.getLocalRemark());
        factoryRemark.setCleanTitle(remarkVo.getCleanTitle());
        factoryRemark.setLocalTitle(remarkVo.getLocalTitle());
        return R.success(this.factoryRemarkService.saveOrUpdate(factoryRemark));
    }

    @GetMapping("/remark")
    @Operation(summary = "查询评分描述", security = @SecurityRequirement(name = "Authorization"))
    public R<FactoryRemarkVo> getRemark() {
        LambdaQueryWrapper<FactoryRemark> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(FactoryRemark::getId);
        FactoryRemark factoryRemark = this.factoryRemarkService.getOne(wrapper, false);
        FactoryRemarkVo remarkVo = new FactoryRemarkVo();
        if (factoryRemark == null) {
            remarkVo.setLocalRemark("未设置");
            remarkVo.setLocalTitle("未设置");
            remarkVo.setCleanRemark("未设置");
            remarkVo.setCleanTitle("未设置");
            return R.success(remarkVo);
        }
        remarkVo.setId(factoryRemark.getId());
        remarkVo.setLocalRemark(factoryRemark.getLocalRemark());
        remarkVo.setCleanRemark(factoryRemark.getCleanRemark());
        remarkVo.setLocalTitle(factoryRemark.getLocalTitle());
        remarkVo.setCleanTitle(factoryRemark.getCleanTitle());
        return R.success(remarkVo);
    }
}
