package com.niao.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niao.config.GetHttpSessionConfig;
import com.niao.pojo.Message;
import com.niao.utils.MessageUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HuangSir
 * @date 2021-11-08 9:58
 */
@Component
@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfig.class)
public class ChatEndPoint {
   /*
   存储每一个客户端对象对应的ChatEndPoint对象
   之所以map是静态的是因为ChatEndPoint在容器中会有很多个
   但是我们的容器只需要一个map
   用户名Sting唯一标识
   */
   private static Map<String, ChatEndPoint> onlineUsers = new ConcurrentHashMap<>();

   /*
   声明Session对象，通过Session可以获取指定的客户端
   这里不可以定义成静态的Session属性，因为每一个chatEndPoint实例都会对应一个Session
   */
   private Session session;

   /*
   我们还需要一个HTTPSession对象，因为我们在Session中存储了用户名（判断消息的发送方）
    */
   private HttpSession httpSession;

   //建立连接时触发
   @OnOpen
   public void onOpen(Session session, EndpointConfig config) {
      System.out.println("连接建立！");
      //将形参的Session传递给全局变量Session
      this.session=session;
      //需要把httpSession对象存到EndpointConfig里，再从config里获取
      this.httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
      //把我们这个endPoint对象存放进我们在线服务的集合
      String username = (String) httpSession.getAttribute("username");
      onlineUsers.put(username,this);

      //只要我们的一个客户端向我们的服务端建立连接之后，我们就向所有的人广播
      String message = MessageUtils.getMessage(true, null, onlineUsers.keySet());
      System.out.println(message);
      broadcastAllUsers(message);
   }

   /**
    * 向所有在线的用户广播发送消息（服务端--->客户端推送）
    * @param message 要发送的消息
    */
   private void broadcastAllUsers(String message){
      Set<String> users = onlineUsers.keySet();
      for(String user:users){
         //对于我们每一个在线的用户，我们都要先获取他的EndPoint实例
         ChatEndPoint chatEndPoint = onlineUsers.get(user);
         try {
            System.out.println("在线的用户是"+user);
            //通过每个EndPoint实例获取WebSocket的Session，之后getRemote然后发消息
            chatEndPoint.session.getBasicRemote().sendText(message);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   @OnMessage//收到客户端的消息时触发
   public void onMessage(String message, Session session) {
      //我们需要把我们收到的JSON字符串封装成为message对象
      ObjectMapper objectMapper = new ObjectMapper();
      try {
         Message readValue = objectMapper.readValue(message, Message.class);
         //从HttpSession中获取当前是谁发送的，用于工具类最终的封装标识
        String fromName=(String) httpSession.getAttribute("username");
         String res = MessageUtils.getMessage(false, fromName, readValue.getMessage());
         //从在线用户双列集合里找到我们目的用户对应的EndPoint才能发送
         onlineUsers.get(readValue.gettoName()).session.getBasicRemote().sendText(res);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @OnClose//关闭连接时触发
   public void onClose(Session session) {

   }
}
