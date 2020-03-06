package com.mxjlife.taobaoke.pojo.base;

import com.mxjlife.taobaoke.common.enums.ErrorEnum;
import com.mxjlife.taobaoke.common.exceptions.BaseException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * description: 接口返回结果对象
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2019/7/19 17:44
 */
@Setter
@Getter
@ToString
public class ResponseVO<T> {

    private String code;
    private String msg;
    private T data;

    private ResponseVO(String code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;

    }

    public void set(ErrorEnum errorEnum) {
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMessage();
    }

    /**
     * 构造一个自定义的返回结果
     *
     * @param code    状态码
     * @param msg 返回状态描述
     * @param data    返回数据
     * @return Response
     */
    public static <T> ResponseVO<T> get(String code, String msg, T data) {
        return new ResponseVO<>(code, msg, data);
    }

    /**
     * 获取一个没有业务数据的结果
     */
    public static ResponseVO<Object> get(String code, String msg) {
        return new ResponseVO<>(code, msg, null);
    }

    /**
     * 获取一个没有业务数据的结果
     */
    public static ResponseVO<Object> get(ErrorEnum errorEnum) {
        return new ResponseVO<>(errorEnum.getCode(), errorEnum.getMessage(), null);
    }

    /**
     * 获取一个默认的结果, 默认失败
     */
    public static <T> ResponseVO<T> get(T data) {
        return new ResponseVO<>(ErrorEnum.REQUEST_FAILD.getCode(), ErrorEnum.REQUEST_FAILD.getMessage(), data);
    }

    public static <T> ResponseVO<T> get(ErrorEnum errorEnum, Class<T> clazz) {
        return new ResponseVO<>(errorEnum.getCode(), errorEnum.getMessage(), null);
    }

    /**
     * 将exception转为返回结果
     */
    public static ResponseVO<Object> get(BaseException e) {
        String code = ErrorEnum.REQUEST_FAILD.getCode();
        if(NumberUtils.isCreatable(e.getCode())){
            code = e.getCode();
        }
        return new ResponseVO<>(code, e.getMsg(), null);
    }

    /**
     * 获取一个成功结果
     */
    public static <T> ResponseVO<T> getSuccess(T data) {
        return new ResponseVO<>(ErrorEnum.REQUEST_SUCCESS.getCode(), ErrorEnum.REQUEST_SUCCESS.getMessage(), data);
    }

    /**
     * 获取一个成功结果
     */
    public static ResponseVO<Object> getSuccess() {
        return new ResponseVO<>(ErrorEnum.REQUEST_SUCCESS.getCode(), ErrorEnum.REQUEST_SUCCESS.getMessage(), null);
    }


}
