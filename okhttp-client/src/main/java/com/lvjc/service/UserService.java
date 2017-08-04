package com.lvjc.service;

import com.lvjc.dto.RemoteApiResult;
import com.lvjc.dto.User;
import com.lvjc.proxy.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lvjc on 2017/7/27.
 */
@Service
public class UserService {

    @Autowired
    private UserProxy userProxy;

    public String register(String userName, String password){
        return userProxy.register(userName, password);
    }

    public RemoteApiResult<User> updateHeadIcon(String username, String password, byte[] icon){
        //TODO
        return null;
    }
}
