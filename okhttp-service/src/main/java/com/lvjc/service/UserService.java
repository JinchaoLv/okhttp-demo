package com.lvjc.service;

import com.lvjc.code.UserQueryCode;
import com.lvjc.code.UserRegisterCode;
import com.lvjc.dto.ApiResult;
import com.lvjc.dto.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lvjc on 2017/7/27.
 */
@Service
public class UserService {

    private Map<String, User> userMap = new HashMap<>();

    public ApiResult<User> registerUser(User user){
        ApiResult<User> result = new ApiResult<>();
        String userName = user.getUsername();
        if(userName == null){
            result.setCodeAndMessage(UserRegisterCode.INVALID_USERNAME);
            return result;
        }
        if(!userMap.containsKey(userName)){
            result.setCodeAndMessage(UserRegisterCode.SUCCESS);
            result.setData(user);
            userMap.put(userName, user);
        } else {
            result.setCodeAndMessage(UserRegisterCode.REPEATED_USERNAME);
        }
        return result;
    }

    public ApiResult<User> queryUser(String username){
        ApiResult<User> result = new ApiResult<>();
        if(userMap.containsKey(username)){
            result.setCodeAndMessage(UserQueryCode.SUCCESS);
            result.setData(userMap.get(username));
        } else {
            result.setCodeAndMessage(UserQueryCode.USER_NOT_EXIST);
        }
        return result;
    }

    public ApiResult<User> updateHeadIcon(String username, String password, byte[] bytes){
        return new ApiResult<>(new User(username, password, bytes));
    }
}
