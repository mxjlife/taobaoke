package com.mxjlife.taobaoke.service;

import com.mxjlife.taobaoke.pojo.sys.PageInfo;
import com.mxjlife.taobaoke.pojo.sys.ConfigInfo;

import java.util.List;

/**
 * description 配置信息管理
 *
 * @author mxj
 * email mxjlife@163.com
 * date 2019/1/2 8:56
 */
public interface ConfigService {

    /**
     * 根据配置信息的key获取配置信息
     * @param key
     * @return
     */
    String getConfigByKey(String key);

    /**
     * 按页获取所有配置信息
     * @param page
     * @return
     */
    List<ConfigInfo> getAllConfigs(PageInfo page);

    /**
     * 添加配置信息
     * @param config
     * @return
     */
    int insertConfig(ConfigInfo config);

    /**
     * 更新配置信息
     * @param config
     * @return
     */
    int updateConfig(ConfigInfo config);

    /**
     *
     * 删除配置信息
     * @param configId
     * @return
     */
    int deleteConfig(Integer configId);
}
