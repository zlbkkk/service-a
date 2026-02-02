package com.example.servicea.provider;

import com.example.servicea.mapper.OfBalanceOccupyRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 余额占用服务 Dubbo 提供者
 * 
 * 封装 OfBalanceOccupyRecordMapper 的调用，暴露为 Dubbo 服务
 * 用于测试 Mapper 层变更的跨项目影响识别
 * 
 * @author system
 * @version 1.0
 */
@DubboService(version = "1.0.0", timeout = 10000, retries = 2)
@Component
public class BalanceOccupyProvider {

    @Autowired
    private OfBalanceOccupyRecordMapper balanceOccupyRecordMapper;

    /**
     * 根据ID查询占用记录
     * 
     * @param id 记录ID
     * @return 占用记录
     */
    public Map<String, Object> getOccupyRecordById(Long id) {
        return balanceOccupyRecordMapper.selectById(id);
    }

    /**
     * 【新增方法】分页查询占用明细
     * 调用 OfBalanceOccupyRecordMapper.occupationDetailPage
     * 
     * @param userId 用户ID
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public Map<String, Object> queryOccupationDetailPage(
            Long userId, String status, int pageNum, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        
        // 调用 Mapper 新增的分页查询方法
        List<Map<String, Object>> records = balanceOccupyRecordMapper.occupationDetailPage(
                userId, status, pageNum, pageSize);
        
        // 查询总数
        int total = balanceOccupyRecordMapper.countByUserIdAndStatus(userId, status);
        
        result.put("records", records);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("pages", (total + pageSize - 1) / pageSize);
        
        return result;
    }

    /**
     * 根据订单ID查询占用记录列表
     * 
     * @param orderId 订单ID
     * @return 占用记录列表
     */
    public List<Map<String, Object>> getOccupyRecordsByOrderId(Long orderId) {
        return balanceOccupyRecordMapper.selectByOrderId(orderId);
    }

    /**
     * 【新增方法】批量查询占用详情
     * 
     * @param ids 记录ID列表
     * @return 占用详情列表
     */
    public List<Map<String, Object>> batchGetOccupyRecords(List<Long> ids) {
        return balanceOccupyRecordMapper.selectBatchByIds(ids);
    }
}
