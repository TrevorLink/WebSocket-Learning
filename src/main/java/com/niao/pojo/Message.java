package com.niao.pojo;

/**
 * 客户端向服务端发送的数据
 * @author HuangSir
 * @date 2021-11-07 21:19
 */
public class Message {
   //接收方的名称
   private String toName;
   //具体消息内容
   private  String message;

   public Message() {
   }

   public Message(String toName, String message) {
      this.toName = toName;
      this.message = message;
   }

   public String gettoName() {
      return toName;
   }

   public void settoName(String toName) {
      this.toName = toName;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   @Override
   public String toString() {
      return "Message{" +
              "toName='" + toName + '\'' +
              ", message='" + message + '\'' +
              '}';
   }
}

