package com.example.servicea.provider;

import com.example.servicea.mapper.OfProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 项目数据 Dubbo Provider
 */
@Service
public class OfProjectProvider {

    @Autowired
    private OfProjectMapper ofProjectMapper;

    /**
     * 分页查询项目列表
     */
    public List<Map<String, Object>> listPage(Map<String, Object> params) {
        return ofProjectMapper.listPage(params);
    }
}
