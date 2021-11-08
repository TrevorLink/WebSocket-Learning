package com.niao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * @author HuangSir
 * @date 2021-11-08 10:00
 */
@Configuration
public class WebSocketConfig{
   @Bean
   //注入serverEndpointExporter开启注解注册扫描，扫描所有带@ServerEndPoint的注解
   public ServerEndpointExporter serverEndpointExporter(){
      return  new ServerEndpointExporter();
   }
}
