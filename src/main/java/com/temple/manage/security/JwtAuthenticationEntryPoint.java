package com.temple.manage.security;

import com.alibaba.fastjson.JSON;
import com.temple.manage.common.api.R;
import com.temple.manage.common.api.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

   @Override
   public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
      // This is invoked when user tries to access a secured REST resource without supplying any credentials
      // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
      // Here you can place any message you want
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json;charset=UTF-8");
      try (PrintWriter writer = response.getWriter()){
         writer.append(JSON.toJSONString(R.failed(ResultCode.UNAUTHORIZED, authException.getMessage())));
         writer.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
