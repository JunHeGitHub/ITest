package com.zinglabs.mq;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.zinglabs.log.LogUtil;

public class MQGet extends MQConnector
{
  public MQGet()
  {
    
  }
  
  public String getMessages(String[] args) throws MQException
  {
   String message=getMessageFromQueue();
   return message;
    
  }
  
  public String getMqMsg() {
      String ret=null;
      MQConnector.DEBUG=true;
      try
      {
        initMq("read");
        openQueue();
        /*设置放置消息选项*/
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        
//        gmo.options=MQC.MQOO_INPUT_AS_Q_DEF+MQC.MQOO_FAIL_IF_QUIESCING;
        
//        gmo.options = MQC.MQOO_OUTPUT;
//        gmo.options = MQC.MQPMO_LOGICAL_ORDER;

//        /*在同步点控制下获取消息*/
//        gmo.options = gmo.options + MQC.MQGMO_SYNCPOINT ;
  //
//        /*如果在队列上没有消息则等待*/
//        gmo.options = gmo.options + MQC.MQGMO_WAIT ;
  //
//        /*如果队列管理器停顿则失败*/
//        gmo.options = gmo.options + MQC.MQGMO_FAIL_IF_QUIESCING ; 

        /*设置等待的时间限制*/
        gmo.waitInterval = 3000 ;
        /*创建MQMessage 类*/
        MQMessage inMsg = new MQMessage(); 
        int queueDepth=mqQueue.getCurrentDepth();
        LogUtil.debug("queue depth : "+queueDepth,SKIP_Logger);
        if(queueDepth>0) {
            /*从队列到消息缓冲区获取消息*/
            mqQueue.get(inMsg, gmo);
            /*从消息读取用户数据*/
            ret= inMsg.readString(inMsg.getMessageLength());
//            LogUtil.debug(" The Message from the Queue is : " + msgString,SKIP_Logger);
        }
        closeQueue();
        disconnectMq();      
      }
      catch (Exception e)
      {
          LogUtil.error(e, SKIP_Logger);
      }
      
      return ret;
  }
  
  
  public static void main(String[] args)
  {
    MQGet mqget = new MQGet();
    MQConnector.DEBUG=true;
    try
    {
      mqget.initMq("read");
      mqget.openQueue();
      
      

      /*设置放置消息选项*/
      MQGetMessageOptions gmo = new MQGetMessageOptions();
      
//      gmo.options=MQC.MQOO_INPUT_AS_Q_DEF+MQC.MQOO_FAIL_IF_QUIESCING;
      
//      gmo.options = MQC.MQOO_OUTPUT;
//      gmo.options = MQC.MQPMO_LOGICAL_ORDER;

//      /*在同步点控制下获取消息*/
//      gmo.options = gmo.options + MQC.MQGMO_SYNCPOINT ;
//
//      /*如果在队列上没有消息则等待*/
//      gmo.options = gmo.options + MQC.MQGMO_WAIT ;
//
//      /*如果队列管理器停顿则失败*/
//      gmo.options = gmo.options + MQC.MQGMO_FAIL_IF_QUIESCING ; 

      /*设置等待的时间限制*/
      gmo.waitInterval = 3000 ;

      /*创建MQMessage 类*/
      MQMessage inMsg = new MQMessage(); 

      System.out.println("queue depth : "+mqget.mqQueue.getCurrentDepth());
      int queueDepth=mqget.mqQueue.getCurrentDepth();
      if(queueDepth>0) {
          /*从队列到消息缓冲区获取消息*/
          mqget.mqQueue.get(inMsg, gmo);

          
          /*从消息读取用户数据*/
          String msgString = inMsg.readString(inMsg.getMessageLength());
          System.out.println(" The Message from the Queue is : " + msgString);
          
      }
      
//      for(String msg=mqget.getMessages(args);msg!=null;msg=mqget.getMessages(args))
//      {
//         System.out.println(msg);
//      }
      
      mqget.closeQueue();
      mqget.disconnectMq();      
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("Usage: "+mqget.getClass().getName()+" ");
    }
  }

}
