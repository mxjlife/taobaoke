package com.mxjlife.taobaoke.config.handle;

import com.mxjlife.taobaoke.common.enums.ErrorEnum;
import com.mxjlife.taobaoke.pojo.base.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description: 全局异常处理
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2019/7/26 18:10
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseVO errorHandler(Exception e) {
        log.error("系统异常->  {} : {}", e.getClass().getName(), e.getMessage());
        return ResponseVO.get(ErrorEnum.REQUEST_FAILD);
    }
    
    /**
     * 拦截捕捉自定义异常 Error.class
     */
    @ResponseBody
    @ExceptionHandler(value = Error.class)
    public ResponseVO myErrorHandler(Error e) {
        log.error("系统异常 -> {}", e.getMessage());
        return ResponseVO.get(ErrorEnum.REQUEST_FAILD);
    }
 
}