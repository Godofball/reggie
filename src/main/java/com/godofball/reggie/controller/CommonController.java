package com.godofball.reggie.controller;

import com.godofball.reggie.common.Result;
import com.sun.deploy.net.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    String basePath;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info(file.toString());
        String fileName = UUID.randomUUID().toString();
        String suffix = file.getOriginalFilename();
        suffix = suffix.substring(suffix.lastIndexOf("."));
        fileName += suffix;
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        File file = new File(basePath + name);
        if (!file.exists()) {
            return;
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            os=response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes,0,len);
                os.flush();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (is!=null) is.close();
            if (os!=null) os.close();
        }
    }

}
