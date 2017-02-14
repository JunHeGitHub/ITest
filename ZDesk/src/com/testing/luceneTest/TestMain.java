package com.testing.luceneTest;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.tokenattributes.TermAttribute;
//import org.wltea.analyzer.lucene.IKTokenizer;

import com.zinglabs.luceneSearch.LuceneBase;



public class TestMain {

	
	public static void main(String[] args) {
		/*
		WriterTask wt=new WriterTask();
		wt.fileDir="E:/lunceTest/datas";
		wt.trName="wt";
		Thread tw=new Thread(wt);
		WriterTask wt1=new WriterTask();
		wt1.fileDir="E:/lunceTest/imp-files";
		wt1.trName="wt1";
		Thread tw1=new Thread(wt1);
		WriterTask wt2=new WriterTask();
		wt2.fileDir="E:/lunceTest/imp-files1";
		wt2.trName="wt2";
		Thread tw2=new Thread(wt2);
		
		WriterTask wt3=new WriterTask();
		wt3.fileDir="E:/lunceTest/imp-files2";
		wt3.trName="wt3";
		Thread tw3=new Thread(wt3);
		
		WriterTask wt4=new WriterTask();
		wt4.fileDir="E:/lunceTest/imp-files3";
		wt4.trName="wt4";
		Thread tw4=new Thread(wt4);
		
		//tw.start();
		//tw1.start();
		//tw2.start();
		//tw3.start();
		//tw4.start();
		
		String [][] qstr={
				{"contents","啃文书库",LuceneBase.TREM_OCCUR_SHOULD},
				{"contents","蹂躏",LuceneBase.TREM_OCCUR_SHOULD},
				{"contents","趾高气昂",LuceneBase.TREM_OCCUR_SHOULD}
		};
		
		String [][] qstr1={
				{"contents","我们",LuceneBase.TREM_OCCUR_MUST}
		};
		String [][] qstr2={
				{"contents","银行",LuceneBase.TREM_OCCUR_MUST}
		};
		
		String [][] qstr3={
				{"contents","趾高气昂",LuceneBase.TREM_OCCUR_MUST},
		};
		
		ReadTask rt=new ReadTask();
		rt.qstr=qstr;
		rt.trName="rt";
		Thread tr=new Thread(rt);
		
		ReadTask rt1=new ReadTask();
		rt1.qstr=qstr1;
		rt1.trName="rt1";
		Thread tr1=new Thread(rt1);
		
		ReadTask rt2=new ReadTask();
		rt2.qstr=qstr2;
		rt2.trName="rt2";
		Thread tr2=new Thread(rt2);
		
		for(int i=0;i<100;i++){
			ReadTask rts=new ReadTask();
			rts.qstr=qstr;
			rts.trName="rt" + i;
			Thread trs=new Thread(rts);
			trs.start();
		}
		
		//tr.start();
		//tr1.start();
		//tr2.start();
		*/
//		IKTokenizer tokenizer = new IKTokenizer(new StringReader("我是中国人") , false);
//		try {
//			while(tokenizer.incrementToken()){
//				TermAttribute termAtt = tokenizer.getAttribute(TermAttribute.class);
//				System.out.println(termAtt);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
