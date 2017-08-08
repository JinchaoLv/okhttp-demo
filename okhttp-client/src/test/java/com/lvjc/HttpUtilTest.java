package com.lvjc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lvjc.dto.Message;
import com.lvjc.dto.RemoteApiResult;
import com.lvjc.dto.User;
import com.lvjc.util.HttpUtil;
import com.lvjc.util.MapUtil;

import com.lvjc.util.support.DefaultResponseHandler;
import com.lvjc.util.support.ResponseHandler;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lvjc on 2017/7/31.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpUtilTest {

    @Autowired
    private ApiUrlConfiguration apiUrlConfiguration;

    @Test
    public void testStringSyncGet(){
        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/file/get/large";
        for(int i = 0; i < 5; i++){
            System.out.println(HttpUtil.instance().syncGet(url, MapUtil.newHashMap("fileId", "" + i), null, new TypeToken<RemoteApiResult<String>>(){}));
        }
    }

    @Test
    public void testObjectSyncGet(){
        this.testPostFormData();

        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/user/query";
        User result = HttpUtil.instance().syncGet(url, MapUtil.newHashMap("username", "lvjc"), null, new TypeToken<RemoteApiResult<User>>(){});
        Assert.assertEquals(new User("lvjc", "123456"), result);
    }

    @Test
    public void testStreamSyncGet(){
        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/file/get/image/author";
        byte[] result = HttpUtil.instance().streamSyncGet(url, null, null);
        try (FileOutputStream fileOutputStream = new FileOutputStream("F:/demo/okhttp/okhttp-client/src/test/resources/author.jpg")) {
            fileOutputStream.write(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsynGet(){
        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/file/get/large";
        int requestNum = 10;
        DefaultResponseHandler<String>[] handlers = new DefaultResponseHandler[requestNum];
        for(int i = 0; i < requestNum; i++){
            handlers[i] = new DefaultResponseHandler<>();
            HttpUtil.instance().asynGet(url, MapUtil.newHashMap("fileId", "" + i), null, handlers[i]);
        }
        for(int i = 0; i < requestNum; i++){
            System.out.println(handlers[i].get());
        }
    }

    @Test
    public void testAccessingHeaders(){
        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/file/get/large";
        System.out.println(HttpUtil.instance().accessingHeaders(url, MapUtil.newHashMap("fileId", "id")));
    }

    @Test
    public void testPostString(){
        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/message/reply2";
        System.out.println(HttpUtil.instance().postString(url, null, null,
                "Hello! I'm jinchaolv. How are you?", MediaType.parse("test/x-markdown; charset=utf-8"),
                new TypeToken<RemoteApiResult<String>>(){}));
    }

    @Test
    public void testPostString2(){
        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/message/reply3";
        String msg = "Hello! I'm jinchaolv. How are you?";
        Message message = new Message(new User("jinchaolv", "123456"), new User("lvjinchao", "123456"), msg);
        System.out.println(HttpUtil.instance().postString(url, null, null, new Gson().toJson(message),
                MediaType.parse("application/json"), new TypeToken<RemoteApiResult<String>>(){}));
    }

    @Test
    public void testPostStream(){
        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/file/upload";
        System.out.println(HttpUtil.instance().postStream(url, null, null, MediaType.parse("application/json"),
                new User("lvjc", "123456"), new TypeToken<RemoteApiResult<String>>(){}));
    }

    @Test
    public void testPostFile(){
        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/file/upload";
        System.out.println(HttpUtil.instance().postFile(url, null, null, MediaType.parse("test/x-markdown; charset=utf-8"),
                new File("F:/demo/okhttp/okhttp-client/src/test/resources/testFile.txt"), new TypeToken<RemoteApiResult<String>>(){}));
    }

    @Test
    public void testPostFormData(){
        System.out.println(HttpUtil.instance().postFormData(apiUrlConfiguration.getOkhttpServicePrefix() + "/user/register",
                null, null, MapUtil.newHashMap(new String[]{"username", "password"}, new String[]{"lvjc", "123456"}), new TypeToken<RemoteApiResult<User>>(){}));
    }

    @Test
    public void testPostMultipartRequest(){
        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/user/update/head";
        List<HttpUtil.FormDataPart> parts = new ArrayList<>(3);
        parts.add(new HttpUtil.KeyValueFormDataPart("username", "lvjc"));
        parts.add(new HttpUtil.KeyValueFormDataPart("password", "123456"));
        parts.add(new HttpUtil.FileFormDataPart("icon", "tulips.jpg", MediaType.parse("image/jpg"),
                new File("C:/Users/Public/Pictures/Sample Pictures/tulips.jpg")));
        User result = HttpUtil.instance().postMultipartRequest(url, null, null, parts,
                new TypeToken<RemoteApiResult<User>>(){});
        System.out.println(result);
    }

    @Test
    public void testCacheable(){

        String url = apiUrlConfiguration.getOkhttpServicePrefix() + "/user/query?username=lvjc";

        HttpUtil.instance().setCacheable(true);

        Request request = new Request.Builder()
                .url(url)
                .header("Cache-Control", "0")
                .header("If-Modidied-Since", "1501832725887")
                .build();
        try {
            Response response1 = HttpUtil.instance().getOkHttpClient().newCall(request).execute();
            System.out.println(response1.body().string());
            System.out.println("Response 1 response:          " + response1);
            System.out.println("Response 1 cache response:    " + response1.cacheResponse());
            System.out.println("Response 1 network response:  " + response1.networkResponse());

            Response response2 = HttpUtil.instance().getOkHttpClient().newCall(request).execute();
            System.out.println("Response 2 response:          " + response2);
            System.out.println("Response 2 cache response:    " + response2.cacheResponse());
            System.out.println("Response 2 network response:  " + response2.networkResponse());
            Response response3 = HttpUtil.instance().getOkHttpClient().newCall(request).execute();

            System.out.println("Response1 = Response2 ? :" + response1.equals(response2));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
