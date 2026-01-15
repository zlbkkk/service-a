package com.example.servicea.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 订单管理 Controller
 * 对应前端菜单：资产管理 > 订单管理
 */
@RestController
@RequestMapping("/order-scfPc-web/ofOrder")
public class OfOrderController {

    /**
     * 分页查询订单列表
     * 前端调用：orderApi.ofOrderController.pageOrder()
     * 菜单路径：资产管理 > 订单管理 (/orderManage)
     */
    @PostMapping("/page")
    public Result pageOrder(@RequestBody PageRequest request) {
        // 参数校验
        if (request.getCurrent() == null || request.getCurrent() < 1) {
            return Result.error("页码不能为空且必须大于0");
        }
        if (request.getSize() == null || request.getSize() < 1 || request.getSize() > 100) {
            return Result.error("每页大小必须在1-100之间");
        }
        
        // 获取查询条件
        Map<String, Object> queryCondition = request.getQueryCondition();
        String fuzzySourceOrderNo = queryCondition != null ? (String) queryCondition.get("fuzzySourceOrderNo") : null;
        String buyerCompanyId = queryCondition != null ? (String) queryCondition.get("buyerCompanyId") : null;
        String sellerCompanyId = queryCondition != null ? (String) queryCondition.get("sellerCompanyId") : null;
        List<String> tradeStageList = queryCondition != null ? (List<String>) queryCondition.get("tradeStageList") : null;
        
        // 模拟分页数据
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> records = new ArrayList<>();
        
        // 模拟订单数据（根据查询条件过滤）
        int totalCount = 100; // 总记录数
        int startIndex = (request.getCurrent() - 1) * request.getSize();
        
        for (int i = 1; i <= request.getSize(); i++) {
            int orderIndex = startIndex + i;
            if (orderIndex > totalCount) break;
            
            Map<String, Object> order = new HashMap<>();
            String orderNo = "ORD-2024-" + String.format("%04d", orderIndex);
            
            // 模拟订单编号过滤
            if (fuzzySourceOrderNo != null && !fuzzySourceOrderNo.isEmpty()) {
                if (!orderNo.contains(fuzzySourceOrderNo)) {
                    continue;
                }
            }
            
            // 确定交易阶段（循环使用三种状态）
            String tradeStage = orderIndex % 3 == 0 ? "INVOICE" : (orderIndex % 3 == 1 ? "ORDER" : "ARRIVAL");
            
            // 模拟交易阶段过滤
            if (tradeStageList != null && !tradeStageList.isEmpty()) {
                if (!tradeStageList.contains(tradeStage)) {
                    continue;
                }
            }
            
            order.put("id", "ORDER_" + orderIndex);
            order.put("sourceOrderNo", orderNo);
            order.put("orderName", "测试订单" + orderIndex);
            order.put("amount", 100000.00 + orderIndex * 1000);
            order.put("financedAmount", 50000.00 + orderIndex * 500);
            order.put("tradeStage", tradeStage);
            order.put("buyerCompanyName", "测试买方企业" + (orderIndex % 5 + 1));
            order.put("sellerCompanyName", "测试卖方企业" + (orderIndex % 5 + 1));
            order.put("contractNo", "CONTRACT-" + orderIndex);
            order.put("contractName", "测试合同" + orderIndex);
            order.put("contractSignDate", "2024-01-15");
            order.put("contractAmount", 200000.00);
            order.put("dueAmount", 50000.00);
            order.put("otherDeductAmount", 0.00);
            order.put("planPayDate", "2024-12-31");
            order.put("planDate", "2024-01-01");
            order.put("expireDate", "2024-12-31");
            order.put("commitUserName", "张三");
            order.put("updateTime", "2024-01-15 10:30:00");
            order.put("isCanDel", orderIndex % 2 == 0 ? "Y" : "N"); // 偶数订单可删除
            records.add(order);
        }
        
        result.put("records", records);
        result.put("total", totalCount);
        result.put("current", request.getCurrent());
        result.put("size", request.getSize());
        
        return Result.success(result);
    }

    /**
     * 查询订单汇总信息
     */
    @PostMapping("/getOrderSummary")
    public Result getOrderSummary(@RequestBody Map<String, Object> queryCondition) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("sumCount", 100);
        summary.put("sumAmount", 10000000.00);
        summary.put("applySumCount", 50);
        summary.put("applySumAmount", 5000000.00);
        return Result.success(summary);
    }

    /**
     * 删除订单
     */
    @PostMapping("/delOrder")
    public Result delOrder(@RequestBody Map<String, Object> params) {
        String orderId = (String) params.get("orderId");
        // 模拟删除操作
        return Result.success("删除成功");
    }

    /**
     * 导出订单台账
     */
    @PostMapping("/exportOrder")
    public Result exportOrder(@RequestBody Map<String, Object> params) {
        // 模拟导出操作
        return Result.success("导出成功");
    }

    /**
     * 导出订单影像件
     */
    @PostMapping("/exportOrderMedia")
    public Result exportOrderMedia(@RequestBody Map<String, Object> params) {
        // 模拟导出操作
        return Result.success("导出成功");
    }

    // 内部类：分页请求参数
    public static class PageRequest {
        private Integer current;
        private Integer size;
        private Map<String, Object> queryCondition;

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Map<String, Object> getQueryCondition() {
            return queryCondition;
        }

        public void setQueryCondition(Map<String, Object> queryCondition) {
            this.queryCondition = queryCondition;
        }
    }

    // 内部类：统一返回结果
    public static class Result {
        private Integer code;
        private String message;
        private Object data;

        public static Result success(Object data) {
            Result result = new Result();
            result.code = 200;
            result.message = "success";
            result.data = data;
            return result;
        }
        
        public static Result error(String message) {
            Result result = new Result();
            result.code = 400;
            result.message = message;
            result.data = null;
            return result;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
