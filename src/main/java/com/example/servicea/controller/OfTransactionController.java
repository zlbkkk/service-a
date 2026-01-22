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

    /**
     * 分页查询融资交易列表
     * 前端调用：orderApi.ofTransactionController.ofTransactionPage()
     * 菜单路径：融资审核 (/standingBook)
     */
    @PostMapping("/page")
    public Result page(@RequestBody PageRequest request) {
        // 参数校验
        if (request.getCurrent() == null || request.getCurrent() < 1) {
            return Result.error("页码不能为空且必须大于0");
        }
        if (request.getSize() == null || request.getSize() < 1 || request.getSize() > 100) {
            return Result.error("每页大小必须在1-100之间");
        }
        
        // 获取查询条件
        Map<String, Object> queryCondition = request.getQueryCondition();
        String ceAuditBelongRegion = queryCondition != null ? (String) queryCondition.get("ceAuditBelongRegion") : null;
        String transactionNo = queryCondition != null ? (String) queryCondition.get("transactionNo") : null;
        String sedCompanyName = queryCondition != null ? (String) queryCondition.get("sedCompanyName") : null;
        String fuzzyBillPackName = queryCondition != null ? (String) queryCondition.get("fuzzyBillPackName") : null;
        String frameContractName = queryCondition != null ? (String) queryCondition.get("frameContractName") : null;
        String transactionType = queryCondition != null ? (String) queryCondition.get("transactionType") : null;
        String mainFlowStatus = queryCondition != null ? (String) queryCondition.get("mainFlowStatus") : null;
        
        // 模拟分页数据
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> records = new ArrayList<>();
        
        // 模拟融资交易数据
        int totalCount = 150; // 总记录数
        int startIndex = (request.getCurrent() - 1) * request.getSize();
        
        for (int i = 1; i <= request.getSize(); i++) {
            int transactionIndex = startIndex + i;
            if (transactionIndex > totalCount) break;
            
            Map<String, Object> transaction = new HashMap<>();
            String txNo = "TX-2024-" + String.format("%06d", transactionIndex);
            
            // 模拟融资申请编号过滤
            if (transactionNo != null && !transactionNo.isEmpty()) {
                if (!txNo.contains(transactionNo)) {
                    continue;
                }
            }
            
            // 确定融资类型（循环使用三种类型）
            String txType = transactionIndex % 3 == 0 ? "ORDER" : (transactionIndex % 3 == 1 ? "ARRIVAL" : "DEALER");
            String txTypeDisplayName = txType.equals("ORDER") ? "订单融资" : (txType.equals("ARRIVAL") ? "到货融资" : "经销商融资");
            
            // 模拟融资类型过滤
            if (transactionType != null && !transactionType.isEmpty()) {
                if (!txType.equals(transactionType)) {
                    continue;
                }
            }
            
            // 确定审核状态（循环使用多种状态）
            String flowStatus = transactionIndex % 4 == 0 ? "PENDING" : 
                               (transactionIndex % 4 == 1 ? "APPROVED" : 
                               (transactionIndex % 4 == 2 ? "REJECTED" : "PROCESSING"));
            String flowStatusDisplayName = flowStatus.equals("PENDING") ? "待审核" : 
                                          (flowStatus.equals("APPROVED") ? "已通过" : 
                                          (flowStatus.equals("REJECTED") ? "已拒绝" : "审核中"));
            
            // 模拟审核状态过滤
            if (mainFlowStatus != null && !mainFlowStatus.isEmpty()) {
                if (!flowStatus.equals(mainFlowStatus)) {
                    continue;
                }
            }
            
            // 确定融资企业名称
            String companyName = "融资企业" + (transactionIndex % 10 + 1) + "有限公司";
            
            // 模拟融资企业名称过滤
            if (sedCompanyName != null && !sedCompanyName.isEmpty()) {
                if (!companyName.contains(sedCompanyName)) {
                    continue;
                }
            }
            
            // 确定所属组织
            String belongRegion = "区域" + (transactionIndex % 5 + 1);
            
            // 模拟所属组织过滤
            if (ceAuditBelongRegion != null && !ceAuditBelongRegion.isEmpty()) {
                if (!belongRegion.contains(ceAuditBelongRegion)) {
                    continue;
                }
            }
            
            transaction.put("id", "TRANS_" + transactionIndex);
            transaction.put("transactionNo", txNo);
            transaction.put("sedCompanyName", companyName);
            transaction.put("billPackName", "单据包" + transactionIndex);
            transaction.put("frameContractName", "框架合同" + transactionIndex);
            transaction.put("ceAuditBelongRegion", belongRegion);
            transaction.put("ceAuditUserName", "审核员" + (transactionIndex % 3 + 1));
            
            // 融资类型对象
            Map<String, String> transactionTypeObj = new HashMap<>();
            transactionTypeObj.put("dictParam", txType);
            transactionTypeObj.put("displayName", txTypeDisplayName);
            transaction.put("transactionType", transactionTypeObj);
            transaction.put("transactionTypeDictParam", txType);
            transaction.put("transactionTypeDisplayName", txTypeDisplayName);
            
            // 主流程状态对象
            Map<String, String> mainFlowStatusObj = new HashMap<>();
            mainFlowStatusObj.put("dictParam", flowStatus);
            mainFlowStatusObj.put("displayName", flowStatusDisplayName);
            transaction.put("mainFlowStatus", mainFlowStatusObj);
            transaction.put("statusDictParam", flowStatus);
            transaction.put("statusDisplayName", flowStatusDisplayName);
            transaction.put("mainFlowStatusParam", flowStatus);
            
            // 交易状态对象
            Map<String, String> tradeStatusObj = new HashMap<>();
            tradeStatusObj.put("dictParam", "NORMAL");
            tradeStatusObj.put("displayName", "正常");
            transaction.put("tradeStatus", tradeStatusObj);
            transaction.put("tradeStatusDictParam", "NORMAL");
            transaction.put("tradeStatusDisplayName", "正常");
            
            // 其他字段
            transaction.put("amount", 500000.00 + transactionIndex * 10000);
            transaction.put("billPackNo", "BP-" + transactionIndex);
            transaction.put("createTime", "2024-01-15 10:30:00");
            transaction.put("updateTime", "2024-01-15 10:30:00");
            
            records.add(transaction);
        }
        
        result.put("records", records);
        result.put("total", totalCount);
        result.put("current", request.getCurrent());
        result.put("size", request.getSize());
        
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
