package com.zinglabs.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.absImpl.BaseAbs;


public class BusyFlag extends BaseAbs{
	protected Thread busyflag = null;
	protected int busycount = 0;
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	public BusyFlag(){
		init(SKIP_Logger);
	}
	
	public synchronized void getBusyFlag() {
//		info("-------Thread currentThread is-------"+Thread.currentThread().getName());
		while (tryGetBusyFlag() == false) {
			try {
				wait();
			} catch (Exception e) {}
		}
//		info("-get------Thread currentThread is-------"+Thread.currentThread().getName());
	}

	public synchronized boolean tryGetBusyFlag() {
		if (busyflag == null) {
			busyflag = Thread.currentThread();
			busycount = 1;
			return true;
		}
		if (busyflag == Thread.currentThread()) {
			busycount++;
			return true;
		}
		return false;
	}

	public synchronized void freeBusyFlag() {
		if (getBusyFlagOwner() == Thread.currentThread()) {
			busycount--;
			if (busycount == 0) {
				busyflag = null;
				notify();
			}
		}
	}

	public synchronized Thread getBusyFlagOwner() {
		return busyflag;
	}
}
