package com.example.servicea.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息 Dubbo 服务提供者
 * 
 * 提供用户信息查询相关的 RPC 接口
 * 供其他微服务通过 Dubbo 调用
 * 
 * @author system
 * @version 1.0
 */
@DubboService(version = "1.0.0", timeout = 3000)
@Component
public class UserInfoProvider {

    /**
     * 根据用户ID获取用户基本信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    public Map<String, Object> getUserInfo(Long userId) {
        Map<String, Object> userInfo = new HashMap<>();
        
        userInfo.put("userId", userId);
        userInfo.put("username", "user_" + userId);
        userInfo.put("email", "user" + userId + "@example.com");
        userInfo.put("phone", "138****" + (userId % 10000));
        userInfo.put("status", 1);
        userInfo.put("createTime", "2025-01-01 00:00:00");
        
        return userInfo;
    }

    /**
     * 验证用户是否存在
     * 
     * @param userId 用户ID
     * @return 是否存在
     */
    public Boolean checkUserExists(Long userId) {
        // 模拟验证用户存在性
        return userId != null && userId > 0;
    }

    /**
     * 获取用户等级信息（修改：增加返回字段）
     * 
     * @param userId 用户ID
     * @return 用户等级信息
     */
    public Map<String, Object> getUserLevel(Long userId) {
        Map<String, Object> levelInfo = new HashMap<>();
        
        levelInfo.put("userId", userId);
        levelInfo.put("level", 3);
        levelInfo.put("levelName", "黄金会员");
        levelInfo.put("points", 1500);
        levelInfo.put("nextLevelPoints", 3000);
        levelInfo.put("discount", 0.95);  // 新增：会员折扣
        levelInfo.put("freeShipping", true);  // 新增：是否包邮
        levelInfo.put("levelUpgradeDate", "2025-06-01");  // 新增：升级日期
        
        return levelInfo;
    }

    /**
     * 更新用户信息（新增方法）
     * 
     * @param userId 用户ID
     * @param updateData 更新数据
     * @return 更新结果
     */
    public Map<String, Object> updateUserInfo(Long userId, Map<String, Object> updateData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("success", true);
        result.put("userId", userId);
        result.put("updatedFields", updateData.keySet());
        result.put("updateTime", "2026-01-14 17:00:00");
        result.put("message", "用户信息更新成功");
        
        return result;
    }

    /**
     * 获取用户积分明细（新增方法）
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 积分明细列表
     */
    public Map<String, Object> getUserPointsHistory(Long userId, Integer pageNum, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("userId", userId);
        result.put("totalPoints", 1500);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("totalRecords", 25);
        result.put("message", "查询成功");
        
        return result;
    }
}
