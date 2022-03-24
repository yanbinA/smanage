package com.temple.manage.security;

import com.alibaba.fastjson.JSON;
import com.temple.manage.common.api.R;
import com.temple.manage.common.api.ResultCode;
import com.temple.manage.common.exception.Asserts;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
      // This is invoked when user tries to access a secured REST resource without the necessary authorization
      // We should just send a 403 Forbidden response because there is no 'error' page to redirect to
      // Here you can place any message you want
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json;charset=UTF-8");
      try (PrintWriter writer = response.getWriter()){
         writer.append(JSON.toJSONString(R.failed(ResultCode.FORBIDDEN)));
         writer.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
