package com.lvjc.code;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * Created by lvjc on 2017/8/1.
 */
public class BaseCode {

    public static final BaseCode SUCCESS = new BaseCode("success", null);
    public static final BaseCode UNKNOWN_FAILURE = new BaseCode("failure", "unknown failure");

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
