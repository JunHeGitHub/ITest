package com.testing.mysqlMemory;

public class ShareConf {
	
	public static String confDBName="ZShifts";
	
	public static boolean writeFlag=false;
	
	public static boolean readFlag=false;
	
	public static long writeSleep=500L;
	
	public static long readSleep=500L;
	
	public static boolean allRun=true;
	
	public static volatile long writeMax=0L;
	
	public static volatile long readMax=0L;
	
	public static volatile long queryuMax=0L;
	
	public static volatile long borowMax=0L;
}
