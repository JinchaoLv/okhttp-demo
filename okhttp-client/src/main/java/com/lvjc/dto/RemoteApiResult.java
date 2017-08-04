package com.lvjc.dto;

import com.lvjc.code.BaseCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by lvjc on 2017/8/1.
 */
@Getter
@Setter
public class RemoteApiResult<T> {

    private String code;
    private String message;

    private T data;

    public RemoteApiResult() {
    }

    public RemoteApiResult(BaseCode code, T data) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = data;
    }

    public T getData(){
        return this.data;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }

}
