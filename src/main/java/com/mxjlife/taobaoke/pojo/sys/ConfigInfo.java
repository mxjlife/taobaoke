package com.mxjlife.taobaoke.pojo.sys;

import lombok.Data;

import java.util.Date;

/**
 * description 配置信息
 *
 * @author mxj
 * email mxjlife@163.com
 * date 2019/1/2 8:58
 */
@Data
public class ConfigInfo {

    private Integer id;  //主键
    private String system;  //配置所属于的系统
    private String keyspace;  //配置所属于的功能
    private String key;  //配置名
    private String value;  //配置值
    private String desc;  //配置描述
    private Integer status;  //配置信息状态
    private String operator;  //操作者
    private Date updateTime;  //更新时间
    private Date createTime;  //创建时间
    private String remark;  //备注
}
