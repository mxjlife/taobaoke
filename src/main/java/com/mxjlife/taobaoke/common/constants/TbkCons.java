package com.mxjlife.taobaoke.common.constants;

/**
 * description:
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2020/2/28 11:12
 */
public interface TbkCons {


    /////////////////  缓存配置的数据的key   //////////////

    /** TOP分配给应用的AppKey */
    String CACHE_KEY_TBK_APP_KEY = "TBK_APP_KEY";
    String CACHE_KEY_TBK_APP_SECRET = "TBK_APP_SECRET";




    /** 签名的摘要算法，可选值为：hmac，md5 */
    String TBK_REQ_PARAM_SIGN_METHOD = "md5";
    /** 响应格式。默认为xml格式，可选值：xml，json。 */
    String TBK_REQ_PARAM_FORMAT = "json";
    /** API协议版本，可选值：2.0 */
    String TBK_REQ_PARAM__V = "2.0";
    /** 是否采用精简JSON返回格式，仅当format=json时有效，默认值为：false */
    boolean TBK_REQ_PARAM_SIMPLIFY = true;
}
