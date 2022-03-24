package com.temple.manage.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public final class LogInUtils {

   private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

   private LogInUtils() {
   }

   public static String getTokenForLogin(String username, MockMvc mockMvc) throws Exception {
      String content = mockMvc.perform(post("/api/authenticate")
         .contentType(MediaType.APPLICATION_JSON)
         .content("{\"username\": \"" + username + "\"}"))
         .andReturn()
         .getResponse()
         .getContentAsString();
      JSONObject object = JSONObject.parseObject(content);
      AuthenticationResponse authResponse = JSON.parseObject(object.getString("data"), AuthenticationResponse.class);

      return authResponse.getToken();
   }

   @Data
   private static class AuthenticationResponse {

      private String token;
   }

   public static void main(String[] args) {
      Map<Integer, Integer> map = new HashMap<>(5);
      System.out.println(map);
   }
}
