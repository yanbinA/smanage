package com.temple.manage.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.temple.manage.common.api.R;
import com.temple.manage.common.validators.group.Modify;
import com.temple.manage.domain.dto.MonitoringPointDto;
import com.temple.manage.domain.vo.MonitoringItemVo;
import com.temple.manage.domain.vo.MonitoringPointDetailVo;
import com.temple.manage.domain.vo.MonitoringPointVo;
import com.temple.manage.entity.MonitoringItem;
import com.temple.manage.entity.MonitoringPoint;
import com.temple.manage.service.MonitoringPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 检查点Controller
 * </p>
 *
 * @author messi
 * @package com.temple.manage.controller
 * @description 检查点Controller
 * @date 2021-12-27 20:50
 * @verison V1.0.0
 */
@RestController
@RequestMapping("/api/monitor")
@Tag(name = "检查点",description = "检查点")
@Validated
public class MonitorController {
    private final MonitoringPointService monitoringPointService;

    public MonitorController(MonitoringPointService monitoringPointService) {
        this.monitoringPointService = monitoringPointService;
    }

    @GetMapping("/list")
    @Operation(summary = "查询所有检查点", security = @SecurityRequirement(name = "Authorization"))
    public R<List<MonitoringPointVo>> list(@NotNull @RequestParam() Integer factoryAreaId) {
        LambdaQueryWrapper<MonitoringPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringPoint::getFactoryAreaId, factoryAreaId)
                .eq(MonitoringPoint::getIsDeleted, false)
                .orderByAsc(MonitoringPoint::getAreaName)
                .orderByAsc(MonitoringPoint::getSerialNumber);
        List<MonitoringPoint> list = this.monitoringPointService.list(wrapper);
        return R.success(BeanUtil.copyToList(list, MonitoringPointVo.class));
    }

    @GetMapping("/getById")
    @Operation(summary = "查询检查点详情", security = @SecurityRequirement(name = "Authorization"))
    public R<MonitoringPointDetailVo> getById(@NotNull @RequestParam Integer id) {
        MonitoringPoint monitoringPoint = this.monitoringPointService.getById(id);

        MonitoringPointDetailVo detailVo = new MonitoringPointDetailVo();
        BeanUtils.copyProperties(monitoringPoint, detailVo);
        List<MonitoringItem> itemList = this.monitoringPointService.listMonitoringItemByPointId(id);
        detailVo.setItemList(BeanUtil.copyToList(itemList, MonitoringItemVo.class));
        return R.success(detailVo);
    }

    @PostMapping("save")
    @Operation(summary = "新增检查点", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> insert(@Validated @RequestBody MonitoringPointDto monitoringPointDto) {
        return R.success(this.monitoringPointService.insert(monitoringPointDto));
    }

    @PostMapping("modify")
    @Operation(summary = "修改检查点", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> modify(@Validated(Modify.class) @RequestBody MonitoringPointDto monitoringPointDto) {
        return R.success(this.monitoringPointService.modify(monitoringPointDto));
    }

    @PostMapping("remove")
    @Operation(summary = "删除检查点", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> remove(@NotNull @RequestParam("id") Integer id) {
        return R.success(this.monitoringPointService.deleteById(id));
    }

}
