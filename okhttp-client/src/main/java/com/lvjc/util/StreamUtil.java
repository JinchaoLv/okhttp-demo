package com.lvjc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lvjc on 2017/7/31.
 */
public class StreamUtil {

    public static byte[] getAllBytesOfInputStream(InputStream inputStream){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[256];
        int size;
        try {
            while ((size = inputStream.read(buffer)) > 0){
                baos.write(buffer, 0, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }


}
