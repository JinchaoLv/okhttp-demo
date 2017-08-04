package com.lvjc.util.support;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lvjc.code.BaseCode;
import com.lvjc.dto.RemoteApiResult;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by lvjc on 2017/8/2.
 */
public class DefaultResponseHandler<T> implements ResponseHandler {

    private RemoteApiResult<T> result;

    @Override
    public synchronized void handleResponse(Call call, Response response) {
        if(!response.isSuccessful()){
            result = new RemoteApiResult<>(BaseCode.UNKNOWN_FAILURE, null);
        }
        else {
            result = new Gson().fromJson(response.body().charStream(), new TypeToken<RemoteApiResult<T>>(){}.getType());
        }
        this.notify();
    }

    @Override
    public synchronized void handleFailure(Call call, IOException e) {
        result = new RemoteApiResult<>(BaseCode.UNKNOWN_FAILURE, null);
        this.notify();
    }

    public synchronized RemoteApiResult<T> get(){
        if(result == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return this.result;
    }
}
