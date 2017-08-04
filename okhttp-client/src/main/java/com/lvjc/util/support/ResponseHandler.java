package com.lvjc.util.support;

import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by lvjc on 2017/8/2.
 */
public interface ResponseHandler {

    void handleResponse(Call call, Response response);

    void handleFailure(Call call, IOException e);
}
