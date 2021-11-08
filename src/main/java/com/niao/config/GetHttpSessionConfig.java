package com.niao.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author HuangSir
 * @date 2021-11-08 12:58
 */
public class GetHttpSessionConfig extends ServerEndpointConfig.Configurator{
   @Override
   public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
      HttpSession httpSession = (HttpSession) request.getHttpSession();
      sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
   }
}
