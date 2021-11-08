package com.niao.pojo;

/**
 * @author HuangSir
 * @date 2021-11-07 21:35
 */
public class Result {
   private boolean flag;//如果为false表明用户名/密码错误
   private String message;

   public Result() {
   }

   public Result(boolean flag, String message) {
      this.flag = flag;
      this.message = message;
   }

   public boolean isFlag() {
      return flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   @Override
   public String toString() {
      return "Result{" +
              "flag=" + flag +
              ", message='" + message + '\'' +
              '}';
   }
}
