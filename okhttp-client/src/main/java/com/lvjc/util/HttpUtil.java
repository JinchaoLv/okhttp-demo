package com.lvjc.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lvjc.dto.RemoteApiResult;
import com.lvjc.util.support.ResponseHandler;
import okhttp3.*;
import okio.BufferedSink;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lvjc on 2017/7/27.
 */
public class HttpUtil {

    private static HttpUtil instance = new HttpUtil();

    private OkHttpClient client;

    private Gson gson = new Gson();
    private Cache cache = new Cache(new File("F:/demo/okhttp/okhttp-client/src/main/resources/cache"), 10 * 1024 *1024);
    private boolean cacheable = false;
    private long connectTimeout = 10;
    private long writeTimeout = 10;
    private long readTimeout = 30;

    private HttpUtil(){
        client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
    }

    /*private HttpUtil(){
        client = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
    }*/

    public static HttpUtil instance(){
        return instance;
    }


    /**
     * 设置缓存状态
     * @param cacheable
     */
    public void setCacheable(boolean cacheable){
        this.cacheable = cacheable;
    }

    /**
     * 同步get请求
     * @param url
     * @return 指定类型的对象
     */
    public <T> T syncGet(String url, Map<String, String> requestParams, Map<String, String> headers, TypeToken<RemoteApiResult<T>> typeToken){
        Request request = new Request.Builder()
                .url(this.buildUrlWithRequestParams(url, requestParams))
                .headers(this.buildHeaders(headers))
                .build();
        return this.callAndReadResponseBodyString(request, typeToken);
    }

    /**
     * 同步get请求，流式读取大文件（1M以上）
     * @param url
     * @param requestParams
     * @param headers
     * @return
     */
    public byte[] streamSyncGet(String url, Map<String, String> requestParams, Map<String, String> headers){
        Request request = new Request.Builder()
                .url(this.buildUrlWithRequestParams(url, requestParams))
                .headers(this.buildHeaders(headers))
                .build();
        return this.callAndReadResponseStream(request);
    }

    /**
     * 异步get请求
     * @param url
     */
    public <T> void asynGet(String url, Map<String, String> requestParams, Map<String, String> headers, ResponseHandler handler){
        Request request = new Request.Builder()
                .url(this.buildUrlWithRequestParams(url, requestParams))
                .headers(this.buildHeaders(headers))
                .build();

        this.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(handler != null)
                    handler.handleFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(handler != null)
                    handler.handleResponse(call, response);
            }
        });
    }

    /**
     * 获取响应头
     * @param url
     * @return
     */
    public String accessingHeaders(String url, Map<String, String> requestParams){
        Request request = new Request.Builder()
                .url(this.buildUrlWithRequestParams(url, requestParams))
                .header("User-Agent", "none")
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();
        return this.getStringOfResponseHeaders(request);
    }

    /**
     * post字符串
     * @param url
     * @param str
     * @param mediaType
     * @return
     */
    public <T> T postString(String url, Map<String, String> requestParams, Map<String, String> headers, String str, MediaType mediaType, TypeToken<RemoteApiResult<T>> typeToken){
        Request request = new Request.Builder()
                .url(this.buildUrlWithRequestParams(url, requestParams))
                .headers(this.buildHeaders(headers))
                .post(RequestBody.create(mediaType, str))
                .build();
        return this.callAndReadResponseBodyString(request, typeToken);
    }

    /**
     * post streaming
     * @param url
     * @param mediaType
     * @param object
     * @return
     */
    public <T> T postStream(String url, Map<String, String> requestParams, Map<String, String> headers, MediaType mediaType, Object object, TypeToken<RemoteApiResult<T>> typeToken){
        RequestBody requestFileBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                OutputStream outputStream = sink.outputStream();
                String json = gson.toJson(object);
                byte[] bytes = json.getBytes("utf-8");
                outputStream.write(bytes);
            }
        };
        Request request = new Request.Builder()
                .url(this.buildUrlWithRequestParams(url, requestParams))
                .headers(this.buildHeaders(headers))
                .post(requestFileBody)
                .build();
        return this.callAndReadResponseBodyString(request, typeToken);
    }

    /**
     * post file
     * @param url
     * @param mediaType
     * @param file
     * @return
     */
    public <T> T postFile(String url, Map<String, String> requestParams, Map<String, String> headers, MediaType mediaType, File file, TypeToken<RemoteApiResult<T>> typeToken){
        Request request = new Request.Builder()
                .url(this.buildUrlWithRequestParams(url,requestParams))
                .headers(this.buildHeaders(headers))
                .post(RequestBody.create(mediaType, file))
                .build();
        return this.callAndReadResponseBodyString(request, typeToken);
    }

    /**
     * post表单
     * @param url
     * @param headers
     * @param formData
     * @return
     */
    public <T> T postFormData(String url, Map<String, String> requestParams, Map<String, String> headers, Map<String, String> formData, TypeToken<RemoteApiResult<T>> typeToken){
        Request request = new Request.Builder()
                .url(this.buildUrlWithRequestParams(url, requestParams))
                .headers(buildHeaders(headers))
                .post(buildRequestBody(formData))
                .build();
        return this.callAndReadResponseBodyString(request, typeToken);
    }

    /**
     * post multipart request
     * @param url
     * @param headers
     * @param formDataParts
     * @return
     */
    public <T> T postMultipartRequest(String url, Map<String, String> requestParams, Map<String, String> headers, List<FormDataPart> formDataParts, TypeToken<RemoteApiResult<T>> typeToken){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for(FormDataPart part : formDataParts){
            if(part instanceof KeyValueFormDataPart) {
                KeyValueFormDataPart keyValueFormDataPart = (KeyValueFormDataPart) part;
                builder.addFormDataPart(keyValueFormDataPart.getName(), keyValueFormDataPart.getValue());
            }
            else {
                FileFormDataPart fileFormDataPart = (FileFormDataPart)part;
                builder.addFormDataPart(fileFormDataPart.getName(), fileFormDataPart.getFileName(), fileFormDataPart.getRequestBody());
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(this.buildUrlWithRequestParams(url, requestParams))
                .headers(this.buildHeaders(headers))
                .post(requestBody)
                .build();
        return this.callAndReadResponseBodyString(request, typeToken);
    }


    private String buildUrlWithRequestParams(String url, Map<String, String> requestParams){
        if(url == null || requestParams == null || requestParams.size() == 0)
            return url;
        StringBuilder stringBuilder = new StringBuilder(url + "?");
        for(String name : requestParams.keySet()){
            stringBuilder.append(name + "=" + requestParams.get(name) + "&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private Headers buildHeaders(Map<String, String> headers){
        if(headers == null)
            headers = new HashMap<>(0);
        Headers.Builder builder = new Headers.Builder();
        for(String name : headers.keySet()){
            builder.add(name, headers.get(name));
        }
        return builder.build();
    }

    private RequestBody buildRequestBody(Map<String, String> formData){
        if(formData == null)
            formData = new HashMap<>(0);
        FormBody.Builder builder = new FormBody.Builder();
        for(String name : formData.keySet()){
            builder.add(name, formData.get(name));
        }
        return builder.build();
    }

    private <T> T callAndReadResponseBodyString(Request request, TypeToken<RemoteApiResult<T>> typeToken){
        Response response = null;
        try {
            response = this.getOkHttpClient().newCall(request).execute();
            if(!response.isSuccessful())
                throw new IOException();
        } catch (IOException e) {
            return null;
        }
        RemoteApiResult<T> remoteApiResult = this.gson.fromJson(response.body().charStream(), typeToken.getType());
        return remoteApiResult.getData();
    }

    private byte[] callAndReadResponseStream(Request request){
        Response response = null;
        try {
            response = this.getOkHttpClient().newCall(request).execute();
            if(!response.isSuccessful())
                throw new IOException();
        } catch (IOException e) {
            return new byte[0];
        }
        InputStream inputStream = response.body().byteStream();
        byte[] bytes = StreamUtil.getAllBytesOfInputStream(inputStream);
        return bytes;
    }

    private String getStringOfResponseHeaders(Request request){
        try{
            Response response = this.getOkHttpClient().newCall(request).execute();
            if(!response.isSuccessful())
                throw new IOException();
            Headers responseHeaders = response.headers();
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < responseHeaders.size(); i++){
                stringBuilder.append(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            return stringBuilder.toString();
        } catch (IOException e){
            return null;
        }
    }

    public OkHttpClient getOkHttpClient(){
        if(cacheable){
            return this.client.newBuilder().cache(this.cache).build();
        } else {
            return this.client;
        }
    }

    /*public OkHttpClient getOkHttpClient(){
        return this.client;
    }*/

    public static abstract class FormDataPart{

        protected String name;

        protected String getName(){
            return this.name;
        }
    }

    public static class KeyValueFormDataPart extends FormDataPart{

        private String value;

        public KeyValueFormDataPart(String name, String value){
            this.name = name;
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }

    public static class FileFormDataPart extends FormDataPart{

        private String fileName;
        private RequestBody requestBody;

        public FileFormDataPart(String name, String fileName, MediaType mediaType, File file){
            this.name = name;
            this.fileName = fileName;
            this.requestBody = RequestBody.create(mediaType, file);
        }

        public String getFileName(){
            return this.fileName;
        }

        public RequestBody getRequestBody(){
            return this.requestBody;
        }
    }

    public static void main(String[] args) throws IOException {
        String IMGUR_CLIENT_ID = "...";
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        OkHttpClient client = new OkHttpClient();


            // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("title", "Square Logo")
                    .addFormDataPart("image", "logo-square.png",
                            RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                    .url("https://api.imgur.com/3/image")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());

    }
}
