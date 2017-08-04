package com.lvjc.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lvjc on 2017/8/2.
 */
public class HttpUtil {

    public static String readRequestBodyToString(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder result = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            result.append(line);
        }
        return result.toString();
    }

    public static byte[] readRequestBodyToByteArray(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        byte[] result = new byte[request.getContentLength()];
        inputStream.read(result);
        return result;
    }
}
