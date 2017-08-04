package com.lvjc.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lvjc on 2017/8/2.
 */
@Getter
@Setter
public class Message {

    private User from;
    private User to;
    private String msg;

    public Message(){}

    public Message(User from, User to, String message){
        this.from = from;
        this.to = to;
        this.msg = message;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Message: \"" + msg + "\"\n");
        builder.append("From: " + from.getUsername() + "\n");
        builder.append("To: " + to.getUsername());
        return builder.toString();
    }
}
