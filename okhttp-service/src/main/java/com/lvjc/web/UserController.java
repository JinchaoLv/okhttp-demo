package com.lvjc.web;

import com.lvjc.dto.ApiResult;
import com.lvjc.dto.User;
import com.lvjc.service.UserService;
import okhttp3.MultipartBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lvjc on 2017/7/27.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResult<User> registerUser(User user){
        return userService.registerUser(user);
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ApiResult<User> queryUser(HttpServletResponse response, @RequestParam("username") String username){
        response.addHeader("Cache-Control", "max-age=3600");
        response.addHeader("Last-Modified", Long.toString(System.currentTimeMillis()));
        System.out.println("————————————————————————————————");
        return userService.queryUser(username);
    }


    @RequestMapping(value = "/update/head", method = RequestMethod.POST)
    public ApiResult<User> updateHeadIcon(String username, String password, MultipartFile icon){
        byte[] bytes;
        try {
            bytes = icon.getBytes();
        } catch (IOException e) {
            bytes = null;
        }
        return userService.updateHeadIcon(username, password, null);
    }
}
