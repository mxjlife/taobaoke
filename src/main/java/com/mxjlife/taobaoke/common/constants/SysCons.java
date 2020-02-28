package com.mxjlife.taobaoke.common.constants;

/**
 * description 系统内部用到的常量值
 *
 * @author mxj
 * email mxjlife@163.com
 * date 2019/1/8 17:06
 */
public interface SysCons {



    // 缓存中的信息key
    // 登陆过滤中忽略的uri在缓存中的key
    String LOGIN_FILTER_IGNORE_URIS = "LOGIN_FILTER_IGNORE_URIS";
    // 是否使用登陆过滤
    String LOGIN_USE_FILTER = "LOGIN_USE_FILTER";


    // 用户登陆后放入在session中的key
    String SESSION_LOGIN_PRE = "CURRENT_USER";
    // 用户登陆时需要跳转的url在session中的key
    String SESSION_LOGIN_JUMP_URL = "JUMP_URL";
    // 验证码
    String SESSION_VERIFY_CODE = "VERIFY_CODE";


}
