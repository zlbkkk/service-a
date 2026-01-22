package com.example.servicea.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 融资交易管理 Controller
 * 对应前端菜单：融资审核
 */
@RestController
@RequestMapping("/order-scfPc-web/ofTransaction")
public class OfTransactionController {

   
    @PostMapping("/page")
    public TransactionResult queryPage(@RequestBody TransactionPageRequest request) {
        // 参数校验
        if (request.getCurrent() == null || request.getCurrent() < 1) {
            return TransactionResult.error("页码不能为空且必须大于0");
        }
        if (request.getSize() == null || request.getSize() < 1 || request.getSize() > 100) {
            return TransactionResult.error("每页大小必须在1-100之间");
        }
        
        // 模拟数据
        List<Map<String, Object>> records = new ArrayList<>();
        int totalCount = 350;  // 增加总数到350条
        int start = (request.getCurrent() - 1) * request.getSize();
        
        // 企业名称池
        String[] companies = {
            "华为技术有限公司", "腾讯科技有限公司", "阿里巴巴集团", "百度在线网络技术公司",
            "京东集团", "美团科技有限公司", "字节跳动科技有限公司", "小米科技有限公司",
            "比亚迪股份有限公司", "宁德时代新能源科技股份有限公司", "中兴通讯股份有限公司",
            "海康威视数字技术股份有限公司", "格力电器股份有限公司", "美的集团股份有限公司",
            "三一重工股份有限公司", "中国建筑集团有限公司", "中国中车股份有限公司",
            "上海汽车集团股份有限公司", "中国石油化工集团公司", "中国移动通信集团公司"
        };
        
        // 产品类型池
        String[] productTypes = {
            "订单融资", "应收账款融资", "存货融资", "预付款融资", 
            "保理融资", "票据融资", "信用贷款", "抵押贷款"
        };
        
        // 银行池
        String[] banks = {
            "中国工商银行", "中国建设银行", "中国农业银行", "中国银行",
            "交通银行", "招商银行", "浦发银行", "中信银行", "民生银行", "光大银行"
        };
        
        for (int i = 1; i <= request.getSize() && (start + i) <= totalCount; i++) {
            int idx = start + i;
            Map<String, Object> tx = new HashMap<>();
            
            // 基础字段
            tx.put("id", "TRANS_" + String.format("%010d", idx));
            tx.put("transactionNo", "RZJY-2025-" + String.format("%08d", idx));
            tx.put("sedCompanyName", companies[idx % companies.length]);
            
            // 产品信息
            tx.put("productType", productTypes[idx % productTypes.length]);
            tx.put("productName", productTypes[idx % productTypes.length] + "产品");
            
            // 金额字段 - 更真实的金额范围
            double[] amountRanges = {100000, 500000, 1000000, 2000000, 5000000, 10000000};
            double baseAmount = amountRanges[idx % amountRanges.length];
            double amount = baseAmount + (idx * 8888.88);
            tx.put("applyAmount", Math.round(amount * 100.0) / 100.0);  // 申请金额
            tx.put("approvedAmount", Math.round(amount * 0.95 * 100.0) / 100.0);  // 批准金额(95%)
            tx.put("usedAmount", Math.round(amount * 0.6 * 100.0) / 100.0);  // 已用金额(60%)
            
            // 利率和期限
            double interestRate = 4.5 + (idx % 10) * 0.1;  // 4.5% - 5.4%
            tx.put("interestRate", Math.round(interestRate * 100.0) / 100.0);
            tx.put("loanTerm", (idx % 12 + 1) * 3);  // 3-36个月
            tx.put("loanTermUnit", "月");
            
            // 状态字段 - 更多状态
            String[] statuses = {"PENDING", "APPROVED", "REJECTED", "IN_REVIEW", "DISBURSED", "COMPLETED"};
            String[] statusNames = {"待审核", "已批准", "已拒绝", "审核中", "已放款", "已完成"};
            int statusIdx = idx % statuses.length;
            tx.put("status", statuses[statusIdx]);
            tx.put("statusName", statusNames[statusIdx]);
            
            // 银行信息
            tx.put("bankName", banks[idx % banks.length]);
            tx.put("bankCode", "BANK_" + String.format("%03d", idx % banks.length + 1));
            
            // 时间字段
            int day = (idx % 28) + 1;
            int hour = (idx % 24);
            int minute = (idx % 60);
            tx.put("applyTime", String.format("2025-01-%02d %02d:%02d:00", day, hour, minute));
            tx.put("createTime", String.format("2025-01-%02d %02d:%02d:00", day, hour, minute));
            
            if (statusIdx >= 1) {  // 已审核的添加审核时间
                tx.put("approveTime", String.format("2025-01-%02d %02d:%02d:00", day, (hour + 2) % 24, minute));
            }
            
            if (statusIdx >= 4) {  // 已放款的添加放款时间
                tx.put("disburseTime", String.format("2025-01-%02d %02d:%02d:00", day, (hour + 3) % 24, minute));
            }
            
            // 业务员和审核人
            tx.put("salesPerson", "业务员" + (idx % 20 + 1));
            tx.put("approver", statusIdx >= 1 ? "审核员" + (idx % 10 + 1) : null);
            
            // 备注
            if (statusIdx == 2) {  // 拒绝状态添加拒绝原因
                String[] rejectReasons = {"资质不符", "额度不足", "风险过高", "资料不全"};
                tx.put("remark", rejectReasons[idx % rejectReasons.length]);
            } else {
                tx.put("remark", "");
            }
            
            records.add(tx);
        }
        
        // 支持查询条件过滤
        if (request.getQueryCondition() != null && !request.getQueryCondition().isEmpty()) {
            records = filterRecords(records, request.getQueryCondition());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", totalCount);
        result.put("current", request.getCurrent());
        result.put("size", request.getSize());
        
        return TransactionResult.success(result);
    }
    
    /**
     * 根据查询条件过滤记录
     */
    private List<Map<String, Object>> filterRecords(List<Map<String, Object>> records, Map<String, Object> condition) {
        List<Map<String, Object>> filtered = new ArrayList<>();
        
        for (Map<String, Object> record : records) {
            boolean match = true;
            
            // 按企业名称过滤
            if (condition.containsKey("sedCompanyName")) {
                String name = (String) condition.get("sedCompanyName");
                if (name != null && !name.isEmpty()) {
                    String recordName = (String) record.get("sedCompanyName");
                    if (!recordName.contains(name)) {
                        match = false;
                    }
                }
            }
            
            // 按状态过滤
            if (condition.containsKey("status")) {
                String status = (String) condition.get("status");
                if (status != null && !status.isEmpty()) {
                    if (!status.equals(record.get("status"))) {
                        match = false;
                    }
                }
            }
            
            // 按产品类型过滤
            if (condition.containsKey("productType")) {
                String productType = (String) condition.get("productType");
                if (productType != null && !productType.isEmpty()) {
                    if (!productType.equals(record.get("productType"))) {
                        match = false;
                    }
                }
            }
            
            if (match) {
                filtered.add(record);
            }
        }
        
        return filtered;
    }

    // 内部类：分页请求参数
    public static class TransactionPageRequest {
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
    public static class TransactionResult {
        private Integer code;
        private String message;
        private Object data;

        public static TransactionResult success(Object data) {
            TransactionResult result = new TransactionResult();
            result.code = 200;
            result.message = "success";
            result.data = data;
            return result;
        }
        
        public static TransactionResult error(String message) {
            TransactionResult result = new TransactionResult();
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
