package com.lvjc.dto;

import com.lvjc.code.BaseCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lvjc on 2017/7/27.
 */
@Getter
@Setter
public class ApiResult<T> {

    private String code;
    private String message;

    private T data;

    public ApiResult() {
    }

    public ApiResult(T data) {
        this.setCodeAndMessage(BaseCode.SUCCESS);
        this.data = data;
    }

    public void setCodeAndMessage(BaseCode abstractCode){
        this.code = abstractCode.getCode();
        this.message = abstractCode.getMessage();
    }
}
