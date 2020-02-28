package com.mxjlife.taobaoke.mapper;

import com.mxjlife.taobaoke.pojo.sys.ConfigInfo;
import com.mxjlife.taobaoke.pojo.sys.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    int insert(ConfigInfo config);

    /**
     * 根据配置信息的key获取配置信息
     * @param key
     * @return
     */
    ConfigInfo getByKey(String key);

    /**
     * 按页及条件查询
     * @param page
     * @return
     */
    List<ConfigInfo> getDataByPage(@Param("page") PageInfo page);

    /**
     * 按页及条件查询数据条数
     * @param page
     * @return
     */
    int getCountByPage(@Param("page") PageInfo page);

    /**
     * 使用关键字全文索引匹配key,value,desc三列, 按页查询
     * @param page
     * @return
     */
    List<ConfigInfo> getByKeyWord(@Param("page") PageInfo page, @Param("keyWord") String keyWord);

    /**
     * 更新配置信息
     * @param config
     * @return
     */
    int updateByIdOrKey(ConfigInfo config);

    /**
     *
     * 删除配置信息, 伪删除
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     *
     * 删除配置信息, 伪删除
     * @return
     */
    ConfigInfo getForUpdate(Integer id);

}
