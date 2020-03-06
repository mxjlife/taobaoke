package com.mxjlife.taobaoke.common.enums;

public enum ErrorEnum {

    /**
     * 操作成功
     */
    REQUEST_SUCCESS("2001", "SUCCESS"),
    REQUEST_FAILD("5001", "FAILD"),

    /**
     * 参数错误
     */
    REQUEST_PARAM_ERROR("20001001", "参数[%s]不正确"),
    REQUEST_VERIFY_CODE_ERROR("20001002", "验证码不正确"),
    REQUEST_USERNAME_PASSWD_ERROR("20001003", "用户名或密码不正确"),
    REQUEST_USER_INIT_ERROR("20001004", "账户未激活"),
    REQUEST_USER_BLOCK_ERROR("20001005", "账户被锁定"),
    ;


    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
