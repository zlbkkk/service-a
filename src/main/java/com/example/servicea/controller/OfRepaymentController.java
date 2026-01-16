package com.example.servicea.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/order-scfPc-web/ofRepayment")
public class OfRepaymentController {

    @PostMapping("/paymentManagementPage")
    public Result paymentManagementPage(@RequestBody PageRequest request) {
        if (request.getCurrent() == null || request.getCurrent() < 1) {
            return Result.error("页码不能为空且必须大于0");
        }
        if (request.getSize() == null || request.getSize() < 1 || request.getSize() > 100) {
            return Result.error("每页大小必须在1-100之间");
        }
        
        Map<String, Object> queryCondition = request.getQueryCondition();
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> records = new ArrayList<>();
        
        int totalCount = 50;
        int startIndex = (request.getCurrent() - 1) * request.getSize();
        
        for (int i = 1; i <= request.getSize(); i++) {
            int recordIndex = startIndex + i;
            if (recordIndex > totalCount) break;
            
            Map<String, Object> record = new HashMap<>();
            record.put("id", "PAYMENT_" + recordIndex);
            record.put("transactionNo", "TXN-2024-" + String.format("%04d", recordIndex));
            record.put("transactionAmount", 100000.00 + recordIndex * 1000);
            record.put("paidAmount", 50000.00 + recordIndex * 500);
            record.put("remainingAmount", 50000.00 + recordIndex * 500);
            record.put("spyCompanyName", "供应商企业" + (recordIndex % 5 + 1));
            record.put("ceCompanyName", "核心企业" + (recordIndex % 5 + 1));
            record.put("cptCompanyName", "资金方企业" + (recordIndex % 5 + 1));
            record.put("planPayDate", "2024-12-31");
            record.put("actualPayDate", recordIndex % 2 == 0 ? "2024-01-15" : null);
            record.put("status", recordIndex % 3 == 0 ? "PAID" : (recordIndex % 3 == 1 ? "PENDING" : "OVERDUE"));
            record.put("createTime", "2024-01-15 10:30:00");
            record.put("updateTime", "2024-01-15 10:30:00");
            records.add(record);
        }
        
        result.put("records", records);
        result.put("total", totalCount);
        result.put("current", request.getCurrent());
        result.put("size", request.getSize());
        
        return Result.success(result);
    }

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

