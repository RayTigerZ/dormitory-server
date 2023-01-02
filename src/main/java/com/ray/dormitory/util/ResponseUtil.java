package com.ray.dormitory.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Ray
 * @date 2019/12/09 18:57
 */
public class ResponseUtil {

    public static void sendJson(HttpServletRequest request, HttpServletResponse response, Object object) {

        try {
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();

            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Vary", "Origin");
            String jsonStr = new ObjectMapper().writeValueAsString(object);

            printWriter.write(jsonStr);
            response.setHeader("content-Length", String.valueOf(jsonStr.getBytes().length));
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
