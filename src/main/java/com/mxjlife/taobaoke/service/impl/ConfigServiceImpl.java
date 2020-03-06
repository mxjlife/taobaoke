package com.mxjlife.taobaoke.service.impl;

import com.mxjlife.taobaoke.pojo.base.BaseQueryParams;
import com.mxjlife.taobaoke.pojo.base.PageInfo;
import com.mxjlife.taobaoke.pojo.base.ConfigInfoPO;
import com.mxjlife.taobaoke.service.ConfigService;

import java.util.List;

/**
 * description: 操作配置信息
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2019/7/5 14:57
 */
public class ConfigServiceImpl implements ConfigService {
    @Override
    public String getConfigByKey(String key) {
        return null;
    }

    @Override
    public ConfigInfoPO getConfigById(Integer id) {
        return null;
    }

    @Override
    public PageInfo<ConfigInfoPO> getConfigs(BaseQueryParams page) {
        return null;
    }

    @Override
    public int insertConfig(ConfigInfoPO config) {
        return 0;
    }

    @Override
    public int updateConfig(ConfigInfoPO config) {
        return 0;
    }

    @Override
    public int deleteConfig(Integer configId) {
        return 0;
    }
}
