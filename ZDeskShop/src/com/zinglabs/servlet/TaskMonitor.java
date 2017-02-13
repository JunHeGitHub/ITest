package com.zinglabs.servlet;

import java.util.ArrayList;

public class TaskMonitor implements Runnable{

	public static ArrayList<Thread> tlist=new ArrayList<Thread>();
	
	public void run() {
		while(true){
			if(tlist.size()>0){
				for(Thread td:tlist){
					System.out.println("----------------- td see Id: " + td.getId() + " state: " + td.getState());
				}
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
