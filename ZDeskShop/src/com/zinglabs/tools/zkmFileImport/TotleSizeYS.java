package com.zinglabs.tools.zkmFileImport;

public class TotleSizeYS {
	/**
	 * 获取知识库文档总量大小
	 * 算法基础数值：当前全部文档总数：9763-3700-34-34-34-35 全部文档-理财产品-分支机构-自助设备-常见问题-营销活动
	 * 				 新增常用文档大小 10 * 2 * 3 (单word文档大小10MB；转换html后大小增加2倍左右；新增一个文档在服务器上会存三份)
	 * 				 新增非常用文档大小 0.4 * 2 * 3 (单word文档大小0.4 MB ... ...)
	 * 				 修订常用文档大小　10 * 2 (... ... 修订文档需要存储历史，文档大小乘2)
	 * 				 修订非常用文档大小 0.4 * 2 (... ... )
	 * 				 常用文档占总文档数74%
	 * 				 非常用文档占总文档数26%
	 * 				 每月新增文档为140 * 14.47%
	 * 				 每月新增的常用文档140 * 14.47% * 74%
	 * 				 每月新增的非常用文档140 * 14.47% * 26%
	 * 				 每月更新文档 全部文档+ 新增文档 * 44.78%(月更新率) 得到每月更新文档总数
	 * 				 每月更新常用文档　每月更新文档　* 74%
	 * 				 每月更新非常用文档 每月更新文档 * 26%
	 * 				 
	 * @param month 月数
	 * @return
	 */
	public static float getZKMDocSize(int month){
		
		float reSize=0;
		float totalDoc=9763-3700-34-34-34-35;
		
		float cy=(float)(totalDoc * 0.74);
		float fcy=(float)(totalDoc * 0.26);
		
		System.out.println("基础常用文档数量：" + cy);
		System.out.println("基础非常用文档数量：" + fcy);
		System.out.println("基础文档总数量：" + totalDoc);
		//新增一个文档大小
		float cyone=10  * 2 * 3;
		float fcyone=(float) (0.4 * 2 * 3);
		//修改一个文档大小
		float mcyone=10 *2;
		float mfcyone=(float) (0.4 * 2);
		
		float cySize=cy * cyone;
		float fcySize=(float) ((fcy *fcyone));
		float totalSize=cySize + fcySize;
		
		System.out.println("基础常用文档占用空间：" + (cySize/1000) + " GB");
		System.out.println("基础非常用文档占用空间：" + (fcySize/1000) + " GB");
		System.out.println("基础文档占用总空间：" + (totalSize/1000) + " GB");
		
		System.out.println("");
		
		float newDocNum=(float) (140 * 0.1447);
		float addcyNum=(float) (newDocNum * 0.74);
		float addfcyNum=(float) (newDocNum * 0.26);
		float mcyDocTotal=0;
		float mfcyDocTotal=0;
		float mDocTotal=0;
		float mDocTotalSize=0;
		float ncyDocTotal=0;
		float nfcyDocTotal=0;
		float nDoctTotal=0;
		float nDocTotalSize=0;
		
		for(int i=0;i<month;i++){
			//常用文档总量
			cy+=addcyNum;
			//非常用文档总量
			fcy+=addfcyNum;
			//参数月常用文档增长量
			ncyDocTotal+=addcyNum;
			//参数月非常用文档增长量
			nfcyDocTotal+=addfcyNum;
			//参数月总增长量
			nDoctTotal+=addcyNum + addfcyNum;
			//参数月增长空间大小
			nDocTotalSize+=addcyNum * cyone;
			nDocTotalSize+=addfcyNum * fcyone;
			
			cySize+=addcyNum * cyone;
			fcySize+=addfcyNum * fcyone;
			
			//文档总大小 + 新增文档大小
			totalSize+=addcyNum * cyone;
			totalSize+=addfcyNum * fcyone;
			//参数月总文档数
			totalDoc+=addcyNum + addfcyNum;
			//每月更新文档数
			float modiDoc=(float) (totalDoc * 0.4478);
			//参数月更新文档总数
			mDocTotal+=modiDoc;
			//参数月常用文档更新数
			mcyDocTotal+=modiDoc * 0.74;
			//参数月非常用文档更新数
			mfcyDocTotal+=modiDoc * 0.26;
			//常用文档总大小
			cySize +=(float) (modiDoc * 0.74 * mcyone);
			//非常用文档总大小
			fcySize+=(float) (modiDoc * 0.26 * mfcyone);
			//参数月更新空间大小
			mDocTotalSize+=(float) (modiDoc * 0.74 * mcyone);
			mDocTotalSize+=(float) (modiDoc * 0.26 * mfcyone);
		}
		
		totalSize=cySize + fcySize;
		
		System.out.println("新增常用文档总数：" +ncyDocTotal);
		System.out.println("新增非常用文档总数：" +nfcyDocTotal);
		System.out.println("新增文档总数：" +nDoctTotal);
		System.out.println("新增文档占用空间大小：" +(nDocTotalSize/1000) + " GB");
		System.out.println("");
		System.out.println("修订常用文档总数：" +mcyDocTotal);
		System.out.println("修订非常用文档总数：" +mfcyDocTotal);
		System.out.println("修订文档总数：" +mDocTotal);
		System.out.println("修订文档占用空间大小：" +(mDocTotalSize/1000) + " GB");
		System.out.println("");
		System.out.println("知识库文档总数：" +totalDoc);
		System.out.println("常用文档数量：" +cy);
		float nohCy=(float)(totalDoc * 0.74) * cyone;
		System.out.println("无历史记录，常用文档空间大小：" + (nohCy/1000) + " GB");
		System.out.println("非常用文档数量：" +fcy);
		float nohFcy=(float)(totalDoc * 0.74) * fcyone;
		System.out.println("无历史记录，非常用文档空间大小：" + (nohFcy/1000) + " GB");
		System.out.println("不包含历史记录文档占用空间大小：" + ((nohCy + nohFcy)/1000) + " GB");
		System.out.println("");
		System.out.println("常用文档大小: " + (cySize/1000) + " GB 非常用文档大小: " + (fcySize/1000) + " GB 总大小: " + (totalSize/1000) + " GB");
		
		System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		reSize=(totalSize/1000);
		
		return reSize;
	}
	
	/**
	 * 获取理财产品大小
	 * 算法基础数值：当前全部理财产品：3700
	 * 				 每理财产品大小 0.25 * 2 (0.25单条大小；每理财都有一个流程中的复制)
	 * 				 月增长量 140 * 78.90%
	 *               月更新量 全部理财产品 * 75.19%
	 * @param month 月份
	 * @return
	 */
	public static float getLCCPSize(int month){
		float reSize=0;
		
		float totalLCCP=3700;
		float oneLCCP=(float) (0.25 * 2) ;
		float totalLCCPSize=totalLCCP * oneLCCP;
		float nLCCPTotal=0;
		float nLCCPSize=0;
		float mLCCPTotal=0;
		float mLCCPSize=0;
		
		System.out.println("理财产品基础量：" +totalLCCP);
		System.out.println("理财产品基础大小：" + (totalLCCPSize/1000) + " GB");
		System.out.println("");
		
		float nLCCPNum=(float) (140 * 0.789);
		for(int i=0;i<month;i++){
			//参数月新增理财产品数量
			nLCCPTotal+=nLCCPNum;
			//理财产品总大小
			totalLCCPSize+=nLCCPNum * oneLCCP;
			nLCCPSize+=nLCCPNum * oneLCCP;
			//理财产品总量
			totalLCCP+=nLCCPNum;
			//理财产品更新数
			float mnum=(float) (totalLCCP * 0.7519);
			//参数月理财产品更新数
			mLCCPTotal+=mnum;
			mLCCPSize+=mnum * oneLCCP;
			//理财产品总大小
			totalLCCPSize+=mnum * oneLCCP;
		}
		
		System.out.println("新增理财产品：" +nLCCPTotal);
		System.out.println("新增理财产品，占用空间大小：" +(nLCCPSize/1000) + " GB");
		System.out.println("更新理财产品：" +mLCCPTotal);
		System.out.println("更新理财产品，占用空间大小：" +(mLCCPSize/1000) + " GB");
		System.out.println("");
		System.out.println("理财产品总数 : " + totalLCCP +" 理财产品总大小 : " + (totalLCCPSize/1000) + " GB");
		System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		reSize=(totalLCCPSize/1000);
		
		return reSize;
	}
	
	/**
	 * 分支机构大小
	 * 算法基础数值：当前全部分支机构：34
	 * 				 每理财产品大小 2.1 * 2 (2.1单条大小；每分支机构都有一个流程中的复制)
	 * 				 月增长量 0
	 *               月更新量 全部分支机构 * 615%
	 * @param month 月份
	 * @return
	 */
	public static float getFZJGSize(int month){
		float reSize=0;
		
		float totalFZJG=34;
		float onFZJG=(float) (2.1 * 2);
		float totalFZJGSize=(float) (totalFZJG * onFZJG);
		float nFZJGotal=0;
		float nFZJGSize=0;
		float mFZJGTotal=0;
		float mFZJGSize=0;
		
		System.out.println("分支机构基础量：" +totalFZJG);
		System.out.println("分支机构基础大小：" + (totalFZJGSize/1000) + " GB");
		System.out.println("");
		
		for(int i=0;i<month;i++){
			
			float mFZJG=(float) (totalFZJG * 6.15);
			mFZJGTotal+=mFZJG;
			mFZJGSize+=mFZJG * onFZJG;
			totalFZJGSize+=mFZJG * onFZJG;
		}
		
		System.out.println("新增分支机构：" +nFZJGotal);
		System.out.println("新增分支机构，占用空间大小：" + (nFZJGSize/1000)+ " GB");
		System.out.println("更新分支机构：" +mFZJGTotal);
		System.out.println("更新分支机构，占用空间大小：" + (mFZJGSize/1000)+ " GB");
		System.out.println("");
		System.out.println("分支机构总数 : " + totalFZJG +" 分支机构总大小 : " + (totalFZJGSize/1000) + " GB");
		
		System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		reSize=(totalFZJGSize/1000);
		
		return reSize;
	}
	
	
	/**
	 * 自助设备大小
	 * 算法基础数值：当前全部自助设备：34
	 * 				 每自助设备大小 2.1 * 2 (2.1单条大小；每分支机构都有一个流程中的复制)
	 * 				 月增长量 0
	 *               月更新量 全部自助设备 * 615%
	 * @param month 月份
	 * @return
	 */
	public static float getZZSBSize(int month){
		float reSize=0;
		
		float totalZZSB=34;
		float oneZZSB=(float) (2.1 * 2) ;
		float totalZZSBSize=(float) (totalZZSB * oneZZSB);
		float nZZSBotal=0;
		float nZZSBSize=0;
		float mZZSBTotal=0;
		float mZZSBSize=0;
		
		System.out.println("自助设备基础量：" +totalZZSB);
		System.out.println("自助设备基础大小：" + (totalZZSBSize/1000) + " GB");
		System.out.println("");
		
		for(int i=0;i<month;i++){
			float mFZJG=(float) (totalZZSB * 6.15);
			mZZSBTotal+=mFZJG;
			mZZSBSize+=mFZJG * oneZZSB;
			totalZZSBSize+=mFZJG * oneZZSB;
		}
		
		
		System.out.println("新增自助设备：" +nZZSBotal);
		System.out.println("新增自助设备，占用空间大小：" +(nZZSBSize/1000) + " GB");
		System.out.println("更新自助设备：" +mZZSBTotal);
		System.out.println("更新自助设备，占用空间大小：" +(mZZSBSize/1000) + " GB");
		System.out.println("");
		System.out.println("自助设备总数 : " + totalZZSB +" 自助设备总大小 : " + (totalZZSBSize/1000) + " GB");
		
		System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		reSize=(totalZZSBSize/1000);
		
		return reSize;
	}
	
	
	/**
	 * 常见问题大小
	 * 算法基础数值：当前全部常见问题：34
	 * 				 每常见问题大小 2.3 * 2 (2.3单条大小；每分支机构都有一个流程中的复制)
	 * 				 月增长量 140 * 0.1447
	 *               月更新量 全部常见问题 * 44.78%
	 * @param month 月份
	 * @return
	 */
	public static float getCJWTSize(int month){
		float reSize=0;
		float totalCJWT=34;
		float oneCJWT=(float) (2.3 * 2);
		float totalCJWTSize=(float) (totalCJWT * oneCJWT);
		float nCJWTTotal=0;
		float nCJWTSize=0;
		float mCJWTTotal=0;
		float mCJWTSize=0;
		
		System.out.println("常见问题基础量：" +totalCJWT);
		System.out.println("常见问题基础大小：" + (totalCJWTSize/1000) + " GB");
		System.out.println("");
		
		float nCJWTNum=(float) (140 * 0.1447);
		for(int i=0;i<month;i++){
			nCJWTTotal+=nCJWTNum;
			totalCJWT+=nCJWTNum;
			nCJWTSize+=nCJWTNum * oneCJWT;
			totalCJWTSize+=nCJWTNum * oneCJWT;
			
			float mCJWT=(float) (nCJWTTotal * 0.6666);
			mCJWTTotal+=mCJWT;
			mCJWTSize+=mCJWT * oneCJWT;
			totalCJWTSize+=mCJWT * oneCJWT;
		}
		
		System.out.println("新增常见问题：" +nCJWTTotal);
		System.out.println("新增常见问题，占用空间大小：" + (nCJWTSize/1000) + " GB");
		System.out.println("更新常见问题：" +mCJWTTotal);
		System.out.println("更新常见问题，占用空间大小：" + (mCJWTSize/1000) + " GB");
		System.out.println("");
		System.out.println("常见问题总数 : " + totalCJWT +" 常见问题总大小 : " + (totalCJWTSize/1000) + " GB");
		
		System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		reSize=(totalCJWTSize/1000);
		
		return reSize;
	}
	
	/**
	 * 营销活动大小
	 * 算法基础数值：当前全部 营销活动：35
	 * 				 每营销活动大小 3.9 * 2 (3.9单条大小；每分支机构都有一个流程中的复制)
	 * 				 月增长量 140 * 0.658
	 *               月更新量 全部营销活动 * 66.66%
	 * @param month 月份
	 * @return
	 */
	public static float getYXHDSize(int month){
		float reSize=0;
		
		float totalYXHD=35;
		float oneYXHD=(float) (3.9 * 2);
		float totalYXHDSize=(float) (totalYXHD * oneYXHD);
		float nYXHDotal=0;
		float nYXHDSize=0;
		float mYXHDTotal=0;
		float mYXHDSize=0;
		
		System.out.println("营销活动基础量：" +totalYXHD);
		System.out.println("营销活动基础大小：" + (totalYXHDSize/1000) + " GB");
		System.out.println("");
		
		float nYXHDNum=(float) (140 * 0.658);
		for(int i=0;i<month;i++){
			nYXHDotal+=nYXHDNum;
			totalYXHD+=nYXHDNum;
			nYXHDSize+=nYXHDNum * oneYXHD;
			totalYXHDSize+=nYXHDNum * oneYXHD;
			
			float mYXHD=(float) (totalYXHD * 0.6666);
			mYXHDTotal+=mYXHD;
			mYXHDSize+=mYXHD * oneYXHD;
			totalYXHDSize+=mYXHD * oneYXHD;
		}
		
		System.out.println("新增营销活动：" +nYXHDotal);
		System.out.println("新增营销活动，占用空间大小：" + (nYXHDSize/1000) + " GB");
		System.out.println("更新营销活动：" +mYXHDTotal);
		System.out.println("更新营销活动，占用空间大小：" + (mYXHDSize/1000) + " GB");
		System.out.println("");
		System.out.println("营销活动总数 : " + totalYXHD +" 营销活动总大小 : " + (totalYXHDSize/1000) + " GB");
		
		System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		reSize=(totalYXHDSize/1000);
		
		return reSize;
	}
	
	
	public static void main(String [] args){
		int month=12;
		
		float totalSize=TotleSizeYS.getZKMDocSize(month);
		totalSize+=TotleSizeYS.getLCCPSize(month);
		totalSize+=TotleSizeYS.getFZJGSize(month);
		totalSize+=TotleSizeYS.getZZSBSize(month);
		totalSize+=TotleSizeYS.getCJWTSize(month);
		totalSize+=TotleSizeYS.getYXHDSize(month);
		
		System.out.println("");
		System.out.println("合计大小：" + totalSize + " GB 计：" +  (totalSize/1000) + " TB");
		System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
	}
	
}
