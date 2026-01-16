package com.example.servicea.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 银行管理 Controller
 * 对应前端：beehiveApi.bankController
 * 前端 baseURL: /scfpc-web
 * 前端调用：beehiveApi.bankController.distinctCodeList() / bankInfoList()
 */
@RestController
@RequestMapping("/scfpc-web/bank")
public class BankController {

    /**
     * 获取银行代码去重列表
     * 前端调用：beehiveApi.bankController.distinctCodeList()
     * 完整路径：POST /scfpc-web/bank/distinctCodeList
     */
    @PostMapping("/distinctCodeList")
    public Result distinctCodeList() {
        // 模拟银行代码列表数据
        List<Map<String, Object>> codeList = new ArrayList<>();
        
        // 模拟数据
        String[] bankCodes = {"ICBC", "CCB", "ABC", "BOC", "CMB", "SPDB", "CMBC", "HXBANK", "CIB", "GDB"};
        String[] bankNames = {"中国工商银行", "中国建设银行", "中国农业银行", "中国银行", "招商银行", 
                             "浦发银行", "民生银行", "华夏银行", "兴业银行", "广发银行"};
        
        for (int i = 0; i < bankCodes.length; i++) {
            Map<String, Object> bank = new HashMap<>();
            bank.put("bankCode", bankCodes[i]);
            bank.put("bankName", bankNames[i]);
            codeList.add(bank);
        }
        
        return Result.success(codeList);
    }

    /**
     * 获取银行信息列表
     * 前端调用：beehiveApi.bankController.bankInfoList(body)
     * 完整路径：POST /scfpc-web/bank/bankInfoList
     * 前端参数：{queryCondition: body}
     */
    @PostMapping("/bankInfoList")
    public Result bankInfoList(@RequestBody Map<String, Object> request) {
        // 获取查询条件
        Map<String, Object> queryCondition = (Map<String, Object>) request.get("queryCondition");
        
        // 模拟银行信息列表数据
        List<Map<String, Object>> bankList = new ArrayList<>();
        
        // 模拟数据
        String[] bankCodes = {"ICBC", "CCB", "ABC", "BOC", "CMB"};
        String[] bankNames = {"中国工商银行", "中国建设银行", "中国农业银行", "中国银行", "招商银行"};
        String[] branchNames = {"北京分行", "上海分行", "广州分行", "深圳分行", "杭州分行"};
        
        for (int i = 0; i < bankCodes.length; i++) {
            Map<String, Object> bank = new HashMap<>();
            bank.put("id", "BANK_" + (i + 1));
            bank.put("bankCode", bankCodes[i]);
            bank.put("bankName", bankNames[i]);
            bank.put("branchName", branchNames[i]);
            bank.put("accountNo", "6222" + String.format("%012d", 1000000000L + i));
            bank.put("accountName", "测试账户" + (i + 1));
            bank.put("status", i % 2 == 0 ? "ACTIVE" : "INACTIVE");
            bank.put("createTime", "2024-01-15 10:30:00");
            bank.put("updateTime", "2024-01-15 10:30:00");
            
            // 如果查询条件中有 bankCode，进行过滤
            if (queryCondition != null) {
                String queryBankCode = (String) queryCondition.get("bankCode");
                if (queryBankCode != null && !queryBankCode.isEmpty()) {
                    if (!bankCodes[i].equals(queryBankCode) && !bankNames[i].contains(queryBankCode)) {
                        continue;
                    }
                }
            }
            
            bankList.add(bank);
        }
        
        return Result.success(bankList);
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

