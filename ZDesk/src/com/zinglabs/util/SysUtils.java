package com.zinglabs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;

public class SysUtils {
	public static Logger logger = LoggerFactory.getLogger("ZKM");
	
	/**
	 * 执行系统shell命令
	 * @param shellStr　shell命令串
	 * @return
	 */
	public static boolean execShell(String shellStr){
	    Runtime rt = Runtime.getRuntime();
	    try {
	    	logger.debug("Shell str --- : " + shellStr);
	        Process p = rt.exec(shellStr);
	        if(p.waitFor() != 0){
	        	logger.debug("Shell str --- : " + shellStr + " lost.");
	        	return false;
	        }
	    } catch (IOException e) {
	    	logger.debug("Shell str --- : " + shellStr + " lost.");
	        return false;
	    } catch (InterruptedException e) {
	    	logger.debug("shell执行错误：" + e.getMessage());
	    	logger.debug("Shell str --- : " + shellStr + " lost.");
	        e.printStackTrace();
	        return false;
	    }
	    logger.debug("Shell str --- : " + shellStr + " sucess.");
	    return true;
	}
	
	/**
	 * 执行系统shell命令,并返回执行结果
	 * @param shellStr　shell命令串
	 * @return
	 */
	public static String execShellResult(String shellStr){
	    Runtime rt = Runtime.getRuntime();
	    try {
	    	logger.debug("Shell str --- : " + shellStr);
	        Process p = rt.exec(shellStr);
	        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        StringBuffer sb = new StringBuffer();
	        String line;
	        while ((line = br.readLine()) != null) {
	        	sb.append(line).append("\n");
	        }
	        return sb.toString();
	    } catch (IOException e) {
	    	logger.debug("Shell str --- : " + shellStr + " lost.");
	    } catch (Exception e) {
	    	logger.debug("shell执行错误：" + e.getMessage());
	    	logger.debug("Shell str --- : " + shellStr + " lost.");
	        e.printStackTrace();
	    }
	    logger.debug("Shell str --- : " + shellStr + " sucess.");
	    return "";
	}
	
	/**
	 * liunx shell 改变文件或目录的所属用户与组
	 * @param user　用户名
	 * @param group　用户组
	 * @param filePath　文件或目录路径
	 * @param recursive　是否递归子目录及文件　true递归；false不递归
	 * @return　
	 */
	public static boolean linuxChangeFileUserGroup(String user,String group,String filePath,boolean recursive){
		String cmd="chown ";
		if(user.trim().length()>0 && group.trim().length()>0 && filePath.trim().length()>0){
			filePath=filePath.trim();
			File file=new File(filePath);
			if(file.exists()){
				user=user.trim();
				group=group.trim();
				cmd +=user+":" + group;
				if(recursive){
					cmd+=" -R ";
				}
				cmd+=" " +filePath;
				logger.debug("shell cmd : " + cmd);
				return execShell(cmd);
			}else{
				logger.error("linuxChangeFileUserGroup -- : 目录或文件不存在。" + filePath);
			}
		}
		return false;
	}
	
	/**
	 * linux 获取文件所属用户
	 * @param file 文件对象
	 * @return 用户名称
	 */
	public static String linuxGetFileUser(File file){
		String cmd="ls -ld " + file.getPath();
		String fn=file.getName();
		cmd=execShellResult(cmd);
		if(cmd.length()>0 && cmd.indexOf(fn)>0){
			String[] rs=cmd.split(" ");
			return rs[2];
		}
		return "";
	}
	
	/**
	 * linux 获取文件所属用户
	 * @param filePath 路径
	 * @return 用户名称
	 */
	public static String linuxGetFileUser(String filePath){
		File file=new File(filePath);
		if(file.exists()){
			return linuxGetFileUser(file);
		}
		return "";
	}
	
	/**
	 * linux 获取用户所属组
	 * @param file　文件对象
	 * @return　用户所属组
	 */
	public static String linuxGetFileGroup(File file){
		String cmd="ls -ld " + file.getPath();
		String fn=file.getName();
		cmd=execShellResult(cmd);
		if(cmd.length()>0 && cmd.indexOf(fn)>0){
			String[] rs=cmd.split(" ");
			return rs[3];
		}
		return "";
	}
	
	/**
	 * linux 获取用户所属组
	 * @param filePath　路径
	 * @return　用户所属组
	 */
	public static String linuxGetFileGroup(String filePath){
		File file=new File(filePath);
		if(file.exists()){
			return linuxGetFileGroup(file);
		}
		return "";
	}
	
	/**
	 * linux 执行shell指令
	 * @param filePath　路径
	 * @param way　删除方式
	 * @return　是否删除成功
	 */
	public static boolean linuxRemvoeFile(String filePath,String way){
		String cmd = "rm -" + way + " " +filePath;
		return execShell(cmd);
	}
	
	/**
	 * 
	 * @param dbName
	 * @param user
	 * @param pwd
	 * @param backFolder
	 * @param dumpName
	 * @param tables 表与表用空格分开
	 * @return
	 */
	public static boolean mysqlDump(String dbName,String user,String pwd,String backFolder,String dumpName,String tables){
		File file=new File(backFolder);
		if(file.isDirectory() && file.canWrite()){
			if(backFolder.lastIndexOf("/")==backFolder.length()-1){
				backFolder=backFolder.substring(0,backFolder.length()-2);
			}
			String cmd="";
			if(tables!=null && tables.length()>0){
				cmd="mysqldump -u" + user + " -p" + pwd + " " + dbName + " " + tables  + " > "  + backFolder + "/" + dumpName;
			}else {
				cmd="mysqldump -u" + user + " -p" + pwd + " " + dbName + " > "  + backFolder + "/" + dumpName;
			}
			return execShell(cmd);
		}else{
			logger.error("数据库导出失败：导出目录 " + backFolder + " 不存在或不可写。");
			return false;
		}
	}
	
	public final static String OS_WINDOWS_FLAG="Windows";
	public final static String OS_LINUX_FLAG="Linux";
	
	public static boolean commonShellMove(String sFile,String dFile,String type){
		if(type.equals(OS_WINDOWS_FLAG)){
			return dosCommonShellMove(sFile,dFile);
		}else if(type.equals(OS_LINUX_FLAG)){
			return linuxCommonShellMove(sFile,dFile);
		}
		return false;
	}
	
	public static boolean dosCommonShellMove(String sFile,String dFile){
		String commonStr="cmd /c move ";
		commonStr+= sFile + " " + dFile;
		return execShell(commonStr);
	}
	
	public static boolean linuxCommonShellMove(String sFile,String dFile){
		if(sFile.length()>0){
			sFile=sFile.replaceAll("//", "/");
		}
		if(dFile.length()>0){
			dFile=dFile.replaceAll("//", "/");
		}
		String commonStr="mv -f ";
		commonStr+= sFile + " " + dFile;
		return execShell(commonStr);
	}
	
	/**
	 * 获取操作系统名称
	 * indexOf Windows 是windows
	 * indexOf Linux 是linux
	 * @return
	 */
	public static String getSystemOSName(){
		Properties ps=System.getProperties();
		return ps.getProperty("os.name");
	}
	
	/**
	 * 获取本机IP地址
	 * @return
	 */
	public static List getLocalInetAddresses() throws Exception{
		Enumeration netInterfaces=NetworkInterface.getNetworkInterfaces();  
		InetAddress ip = null;
		List ips=new ArrayList();
		while(netInterfaces.hasMoreElements()){  
		    NetworkInterface ni=(NetworkInterface)netInterfaces.nextElement();  
		    Enumeration e2=ni.getInetAddresses();
		    while(e2.hasMoreElements()){
                ip=(InetAddress)e2.nextElement();
                if(ip instanceof Inet6Address){
                    continue;
                }
    		    ips.add(ip.getHostAddress().toString());
            }
		}
		return ips;
	}
	
	/**
	 * IP地比较
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static boolean ipMatched(String ip) throws Exception{
        if(ip==null){return false;}
        String ret=null;
        Enumeration<?> e1=(Enumeration<?>)NetworkInterface.getNetworkInterfaces();
        while(e1.hasMoreElements()){
            NetworkInterface ni=(NetworkInterface)e1.nextElement();
            Enumeration e2=ni.getInetAddresses();
            while(e2.hasMoreElements()){
                InetAddress ia=(InetAddress)e2.nextElement();
                if(ia instanceof Inet6Address){
                    continue;
                }
                ret=ia.getHostAddress().toString();
                if(ip.equals(ret)){
                    return true;
                }
            }
        }
        return false;
    }
	
	public static void main(String[] args) throws Exception{
		System.out.println(JSONSerializer.toJSON(getLocalInetAddresses()).toString());
	}
}
