package com.temple.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.temple.manage.domain.dto.AuditResultDto;
import com.temple.manage.domain.dto.AuditScoreDto;
import com.temple.manage.domain.dto.MonitoringPointDto;
import com.temple.manage.domain.vo.MonitoringPointAuditVo;
import com.temple.manage.entity.MonitoringItem;
import com.temple.manage.entity.MonitoringPoint;
import com.temple.manage.entity.PointAuditRecord;

import java.util.List;

/**
* @author messi
* @description 针对表【s_monitoring_point(监测点)】的数据库操作Service
* @createDate 2021-12-27 20:46:45
*/
public interface MonitoringPointService extends IService<MonitoringPoint> {

    boolean insert(MonitoringPointDto monitoringPointDto);

    boolean modify(MonitoringPointDto monitoringPointDto);

    /**
     * auditor query MonitoringPoint
     * @param factoryAreaId factoryAreaId
     * @param auditorName   auditorName
     * @return  List<MonitoringPointAuditVo>
     */
    List<MonitoringPointAuditVo> listByAuditor(Integer factoryAreaId, String auditorName);

    List<MonitoringItem> listMonitoringItemByPointId(Integer id);

    /**
     * query MonitoringPoint record status
     * @param auditorRecordId auditorRecordId
     * @param pointId   pointId
     * @return  PARStatusEnum
     */
    PointAuditRecord getPointRecordStatus(Integer auditorRecordId, Integer pointId);

    /**
     * @description 提交检查项结果
     * @author messi
     * @date 2021-12-30 21:42
     * @param resultDto AuditResultDto
     */
    void submitAudit(AuditResultDto resultDto);

    /**
     * @description 管理员修改检查项结果
     * @author messi
     * @date 2021-12-30 21:42
     * @param resultDto AuditResultDto
     */
    void updateAudit(AuditResultDto resultDto);

    /**
     * @description 管理员修改项分数
     * @author messi
     * @date 2021-12-30 21:43
     * @param scoreDto  AuditScoreDto
     */
    void updateScore(AuditScoreDto scoreDto);

    /**
     * @description 提交检查项分数
     * @author messi
     * @date 2021-12-30 21:43
     * @param scoreDto  AuditScoreDto
     */
    void submitScore(AuditScoreDto scoreDto);

    /**
     * @description 提交车间数据
     * @author messi
     * @date 2021-12-30 21:44
     * @param factoryAreaId 车间id
     * @param username 审核员名称
     */
    void submit(Integer factoryAreaId, String username);
}
