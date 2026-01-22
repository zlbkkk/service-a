package com.example.servicea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

/**
 * 订单管理 Controller
 * 对应前端菜单：订单管理
 * 调用 beehive-order-scfPc 服务的订单接口
 */
@RestController
@RequestMapping("/order-scfPc-web/ofOrder")
public class OrderManageController {

    @Autowired
    private RestTemplate restTemplate;

    // beehive-order-scfPc 服务的基础URL
    private static final String BASE_URL = "http://localhost:17231/order-scfPc-web";

    /**
     * 分页查询订单列表
     * 
     * @param request 分页查询请求参数
     * @return 订单分页结果
     */
    @PostMapping("/pageOrder")
    public OrderResult pageOrder(@RequestBody OrderPageRequest request) {
        // 参数校验
        if (request.getCurrent() == null || request.getCurrent() < 1) {
            return OrderResult.error("页码不能为空且必须大于0");
        }
        if (request.getSize() == null || request.getSize() < 1 || request.getSize() > 100) {
            return OrderResult.error("每页大小必须在1-100之间");
        }

        String url = BASE_URL + "/ofOrder/pageOrder";
        
        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // 将请求体转换为 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(request);
            
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            
            // 发送 POST 请求
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
            );
            
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.get("data") != null) {
                return OrderResult.success(responseBody.get("data"));
            } else {
                return OrderResult.error("调用远程服务失败");
            }
        } catch (Exception e) {
            // 异常处理
            System.err.println("调用分页查询订单接口失败: " + e.getMessage());
            e.printStackTrace();
            return OrderResult.error("调用远程服务失败: " + e.getMessage());
        }
    }

    // 内部类：分页请求参数
    public static class OrderPageRequest {
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
    public static class OrderResult {
        private Integer code;
        private String message;
        private Object data;

        public static OrderResult success(Object data) {
            OrderResult result = new OrderResult();
            result.code = 200;
            result.message = "success";
            result.data = data;
            return result;
        }
        
        public static OrderResult error(String message) {
            OrderResult result = new OrderResult();
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
