package com.zinglabs.util.solrj;

import java.util.HashMap;

public class PutdocThread implements Runnable{
	
	public HashMap<String,String> doc;
	
	public String serverKey;
	
	public void run() {
		try{
			Thread.sleep(200);
			if(doc!=null){
				SolrjUtil.updateIndexDoc(doc,serverKey);
			}
		}catch(Exception x){
			x.printStackTrace();
		}
	}
}
