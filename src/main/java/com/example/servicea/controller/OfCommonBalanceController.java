package com.example.servicea.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/order-scfPc-web/ofCommonBalance")
public class OfCommonBalanceController {

    
    @PostMapping("/manageListPage")
    public Result manageListPage(@RequestBody PageRequest request) {
        // 参数校验
        if (request.getCurrent() == null || request.getCurrent() < 1) {
            return Result.error("页码不能为空且必须大于0");
        }
        if (request.getSize() == null || request.getSize() < 1 || request.getSize() > 100) {
            return Result.error("每页大小必须在1-100之间");
        }
        
        // 获取查询条件
        Map<String, Object> queryCondition = request.getQueryCondition();
        
        // 模拟分页数据
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> records = new ArrayList<>();
        
        // 模拟额度数据
        int totalCount = 50; // 总记录数
        int startIndex = (request.getCurrent() - 1) * request.getSize();
        
        for (int i = 1; i <= request.getSize() && (startIndex + i) <= totalCount; i++) {
            Map<String, Object> balance = new HashMap<>();
            int balanceIndex = startIndex + i;
            
            balance.put("id", balanceIndex);
            balance.put("balanceNo", "BL" + String.format("%06d", balanceIndex));
            balance.put("companyName", "测试公司" + balanceIndex);
            balance.put("companyId", balanceIndex * 1000L);
            balance.put("balanceAmount", 1000000.00 + balanceIndex * 10000);
            balance.put("usedAmount", balanceIndex * 5000.00);
            balance.put("availableAmount", 1000000.00 - balanceIndex * 5000.00);
            balance.put("status", balanceIndex % 3 == 0 ? "有效" : balanceIndex % 3 == 1 ? "待审核" : "已作废");
            balance.put("createTime", "2024-01-15 10:30:00");
            balance.put("updateTime", "2024-01-15 10:30:00");
            
            records.add(balance);
        }
        
        result.put("records", records);
        result.put("total", totalCount);
        result.put("current", request.getCurrent());
        result.put("size", request.getSize());
        
        // 记录日志
        System.out.println("额度分页查询 - 页码: " + request.getCurrent() + 
                         ", 每页大小: " + request.getSize() + 
                         ", 总记录数: " + totalCount);
        
        return Result.success(result);
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

