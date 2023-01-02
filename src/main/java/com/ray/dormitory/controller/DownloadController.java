package com.ray.dormitory.controller;


import com.ray.dormitory.system.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author : Ray
 * @date : 2019/11/19 20:05
 */

@Slf4j
@RestController
@RequestMapping("/download")
public class DownloadController {

    private static String excelExtension = ".xlsx";
    @Autowired
    private SysConfig sysConfig;


    @GetMapping("/batchExcel")
    public void batchExcel(@NotBlank String code, HttpServletResponse response) {

        String filename = code + excelExtension;
        String filePath = sysConfig.getTemplatePath() + filename;


        // 如果文件存在，则进行下载

            // 配置文件下载"application/octet-stream"
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            String name = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            response.setHeader("fileName", name);
            response.addHeader("Access-Control-Expose-Headers", "fileName");



            Resource resource=new ClassPathResource(filePath);

        try (InputStream inputStream = resource.getInputStream();
             OutputStream outputStream = response.getOutputStream()){
            IOUtils.copy(inputStream,outputStream);
            //加上设置大小下载下来的.xlsx文件打开时才不会报“Excel 已完成文件级验证和修复。此工作簿的某些部分可能已被修复或丢弃”
            response.addHeader("Content-Length", String.valueOf(inputStream.available()));
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
