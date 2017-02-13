package com.testing.mysqlMemory;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
public class TestMain {

	
	
	public void insertSignTest(){
		DAOTools dt=new DAOTools();
		String sql="insert into test_ values('test1','test_name1','1')," +
				"('test2','test_name2','2')," +
				"('test3','test_name3','3')";
		int i=dt.execUpdate(sql, ShareConf.confDBName);
		System.out.println(i);
	}
	
	/**
	 * 建立写入线程
	 * @param dts
	 * @param pnum
	 */
	public void startWrite(DAOTools dts,int pnum) throws Exception{
		for(int i=0;i<pnum;i++){
			TestWriteTask twt=new TestWriteTask();
			twt.pid="WRITE_" + i;
			twt.num=0;
			//twt.dbType=ShareConf.confDBName;
			twt.dt=dts;
			Thread trd=new Thread(twt);
			trd.start();
		}
	}
	/**
	 * 建立读取线程
	 * @param dts
	 * @param pnum
	 */
	public void startRead(DAOTools dts,int pnum) throws Exception{
		for(int i=0;i<pnum;i++){
			TestReadTask trt=new TestReadTask();
			trt.dbType=ShareConf.confDBName;
			trt.con=DAOTools.getConnectionOutS(trt.dbType);
			trt.pid="READ_" + i;
			trt.dt=dts;
			Thread trd=new Thread(trt);
			trd.start();
		}
	}
	
	public static void main(String [] args) throws Exception{
		TestMain tm=new TestMain();
//		tm.insertSignTest();
		DAOTools dt=new DAOTools();
		//启动写入线程
		
		//System.out.println(ConfInfo.threadNumberW + "  ----  " + ConfInfo.threadNumberR);
		
		tm.startWrite(dt,1);
		
		//tm.startRead(dt, ConfInfo.threadNumberR);
		//所有写入线程，开始写入。
		ShareConf.writeFlag=true;
		//Thread.sleep(3000);
		//所有读取线程，开始读取
		ShareConf.readFlag=true;
	}
	
}
