package com.niao.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niao.pojo.ResultMessage;

/**
 * @author HuangSir
 * @date 2021-11-07 21:30
 */
public class MessageUtils {
   public static String getMessage(boolean isSystemMessage,String fromName,Object message){
      try{
         ResultMessage resultMessage = new ResultMessage();
         resultMessage.setSystem(isSystemMessage);
         resultMessage.setMessage(message);
         if(fromName != null){
            resultMessage.setFromName(fromName);
         }
         ObjectMapper mapper = new ObjectMapper();
         return mapper.writeValueAsString(resultMessage);//响应JSON
      }catch (JsonProcessingException e){
         e.printStackTrace();
      }
      return null;
   }

}
