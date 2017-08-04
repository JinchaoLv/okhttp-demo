package com.lvjc;

import com.lvjc.util.StreamUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * Created by lvjc on 2017/7/31.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamUtilTest {

    @Test
    public void testGetAllBytesOfInputStream(){
        String path = "F:/demo/okhttp/okhttp-client/src/test/resources/";
        File file = new File(path + "testFile.txt");
        try (InputStream inputStream = new FileInputStream(file);
             FileOutputStream fileOutputStream = new FileOutputStream(new File(path + "testFile-副本.txt"))) {
            byte[] bytes = StreamUtil.getAllBytesOfInputStream(inputStream);
            fileOutputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
