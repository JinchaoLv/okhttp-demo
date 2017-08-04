package com.lvjc.web;

import com.lvjc.dto.ApiResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;

/**
 * Created by lvjc on 2017/7/31.
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @RequestMapping(value = "/get/small", method = RequestMethod.GET)
    public ApiResult<String> getSmallFile(@RequestParam("fileId") @NotNull String fileId){
        try {
            Thread.sleep(200);
            return new ApiResult<>("small file" + fileId);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @RequestMapping(value = "/get/large", method = RequestMethod.GET)
    public ApiResult<String> getLargeFile(@RequestParam("fileId") @NotNull String fileId){
        try {
            Thread.sleep(1000);
            return new ApiResult<>("large file" + fileId);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @RequestMapping(value = "/get/image/author", method = RequestMethod.GET)
    public void getAuthorImage(HttpServletResponse response){
        File file = new File("F:\\demo\\okhttp\\okhttp-service\\src\\main\\resources\\static\\author.jpg");
        try(FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream()
        ) {
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream.read(bytes);
            outputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ApiResult<String> uploadFile(@RequestBody byte[] bytes) {
        String string = new String(bytes);
        return new ApiResult<>(string);
    }
}
