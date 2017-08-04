package com.lvjc.web;

import com.lvjc.dto.ApiResult;
import com.lvjc.dto.Message;
import com.lvjc.util.HttpUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by lvjc on 2017/7/28.
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public ApiResult<String> sendMessage(HttpServletRequest request){
        String msg;
        try {
            msg = HttpUtil.readRequestBodyToString(request);
        } catch (IOException e) {
            msg = null;
        }
        return new ApiResult<>("Your message: \"" + msg + "\" has been received successfully.");
    }

    @RequestMapping(value = "/reply2", method = RequestMethod.POST)
    public ApiResult<String> sendMessage(@RequestBody String msg){
        return new ApiResult<>("Your message: \"" + msg + "\" has been received successfully.");
    }

    @RequestMapping(value = "/reply3", method = RequestMethod.POST)
    public ApiResult<String> sendMessage(@RequestBody Message message){
        return new ApiResult<>(message.toString());
    }
}
