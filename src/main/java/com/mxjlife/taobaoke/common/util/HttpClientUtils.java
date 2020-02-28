package com.mxjlife.taobaoke.common.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 类说明: 发送Http请求的工具类
 * 
 * @author mxj
 * @email xj.meng@sinowaycredit.com
 * @version 创建时间：2017年7月28日 上午11:50:43
 */
public class HttpClientUtils {
	
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	//默认的字符集
	private static final String DEFAULT_CHARSET = "UTF-8";
	//默认Content-Type
	private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";
	//数据传输的最长时间
	private static final int socketTimeout = 60000;
	//连接超时时间
	private static final int timeout = 15000;
    //连接请求最长时间
	private static final int requestTimeout = 8000;
	//连接池最大连接数
	private static final int maxTotal = 500;
	//单个目标最大连接数
	private static final int maxPerRoute = 60;
	
	private static PoolingHttpClientConnectionManager cm = null;

    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
        	SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				@Override
                public boolean isTrusted(X509Certificate[] chain, String authType){
					return true;
				}
			}).build();
			sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            logger.error("创建SSL连接失败" + e);
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 最大连接数
        cm.setMaxTotal(maxTotal);
        // 每个路由基础的连接
        cm.setDefaultMaxPerRoute(maxPerRoute);
        //单独目标主机的最大连接数,可以设置多个
//        HttpHost localhost = new HttpHost("https://www.baidu.com", 443);
//        cm.setMaxPerRoute(new HttpRoute(localhost), 50);
    }
    
    /**
     * 设置httpPost和HttpGet请求的配置
     * @param httpRequestBase
     */
    private static void config(HttpRequestBase httpRequestBase, int requestTimeout) {
        // 配置请求的超时设置
//        setConnectTimeout 设置连接超时时间
//        setConnectionRequestTimeout 设置连接请求最长时间
//        setSocketTimeout 数据传输的最长时间
//        setStaleConnectionCheckEnabled 提交请求前测试连接是否可用 
        
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(requestTimeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(socketTimeout)
                .build();
        httpRequestBase.setConfig(requestConfig);
    }
    
    /**
     * 获取httpClient实例
     * @return CloseableHttpClient
     */
    private static synchronized CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
    }

    /**
     * 发送httpget请求, 传入URL和param后会将param中的内容使用UTF-8字符集进行URLEncoder
     * @param url 请求地址+?
     * @param param 拼接好的get请求参数
     * @return
     */
    public static String get(String url, String param, Map<String, String> headers) {
        if(StringUtils.isBlank(url)){
            return null;
        }
        // 创建默认的httpClient实例
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse httpResponse = null;
        HttpGet httpGet = null;
        // 发送get请求
        try {
            // 用get方法发送http请求
        	httpGet = new HttpGet(url + param);
        	//设置默认请求头
        	httpGet.setHeader("Content-Type", DEFAULT_CONTENT_TYPE);
            //设置请求头
            if(headers != null && headers.size()>0){
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            logger.debug("执行get请求, uri: " + httpGet.getURI());
            httpResponse = httpClient.execute(httpGet);
            // response实体
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                String response = EntityUtils.toString(entity, DEFAULT_CHARSET);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                logger.debug("响应状态码:" + statusCode);
                logger.debug("响应内容:" + response);
                if (statusCode == HttpStatus.SC_OK) {
                    // 成功
                    logger.debug("get请求成功, 获取结果:" + response);
                    return response;
                } else {
                    logger.debug("get请求失败, 返回内容:" + response);
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("httpclient请求失败: " +  e.getMessage());
            return null;
        } finally {
            if(httpGet != null){
                httpGet.releaseConnection();
            }
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                    httpResponse.close();
                } catch (IOException e) {
                    logger.error("关闭response失败: " +  e.getMessage());
                }
            }
        }
    }

    /**
     * 执行post方法, 同时会忽略https请求的证书
     * @param url  请求地址
     * @param params  请求参数, map中的key是请求参数名, value是请求参数值
     * @param headers  请求头, map中的key是请求头名, value是请求头的值
     * @param charset  字符集编码, 默认UTF-8
     * @return
     */
    public static HttpRes post(String url, Map<String, String> params, Map<String, String> headers, int requestTimeout, String charset) {
        HttpRes res = new HttpRes();
    	if(StringUtils.isBlank(url)){
    	    res.setStatus(451);
    	    res.setResult("url cannot be null");
			return res;
		}
		if(StringUtils.isBlank(charset)){
			charset = DEFAULT_CHARSET;
		}
    	// 创建默认的httpClient实例
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse httpResponse = null;
		HttpPost httpPost = null;
		long l1 = 0L;
		int statusCode = 200;
		try {
			httpPost = new HttpPost(url);
			//设置超时时间
			config(httpPost, requestTimeout);
			//设置默认请求头
			httpPost.setHeader("Content-Type", DEFAULT_CONTENT_TYPE);
			//设置请求头
			if(headers != null && headers.size()>0){
				for (Entry<String, String> entry : headers.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求参数
			if (null != params && params.size() > 0) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for (Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				} 
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams,charset);  
                httpPost.setEntity(entity);  
			}
			//发送请求
			l1 = System.currentTimeMillis();
			httpResponse = httpClient.execute(httpPost);
			//http请求所花费的时间
			res.setTime(System.currentTimeMillis() - l1);
            // response实体
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                String result = EntityUtils.toString(entity, charset);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                logger.debug("响应状态码:" + statusCode);
                logger.debug("响应内容:" + result);
                if (statusCode == HttpStatus.SC_OK) {
                    // 成功
                    res.setStatus(200);
                    res.setResult(result);
                } else {
                    logger.error("httpClient请求失败, 请求状态码: " + statusCode + ", 返回内容:" +  result);
                    res.setStatus(statusCode);
                    res.setResult(httpResponse.getStatusLine().getReasonPhrase());
                }
            } else {
                throw new RuntimeException("HttpEntity cannot be null");
            }
        } catch (IOException e) {
            logger.error("httpClient请求失败: " + e.getMessage());
            res.setStatus(452);
            res.setResult("request failed and throws an Exception: " + e.getMessage());
            res.setTime(System.currentTimeMillis() - l1);
        } finally {
            if(httpPost != null){
                httpPost.releaseConnection();
            }
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                    httpResponse.close();
                } catch (IOException e) {
                    logger.error("关闭response失败: " + e.getMessage());
                }
            }
        }
		return res;
    }
    
    /**
     * 发送post请求, post请求体中的参数不带key
     * @param url  请求地址
     * @param data  请求参数
     * @param charset  请求字符集
     * @param headers  请求头需要携带的信息
     * @return
     */
    public static HttpRes jsonPost(String url,String data, Map<String,String> headers, int requestTimeout, String charset){
        HttpRes res = new HttpRes();
        if(StringUtils.isBlank(url)){
            res.setStatus(451);
            res.setResult("url cannot be null");
            return res;
        }
        if(StringUtils.isBlank(charset)){
            charset = DEFAULT_CHARSET;
        }
        // 创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        HttpPost httpPost = null;
        long l1 = System.currentTimeMillis();
        int statusCode = 200;
        try {
            httpPost = new HttpPost(url);
            // 设置超时时间
            config(httpPost, requestTimeout);
            //设置默认请求头
            httpPost.setHeader("Content-Type", DEFAULT_CONTENT_TYPE);
            //设置请求头
            if(headers != null && headers.size()>0){
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求参数
            //无参数名，只是参数内容
            if(data != null){
                httpPost.setEntity(new StringEntity(data, charset));
            }
            //发送请求
            l1 = System.currentTimeMillis();
            httpResponse = httpClient.execute(httpPost);
            //http请求所花费的时间
            res.setTime(System.currentTimeMillis() - l1);
            // response实体
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                String result = EntityUtils.toString(entity, charset);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                logger.debug("响应状态码:" + statusCode);
                logger.debug("响应内容:" + result);
                if (statusCode == HttpStatus.SC_OK) {
                    // 成功
                    res.setStatus(200);
                    res.setResult(result);
                } else {
                    logger.error("httpClient请求失败, 请求状态码: " + statusCode + ", 返回内容:" +  result);
                    res.setStatus(statusCode);
                    res.setResult(httpResponse.getStatusLine().getReasonPhrase());
                    res.setTime(System.currentTimeMillis() - l1);
                }
            } else {
                throw new RuntimeException("HttpEntity cannot be null");
            }
        } catch (IOException e) {
            logger.error("httpClient请求失败: " + e.getMessage());
            res.setStatus(452);
            res.setResult("request failed and throws an Exception: " + e.getMessage());
        } finally {
            if(httpPost != null){
                httpPost.releaseConnection();
            }
            if (httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error("CloseableHttpClient关闭资源时出现异常: " + e.getMessage());
                }
            }
        }
        return res;
    }


    /**
     * 发送post请求, post请求体中的参数不带key
     * @param url  请求地址
     * @param data  请求参数
     * @param charset  请求字符集
     * @param headers  请求头需要携带的信息
     * @return
     */
    public static HttpRes sendJson(String url,String data, Map<String,String> headers, String charset){
        HttpRes result = new HttpRes();
        if(StringUtils.isBlank(url)){
            throw new RuntimeException("地址不能为空");
        }
        if(StringUtils.isBlank(charset)){
            charset = DEFAULT_CHARSET;
        }
        // 创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            // 设置超时时间
            config(httpPost, requestTimeout);
            //设置默认请求头
//            httpPost.setHeader("Content-Type", DEFAULT_CONTENT_TYPE);
            //设置请求头
            if(headers != null && headers.size()>0){
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求参数
            //无参数名，只是参数内容
            if(data != null){
                httpPost.setEntity(new StringEntity(data, charset));
            }
            //发送请求
            long pre = System.currentTimeMillis();
            httpResponse = httpClient.execute(httpPost);
            long suff = System.currentTimeMillis();
            result.setTime(suff - pre);
            // response实体
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                String resStr = EntityUtils.toString(entity, charset);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                result.setStatus(statusCode);
                result.setResult(resStr);
                return result;
            } else {
                throw new RuntimeException("Http返回为null");
            }
        } catch (IOException e) {
            logger.error("httpClient请求失败: " + e.getMessage());
            throw new RuntimeException("请求失败",e);
        } finally {
            if(httpPost != null){
                httpPost.releaseConnection();
            }
            if (httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error("CloseableHttpClient关闭资源时出现异常: " + e.getMessage());
                }
            }
        }
    }


    /**
     * 执行post方法, 同时会忽略https请求的证书, 没有使用http连接池
     * @param url  请求地址
     * @param params  请求参数, map中的key是请求参数名, value是请求参数值
     * @param headers  请求头, map中的key是请求头名, value是请求头的值
     * @param charset  字符集编码
     * @return
     */
    public static String excutePost(String url, Map<String, String> params, Map<String, String> headers, String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        if(StringUtils.isBlank(charset)){
            charset = DEFAULT_CHARSET;
        }
        CloseableHttpClient client = null;
        HttpPost httpPost = null;
        HttpResponse response = null;
        HttpEntity entity = null;
        String result = "";
        try {
            client = getHttpClient();
            httpPost = new HttpPost(url);
            // 设置超时时间
            config(httpPost, requestTimeout);
            //设置默认请求头
            httpPost.setHeader("Content-Type", DEFAULT_CONTENT_TYPE);
            //设置请求头
            if(headers != null && headers.size()>0){
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求参数
            if (null != params && params.size() > 0) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                for (Entry<String, String> entry : params.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(formParams, charset));
            }
            //发送请求
            response = client.execute(httpPost);
            entity = response.getEntity();
            int resSta = response.getStatusLine().getStatusCode();
            if (null != entity) {
                result = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
            }
            if (resSta != HttpStatus.SC_OK) {
                logger.error("请求发生异常, 错误码：" + resSta + ", 返回信息:" + result);
            }
        } catch (Exception e) {
            logger.error("发送post请求出现异常: " + e.getMessage());
        } finally {
            if(httpPost != null){
                httpPost.releaseConnection();
            }
            if (client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    logger.error("CloseableHttpClient关闭资源时出现异常: " + e.getMessage());
                }
            }
        }
        return result;
    }


    /**
     * 执行post方法, 同时会忽略https请求的证书, 证书过期也不影响访问
     * @param url  请求地址
     * @param params  请求参数, map中的key是请求参数名, value是请求参数值
     * @param headers  请求头, map中的key是请求头名, value是请求头的值
     * @param charset  字符集编码
     * @return
     */
    public static HttpResponse excutePostGetResponse(String url, Map<String, String> params, Map<String, String> headers, String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        if(StringUtils.isBlank(charset)){
            charset = DEFAULT_CHARSET;
        }
        // 创建默认的httpClient实例
        CloseableHttpClient client = getHttpClient();
        HttpPost httpPost = null;
        HttpResponse response = null;
        try {
            httpPost = new HttpPost(url);
            // 设置超时时间
            config(httpPost, requestTimeout);
            //设置默认请求头
            httpPost.setHeader("Content-Type", DEFAULT_CONTENT_TYPE);
            //设置请求头
            if(headers != null && headers.size()>0){
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求参数
            if (null != params && params.size() > 0) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                for (Entry<String, String> entry : params.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(formParams, charset));
            }
            //发送请求
            response = client.execute(httpPost);
            
            if (response == null) {
                logger.debug("请求发生异常, 返回为空");
            }
        } catch (Exception e) {
            logger.error("发送post请求出现异常: " + e.getMessage());
        } finally {
            if(httpPost != null){
                httpPost.releaseConnection();
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    logger.error("CloseableHttpClient关闭资源时出现异常: " + e.getMessage());
                }
            }
        }
        return response;
    }

    /**
     * httpGet, 使用javaNet发送get请求
     * @param urlStr
     * @return
     */
    public static String sendGet(String urlStr) {
        URL url = null;
        HttpURLConnection httpURLConn = null;
        StringBuffer response = new StringBuffer();
        try {
            url = new URL(urlStr);
            String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 "
                    + "(KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
            httpURLConn = (HttpURLConnection) url.openConnection();
            // httpURLConn.setDoOutput(true);
            httpURLConn.setRequestMethod("GET");
            httpURLConn.setReadTimeout(30000);
            httpURLConn.setConnectTimeout(30000);
            httpURLConn.setRequestProperty("User-agent", userAgent);
            httpURLConn.connect();
            InputStream in = httpURLConn.getInputStream();
            BufferedReader bd = new BufferedReader(new InputStreamReader(in,
                    "utf-8"));
            String temp;
            while ((temp = bd.readLine()) != null) {
                response.append(temp);
            }
            in.close();
            bd.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConn != null) {
                httpURLConn.disconnect();
            }
        }
        return response.toString();
    }
    
    /**
     * 将map转换为 k1=v1&k2=v2&k3=v3&k4=v4&k5=v5
     * @param params
     * @return
     */
    public String mapToUrlString(Map<String, Object> params){
        String result = null;
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        result = sb.deleteCharAt(sb.length()-1).toString();
        return result;
    }

    @Data
    public static class HttpRes{
        public static final int SUCCESS_CODE = 200;

        private int status;
        private String result;
        private long time;
    }

}