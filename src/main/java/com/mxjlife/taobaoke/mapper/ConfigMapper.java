package com.mxjlife.taobaoke.mapper;

import com.mxjlife.taobaoke.pojo.base.BaseQueryParams;
import com.mxjlife.taobaoke.pojo.base.ConfigInfoPO;
import com.mxjlife.taobaoke.pojo.base.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * description:
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2019/8/15 16:39
 */
public interface ConfigMapper {


    /**
     * 添加配置信息
     * @param config
     * @return
     */
    int insert(@Param("config")ConfigInfoPO config);

    /**
     * 根据配置信息的key获取配置信息
     * @param key
     * @return
     */
    ConfigInfoPO getByKey(@Param("key")String key);


    /**
     * 根据配置信息的id获取配置信息
     * @param id
     * @return
     */
    ConfigInfoPO getById(@Param("id")Integer id);

    /**
     * 按页及条件查询
     * @param params
     * @return
     */
    List<ConfigInfoPO> getConfigs(@Param("params") BaseQueryParams params);

    /**
     * 按页及条件查询数据条数
     * @param params
     * @return
     */
    int countConfigs(@Param("params") BaseQueryParams params);

    /**
     * 使用关键字全文索引匹配key,value,desc三列, 按页查询
     * @param params
     * @return
     */
    List<ConfigInfoPO> getByKeyWord(@Param("params") Map<String, Object> params);

    /**
     * 更新配置信息
     * @param config
     * @return
     */
    int updateByIdOrKey(@Param("config")ConfigInfoPO config);

    /**
     *
     * 删除配置信息, 伪删除
     * @param id
     * @return
     */
    int deleteById(@Param("id")Integer id);


}
