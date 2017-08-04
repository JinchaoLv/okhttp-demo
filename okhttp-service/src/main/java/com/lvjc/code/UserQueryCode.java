package com.lvjc.code;

/**
 * Created by lvjc on 2017/8/1.
 */
public class UserQueryCode extends BaseCode {

    public static final UserQueryCode USER_NOT_EXIST = new UserQueryCode("error", "用户不存在");

    private UserQueryCode(String code, String message){
        this.code = code;
        this.message = message;
    }


}
