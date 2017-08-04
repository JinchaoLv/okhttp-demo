package com.lvjc.code;

/**
 * Created by lvjc on 2017/8/1.
 */
public class BaseCode {

    public static final BaseCode SUCCESS = new BaseCode("success", null);

    protected String code;
    protected String message;

    public BaseCode(){}

    public BaseCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
