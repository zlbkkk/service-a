package com.example.servicea.provider;

import com.example.servicea.mapper.OfProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 项目数据 Dubbo Provider
 * 对外提供项目数据访问服务
 */
@Service
public class OfProjectProvider {

    @Autowired
    private OfProjectMapper ofProjectMapper;

    /**
     * 分页查询项目列表
     * @param params 查询参数
     * @return 项目列表
     */
    public List<Map<String, Object>> listPage(Map<String, Object> params) {
        return ofProjectMapper.listPage(params);
    }

    /**
     * 新增项目
     * @param project 项目数据
     * @return 操作结果
     */
    public boolean addProject(Map<String, Object> project) {
        int rows = ofProjectMapper.addProject(project);
        return rows > 0;
    }

    /**
     * 更新项目
     * @param project 项目数据
     * @return 操作结果
     */
    public boolean updateProject(Map<String, Object> project) {
        int rows = ofProjectMapper.updateProject(project);
        return rows > 0;
    }

    /**
     * 根据买方查询项目列表
     * @param buyerId 买方ID
     * @return 项目列表
     */
    public List<Map<String, Object>> listProjectByBuyer(String buyerId) {
        return ofProjectMapper.listProjectByBuyer(buyerId);
    }

    /**
     * 获取在线项目信息
     * @param projectId 项目ID
     * @return 项目信息
     */
    public Map<String, Object> getOnlineProject(String projectId) {
        return ofProjectMapper.getOnlineProject(projectId);
    }

    /**
     * 同步贷后材料至资金方
     * @param projectId 项目ID
     * @return 同步结果
     */
    public boolean syncPostLoan(String projectId) {
        int rows = ofProjectMapper.syncPostLoan(projectId);
        return rows > 0;
    }
}
