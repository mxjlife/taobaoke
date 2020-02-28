package com.mxjlife.taobaoke.tbkTest;

import com.mxjlife.taobaoke.common.util.BeanUtils;
import com.mxjlife.taobaoke.common.util.HttpClientUtils;
import com.mxjlife.taobaoke.common.util.TbkSignUtil;
import com.mxjlife.taobaoke.pojo.TbkBaseRequest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2020/2/27 16:03
 */
public class ReqTest {

    String url = "http://gw.api.taobao.com/router/rest";
    String charset = "UTF-8";
    String secret = "09c85c86472bf4d00e7c5a760ab83e61";
    String appKey = "28419413";

    @Test
    public void reqTest(){
        String method = "taobao.tbk.item.info.get";
        Map<String, String> headers = new HashMap<>();
        TbkBaseRequest tbkBaseRequest = new TbkBaseRequest(method);
        tbkBaseRequest.setApp_key(appKey);
        Map<String, String> params = BeanUtils.javabean2map(tbkBaseRequest,1);
        params.put("num_iids", "381,11655,16189,16292,16516,16518,16521");

        String sign = TbkSignUtil.signTopRequest(params, secret, "md5");
        params.put("sign", sign);
        System.out.println(params);
        String s = HttpClientUtils.excutePost(url, params, headers, charset);
        System.out.println(s);
    }
}
