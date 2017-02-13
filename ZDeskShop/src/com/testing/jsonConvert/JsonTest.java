package com.testing.jsonConvert;

public class JsonTest {
	
	public long converToJSON_lib(){
		long sc=System.currentTimeMillis();
		
		
		return System.currentTimeMillis() -sc;
	}
	
	public static void main(String[] args){
		String ab="0010101.22";
		System.out.println(ab.substring(0,ab.indexOf(".")));
	}
	
}
