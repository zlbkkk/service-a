package com.example.servicea.controller;

import com.example.servicea.client.CertificationApplyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 授信申请管理 Controller
 * 用于调用 beehive-order-scfPc 服务的授信申请相关接口
 */
@RestController
@RequestMapping("/order-scfPc-web/OfCertificationApply")
public class OfCertificationApplyController {

    @Autowired
    private CertificationApplyClient certificationApplyClient;

    /**
     * 删除授信申请
     * 
     * @param id 授信申请ID
     * @return 删除结果
     */
    @PostMapping("/delete")
    public CertificationApplyResult delete(@RequestParam Long id) {
        // 参数校验
        if (id == null || id <= 0) {
            return CertificationApplyResult.error("授信申请ID不能为空且必须大于0");
        }
        
        try {
            // 调用远程服务删除授信申请
            CertificationApplyClient.ApiResponse<Boolean> response = certificationApplyClient.deleteCertificationApply(id);
            
            if (response != null && response.getCode() == 200) {
                return CertificationApplyResult.success(response.getData(), "删除成功");
            } else {
                String errorMsg = response != null ? response.getMessage() : "删除失败";
                return CertificationApplyResult.error(errorMsg);
            }
        } catch (Exception e) {
            return CertificationApplyResult.error("删除授信申请失败: " + e.getMessage());
        }
    }

    // 内部类：统一返回结果
    public static class CertificationApplyResult {
        private Integer code;
        private String message;
        private Object data;

        public static CertificationApplyResult success(Object data, String message) {
            CertificationApplyResult result = new CertificationApplyResult();
            result.code = 200;
            result.message = message;
            result.data = data;
            return result;
        }
        
        public static CertificationApplyResult error(String message) {
            CertificationApplyResult result = new CertificationApplyResult();
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
