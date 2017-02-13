package com.testing.tools;

public class Moneyoperation {
	
	public float operationTotal2Profit(float totl,float d,float e){
		float a=totl*d;
		float r=(totl-a)*e;
		return r;
	}
	
	public float operationProfit2Total(float tl,float d,float e){
		float a=1;
		float b=a-d;
		float c=b*e;
		float r=20/(b-c);
		return r;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Moneyoperation mpt=new Moneyoperation();
		System.out.println(mpt.operationProfit2Total(20, Float.valueOf("0.23"), Float.valueOf("0.4")));
		
		System.out.println(mpt.operationTotal2Profit(89, Float.valueOf("0.23"), Float.valueOf("0.6")));
	}

}
