package com.mxjlife.taobaoke.pojo;

import com.mxjlife.taobaoke.common.cache.GuavaCache;
import com.mxjlife.taobaoke.common.constants.TbkCons;
import com.mxjlife.taobaoke.common.util.DateUtils;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * description:
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2020/2/27 15:00
 */
@Data
public class TbkBaseRequest {

    /** API接口名称 */
    private String method;
    /** TOP分配给应用的AppKey */
    private String app_key;
    /** 被调用的目标AppKey，仅当被调用的API为第三方ISV提供时有效 */
    private String target_app_key;
    /** 签名的摘要算法，可选值为：hmac，md5 */
    private String sign_method;
    /** API输入参数签名结果，签名算法介绍请点击这里 */
    private String sign;
    /** 用户登录授权成功后，TOP颁发给应用的授权信息，详细介绍请点击这里。当此API的标签上注明：“需要授权”，则此参数必传；“不需要授权”，则此参数不需要传；“可选授权”，则此参数为可选 */
    private String session;
    /** 时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8，例如：2015-01-01 12:00:00。淘宝API服务端允许客户端请求最大时间误差为10分钟 */
    private String timestamp;
    /** 响应格式。默认为xml格式，可选值：xml，json。 */
    private String format;
    /** API协议版本，可选值：2.0 */
    private String v;
    /** 合作伙伴身份标识 */
    private String partner_id;
    /** 是否采用精简JSON返回格式，仅当format=json时有效，默认值为：false */
    private boolean simplify;

    public TbkBaseRequest(String method) {
        this.method = method;
//        this.app_key = GuavaCache.get(TbkCons.CACHE_KEY_TBK_APP_KEY);
        this.sign_method = TbkCons.TBK_REQ_PARAM_SIGN_METHOD;
        this.timestamp = DateFormatUtils.format(new Date(),DateUtils.DEFAULT_PATTERN);
        this.format = TbkCons.TBK_REQ_PARAM_FORMAT;
        this.v = TbkCons.TBK_REQ_PARAM__V;
        this.simplify = TbkCons.TBK_REQ_PARAM_SIMPLIFY;
    }
}
