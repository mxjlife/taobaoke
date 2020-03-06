package com.mxjlife.taobaoke.config.filter;

import com.mxjlife.taobaoke.common.cache.GuavaCache;
import com.mxjlife.taobaoke.common.constants.SysCons;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * description: 登陆filter
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2019/7/23 16:30
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/")
@Order(1)
@Slf4j
public class LoginFilter implements Filter {

    private static final String DEFAULT_IGNORE_URIS = "login.html,logout.html,get";
    private static final String LOGIN_PAGE = "/user/login.html";
    private static final int USE_FILTER = 1;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        if(log.isDebugEnabled()){
            log.debug("收到请求 -> {}", httpRequest.getRequestURI());
        }
        // 是否启用过滤器,
        Integer useFilter = Integer.valueOf(GuavaCache.get(SysCons.LOGIN_USE_FILTER));
        if(useFilter == null || useFilter == USE_FILTER){
            if (isIgnore(httpRequest)) {
                if(log.isDebugEnabled()){
                    log.debug("请求uri[{}]不进行登陆过滤", httpRequest.getRequestURI());
                }
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("请求uri[{}]进行登陆过滤", httpRequest.getRequestURI());
                }
                HttpSession session = httpRequest.getSession();
                Object login = session.getAttribute(SysCons.SESSION_LOGIN_PRE);
                if (login == null) {
                    // 没有登陆，跳转到登陆页面
                    // 保存客户想要去的地址, 登录成功后则直接跳转,而不是到首页
                    String goURL = httpRequest.getServletPath();
                    // 判断参数是否为空，不null就获取参数
                    if (StringUtils.isNotBlank(httpRequest.getQueryString())) {
                        goURL += "?" + httpRequest.getQueryString();
                    }
                    session.setAttribute(SysCons.SESSION_LOGIN_JUMP_URL, goURL);
                    httpResponse.sendRedirect(httpRequest.getContextPath() + LOGIN_PAGE);
                } else {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        } else {
            if(log.isDebugEnabled()){
                log.debug("登陆校验已关闭");
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * filter是否排除此请求, 不进行过滤
     * @param request
     * @return
     */
    private boolean isIgnore(HttpServletRequest request) {
        String path = request.getRequestURI();
        String ignoreUris = GuavaCache.get(SysCons.LOGIN_FILTER_IGNORE_URIS);
        if(StringUtils.isBlank(ignoreUris)){
            ignoreUris = DEFAULT_IGNORE_URIS;
        }
        String[] urisArr = StringUtils.split(ignoreUris, ",");
        for (String s: urisArr) {
            if(StringUtils.isNotBlank(s) && StringUtils.isNotBlank(path) && path.endsWith(s)){
                return true;
            }
        }
//        Set<String> uris = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(urisArr)));
        return false;
    }


}