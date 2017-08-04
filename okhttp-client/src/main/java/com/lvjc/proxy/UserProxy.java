package com.lvjc.proxy;

import com.google.gson.reflect.TypeToken;
import com.lvjc.ApiUrlConfiguration;
import com.lvjc.dto.RemoteApiResult;
import com.lvjc.dto.User;
import com.lvjc.util.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by lvjc on 2017/7/27.
 */
@Component
public class UserProxy {

    @Autowired
    private ApiUrlConfiguration apiUrlConfiguration;

    private static final String URL_USER_REGISTER = "/user/register";

    public String register(String userName, String passaword){
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(passaword))
            return null;
        Map<String, String> formData = new HashMap<>(2);
        formData.put("userName", userName);
        formData.put("password", passaword);
        return HttpUtil.instance().postFormData(apiUrlConfiguration.getOkhttpServicePrefix() + URL_USER_REGISTER,
                null, null, formData, new TypeToken<RemoteApiResult<String>>(){});
    }


}
