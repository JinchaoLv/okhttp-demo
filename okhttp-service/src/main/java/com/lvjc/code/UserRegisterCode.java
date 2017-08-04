package com.lvjc.code;

/**
 * Created by lvjc on 2017/7/27.
 */
public class UserRegisterCode extends BaseCode {


    public static final UserRegisterCode REPEATED_USERNAME = new UserRegisterCode("error", "该用户名已被注册");
    public static final UserRegisterCode INVALID_USERNAME = new UserRegisterCode("error", "用户名不符合要求");




    private UserRegisterCode(String code, String message){
        this.code = code;
        this.message = message;
    }


}
