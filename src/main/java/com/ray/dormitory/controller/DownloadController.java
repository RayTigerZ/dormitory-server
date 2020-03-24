package com.ray.dormitory.controller;


import com.ray.dormitory.system.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.net.URLEncoder;

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
        try {
            String filename = code + excelExtension;
            String filePath = sysConfig.getTemplatePath() + filename;

            File file = new File(filePath);
            // 如果文件存在，则进行下载
            if (file.exists()) {
                // 配置文件下载"application/octet-stream"
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/octet-stream;charset=UTF-8");
                String name = URLEncoder.encode(filename, "UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + name);
                response.setHeader("fileName", name);
                response.addHeader("Access-Control-Expose-Headers", "fileName");
                //加上设置大小下载下来的.xlsx文件打开时才不会报“Excel 已完成文件级验证和修复。此工作簿的某些部分可能已被修复或丢弃”
                response.addHeader("Content-Length", String.valueOf(file.length()));

                OutputStream out = response.getOutputStream();

                byte[] buff = new byte[1024];
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                int i = bufferedInputStream.read(buff);
                while (i != -1) {
                    out.write(buff, 0, buff.length);
                    i = bufferedInputStream.read(buff);
                }
                out.flush();
                out.close();
                bufferedInputStream.close();

                log.info("download {} successfully!", filePath);
            }


        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
