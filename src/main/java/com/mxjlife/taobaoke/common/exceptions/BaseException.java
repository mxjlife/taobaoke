package com.mxjlife.taobaoke.common.exceptions;

import lombok.Data;

/**
 * description:
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2019/8/15 16:04
 */
@Data
public class BaseException extends RuntimeException{
    public String code;
    public String msg;
}
