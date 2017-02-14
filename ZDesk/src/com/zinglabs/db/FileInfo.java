package com.zinglabs.db;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;
import com.zinglabs.tools.Utility;

public class FileInfo
{
//    数据缓存
    public StringBuffer data=new StringBuffer();
//    上次写入文件位置
    public int fileEndPos=0;
//    当前已缓存条数
    public int msgLine=0;
//    写入文件名
    public String fileName;
    
    public String fileDir="";
    // 缓存数据条数
    public int maxMsgLine;
    
    public synchronized void appendMsg(String msg) {
        this.data.append(msg);
        this.msgLine++;
    }
    
    /**
     * @param isEnd 文件结束标识
     * @throws Exception
     * 1.一个文件一个锁，不影响性能。
     * 2.一台服务器只有一个容器中运行，不会发生文件写争用。
     */
    public synchronized void toFile(boolean isEnd) throws Exception {
        
        LogUtil.debug("toFile "+(data!=null && data.length()>0)+" "+msgLine+" "+maxMsgLine+" "+fileName, SKIP_Logger);
        
        if(data!=null && data.length()>0 && (msgLine>=maxMsgLine || isEnd)) {
            LogUtil.debug("FileInfo toFile "+this.fileDir+" "+this.fileName, SKIP_Logger);
            File dateDir = new File(this.fileDir);
            if(!dateDir.exists())dateDir.mkdir();
            
            this.fileEndPos=Utility.writeToFileEnd2(this.fileDir+this.fileName, data.toString().getBytes(), this.fileEndPos);
            if(!isEnd) {
                this.data=new StringBuffer();
            }
            this.msgLine=0;
        }
    }
    
    public static void main(String[] arg) {
        FileInfo fTmp=new FileInfo();
        fTmp.fileName="aaa.csv";
        fTmp.maxMsgLine=100;
        
        for(int i=0;i<1001;i++) {
            fTmp.appendMsg("a,b,c,d,e,"+i);
            try
            {
                fTmp.toFile(false);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        
        try
        {
            fTmp.toFile(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
