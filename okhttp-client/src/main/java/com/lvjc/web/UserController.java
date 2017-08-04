package com.lvjc.web;

import com.lvjc.dto.RemoteApiResult;
import com.lvjc.dto.User;
import com.lvjc.service.UserService;
import com.lvjc.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by lvjc on 2017/7/27.
 */
@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "register";
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("userName") String userName, @RequestParam("password") String password){
        return userService.register(userName, password);
    }

    @ResponseBody
    @RequestMapping(value = "/update/head", method = RequestMethod.POST)
    public RemoteApiResult<User> updateHeadIcon(String username, String password, byte[] icon){
        //TODO
        return null;
    }

}
