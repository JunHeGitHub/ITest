package com.zinglabs.mq;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
//
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
//import com.ibm.mq.MQException;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.sybase.jdbc3.tds.e;
import com.zinglabs.log.LogUtil;
//import com.zinglabs.xml.XmlDBAction;

public class MQConnector
{
    
  protected String readFlag;
    
  protected String qManager = ""; // define name of queue manager
  
  protected String RChanel = "";
  protected String SChanel = "";

  protected String qManagerHost = "";

  protected String queuName = ""; // define name of queue

  protected MQQueue mqQueue;

  protected MQQueueManager qMgr;

  public static boolean DEBUG = true;
  
  public int MQEncoding;
  
  public int MQCharacterSet;
  
  public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
  
  public MQConnector()
  {
	  
  }

  public void initMq(String flag) {
		try {
			Properties props = new Properties();
			String fileName = "/mnt/mqconnect.properties";
//			String fileName = "c:\\mqconnect.properties";
//			String fileName = "mqconnect.properties";
			// String fileName = "c:\\cvnet.conf";
			// String fileName = "c:\\cvnetReport.conf";
			File msgFile = new File(fileName);
			FileInputStream in = new FileInputStream(msgFile);
			props.load(in);
			in.close();

			// FileInputStream fis = new FileInputStream(new
			// File("mqconnect.properties"));
			// Properties props = new Properties();
			// props.load(fis);
			// fis.close();
			String chanelTmp="";
			String portTmp="";
			
			
			if(flag.equals("read")){
				qManager = props.getProperty("queueR.manager");
				qManagerHost = props.getProperty("queueR.manager.host");
				queuName = props.getProperty("queueR.name");
				qManager = props.getProperty("queueR.manager");
				chanelTmp= props.getProperty("queueR.channel");
				portTmp= props.getProperty("queueR.port");
				if(props.getProperty("queueR.CCSID")!=null && props.getProperty("queueR.CCSID").length()>0) {
				    MQEnvironment.CCSID= Integer.parseInt(props.getProperty("queueR.CCSID"));
				}
			}else if(flag.equals("send")){
				qManager = props.getProperty("queueW.manager");
				qManagerHost = props.getProperty("queueW.manager.host");
				queuName = props.getProperty("queueW.name");
				chanelTmp= props.getProperty("queueW.channel");
				portTmp= props.getProperty("queueW.port");
				
				if(props.getProperty("queueW.CCSID")!=null && props.getProperty("queueW.CCSID").length()>0) {
                    MQEnvironment.CCSID= Integer.parseInt(props.getProperty("queueW.CCSID"));
                    MQCharacterSet=MQEnvironment.CCSID;
                    MQEncoding=MQEnvironment.CCSID;
                }
//				MQEnvironment.CCSID= props.getProperty("queueW.CCSID");
			}
			
			
			readFlag=flag;
			// Create a connection to the queue manager
			// MQEnvironment.channel = "SYSTEM.DEF.SVRCONN";
			MQEnvironment.channel = chanelTmp;
			MQEnvironment.hostname = qManagerHost;
			if(portTmp!=null && portTmp.length()>0) {
			    MQEnvironment.port=Integer.parseInt(portTmp);
			}
			
			
			MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES);  
			
//			MQEnvironment.CCSID = 1381;
			debug("Connecting to QueueManager " + qManager + " on "+ qManagerHost+" ccsid:"+MQEnvironment.CCSID);
			qMgr = new MQQueueManager(qManager);

		} catch (Exception e) {
//		    e.printStackTrace();
		    LogUtil.error(e, SKIP_Logger);
		}
	}

  public void openQueue() throws MQException
  {

    // Set up the options on the queue we wish to open...
    // Note. All WebSphere MQ Options are prefixed with MQC in Java.
//     int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT;
//	int openOptions = MQC.MQOO_OUTPUT |MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_FAIL_IF_QUIESCING |MQC.MQOO_INQUIRE;
//	int openOptions = MQC.MQOO_OUTPUT |MQC.MQOO_INPUT_AS_Q_DEF|MQC.MQOO_INQUIRE;
//      发
//    int openOptions = MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING ;
//      收
      
      int openOptions ;
      if(readFlag.equals("read")){
          openOptions = MQC.MQOO_INPUT_AS_Q_DEF|MQC.MQOO_OUTPUT|MQC.MQOO_INQUIRE;
      }else {
          openOptions = MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING ;
      }
        
//	MQC.MQOO_INPUT_AS_Q_DEF|MQC.MQOO_OUTPUT|MQC.MQOO_INQUIRE
      
//      int openOptions = MQC.MQOO_INPUT_SHARED | MQC.MQOO_FAIL_IF_QUIESCING ;
      
      
    // Now specify the queue that we wish to open,
    // and the open options...    
    debug("Opening queue: " + queuName);
    try
    {
      mqQueue = qMgr.accessQueue(queuName, openOptions);
    }
    catch(MQException mqe)
    {
      //check if MQ reason code 2045
      //means that opened queu is remote and it can not be opened as 
      //input queue
      //try to open as output only
      if(mqe.reasonCode==2045)
      {
        openOptions = MQC.MQOO_OUTPUT;
        mqQueue = qMgr.accessQueue(queuName, openOptions);
      }
//      mqe.printStackTrace();
      LogUtil.error(mqe, SKIP_Logger);
      
    }
  }
    
  public void putMessageToQueue(String msg) throws MQException
  {
    try
    {
      debug("Sending message: " + msg);

      MQPutMessageOptions pmo = new MQPutMessageOptions(); 
      MQMessage mqMsg = new MQMessage();
      mqMsg.characterSet=MQCharacterSet;
      mqMsg.encoding=MQEncoding;
      mqMsg.write(msg.getBytes());

      // put the message on the queue
      mqQueue.put(mqMsg, pmo);
    }
    catch (IOException e)
    {
//        e.printStackTrace();
      LogUtil.error(e, SKIP_Logger);
    }
  }

  public String getMessageFromQueue() throws MQException 
  {
    try
    {
      MQMessage mqMsg = new MQMessage();
      
      MQGetMessageOptions gmo = new MQGetMessageOptions();

      // Get a message from the queue
      mqQueue.get(mqMsg,gmo);  
       
      //Extract the message data
      int len=mqMsg.getDataLength();
      byte[] message = new byte[len];
      mqMsg.readFully(message,0,len);
      return new String(message);
    }
    catch(MQException mqe)
    {
      int reason=mqe.reasonCode;
      
      if(reason==2033)//no messages
      {
        return null;
      }
      else
      {
        throw mqe;
      }
    }    
    catch (IOException e)
    {
      e.printStackTrace();
      return null;
    }
  }
  
  
  public void closeQueue() throws MQException
  {
    debug("Closing queue...");

    // Close the queue...
    mqQueue.close();

  }

  public void disconnectMq() throws MQException
  {
    debug("Disconnecting QueueManager...");

    // Disconnect from the queue manager
    qMgr.disconnect();

  }

  
  protected boolean hasArg(String arg, String[] args)
  {
    for(int i=0;i<args.length;i++)
    {
      if(args[i].equals(arg))
      {
        return true;
      }
    }
    return false;
  }
  
  public void debug(Object msg)
  {
    if (DEBUG && msg!=null)
    {
      LogUtil.debug(msg.toString(), SKIP_Logger);
    }
  }

  
  
}

  
