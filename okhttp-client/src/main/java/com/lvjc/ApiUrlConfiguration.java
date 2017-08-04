package com.lvjc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by lvjc on 2017/7/27.
 */
@Component
public class ApiUrlConfiguration {

    @Value("${application.okhttp-service.url.prefix}")
    private String okhttpServicePrefix;

    public String getOkhttpServicePrefix(){
        return this.okhttpServicePrefix;
    }
}
