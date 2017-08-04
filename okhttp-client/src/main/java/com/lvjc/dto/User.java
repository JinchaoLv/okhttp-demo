package com.lvjc.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by lvjc on 2017/7/27.
 */
@Setter
@Getter
public class User {

    private String username;
    private String password;

    private byte[] icon;

    public User(){}

    public User(String userName, String password){
        this.username = userName;
        this.password = password;
    }

    public User(String username, String password, byte[] icon){
        this.username = username;
        this.password = password;
        this.icon = icon;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
