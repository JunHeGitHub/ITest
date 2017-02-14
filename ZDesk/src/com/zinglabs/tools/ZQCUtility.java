package com.zinglabs.tools;

import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.base.Interface.CallBackInterface;
import com.zinglabs.db.CallBackParam;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;



/*质检中业务相关代码，非工具，不应该放到Utility工具类中 ，临时拆分到这里*/
public class ZQCUtility {

	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");

	/**
	 *
	 * @param doc
	 * @param absPath
	 * @return
	 * @throws Exception
	 * DESC 各数据有效性 如agents 质检员数量 assignFun分配算法 评分指标 分配数量 由客户端保证
	 * 数字字段的不为0验证服务器端处理
	 *
	 */
	public static int assignRecord(Document doc,String absPath,String userName) throws Exception{
		LogUtil.info("assignRecord begin ", SKIP_Logger);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String assignFun=null,dicValue=null,lengthM=null,
		lengthL=null,beginTime=null,endTime=null,businessType=null,agentId=null,
		recordCount=null;
		Timestamp timeBeg=null,timeEnd=null;

		CallBackInterface callBack =new CallBackAssign();


		StringBuffer sbSelect=new StringBuffer();
 		Node clientRoot = DOMTool.getSingleNode(doc, "client");
		Node ZQCAgents = DOMTool.getSingleNode(clientRoot, "ZQCAgents");
		NodeList nlAgents = DOMTool.getMultiNodes(ZQCAgents, "agent");


		Node assignRecord= DOMTool.getSingleNode(clientRoot, "assignRecord");

		assignFun=DOMTool.getAttributeValue((Element)assignRecord,"assign_fun");
		dicValue=DOMTool.getAttributeValue((Element)assignRecord,"dict_type");
		lengthM=DOMTool.getAttributeValue((Element)assignRecord,"record_lengthM");
		lengthL=DOMTool.getAttributeValue((Element)assignRecord,"record_lengthL");
		beginTime=DOMTool.getAttributeValue((Element)assignRecord,"begin_time");
		if(beginTime!=null && beginTime.length()>0){
			timeBeg=new Timestamp(Long.parseLong(beginTime));
		}
		endTime=DOMTool.getAttributeValue((Element)assignRecord,"end_time");
		if(endTime!=null && endTime.length()>0){
			timeEnd=new Timestamp(Long.parseLong(endTime));
		}
		businessType=DOMTool.getAttributeValue((Element)assignRecord,"business_type");
		agentId=DOMTool.getAttributeValue((Element)assignRecord,"agent_id");
		String[] a =agentId.split(":");
		agentId = a[0];
		recordCount=DOMTool.getAttributeValue((Element)assignRecord,"record_count");


		sbSelect.append("select file_name,begin_time,id from  SU_QC_SOURCE_RECORD_DATA ");
		sbSelect.append("where 1=1 ");
		if(timeBeg!=null && timeEnd!=null){
			sbSelect.append(" and ");
			sbSelect.append(" begin_time ");
			sbSelect.append(" >= ");
			sbSelect.append(" '");
			sbSelect.append(f.format(timeBeg));
			sbSelect.append("' ");

			sbSelect.append(" and ");
			sbSelect.append(" end_time ");
			sbSelect.append(" <= ");
			sbSelect.append(" '");
			sbSelect.append(f.format(timeEnd));
			sbSelect.append("' ");
		}
		try {
			if(lengthM!=null && lengthM.length()>0 && Integer.parseInt(lengthM)>0){
				sbSelect.append(" and ");
				sbSelect.append(" record_length ");
				sbSelect.append(" >= ");
				sbSelect.append(lengthM);
			}

			if(lengthL!=null && lengthL.length()>0&& Integer.parseInt(lengthL)>0){
				sbSelect.append(" and ");
				sbSelect.append(" record_length ");
				sbSelect.append(" <= ");
				sbSelect.append(lengthL);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}


		if(businessType!=null && businessType.length()>0){
			sbSelect.append(" and ");
			sbSelect.append(" business_type ");
			sbSelect.append(" = ");
			sbSelect.append(" '");
			sbSelect.append(businessType.toString());
			sbSelect.append("' ");

		}

		if(agentId!=null && agentId.length()>0){
			String agentIdIn=agentId.replaceAll(BaseInterface.MULTI_COMBO_BOX_SPLITOR, "','");
			sbSelect.append(" and ");
			sbSelect.append(" alias1 ");
			sbSelect.append(" in('");
			sbSelect.append(agentIdIn);
			sbSelect.append("') ");
//			agentIdIn="'"+agentIdIn+"'";
//			String[] strArr=agentId.split(BaseInterface.MULTI_COMBO_BOX_SPLITOR);
//			String strTmp=null;
//			for(int i=0;i<strArr.length;i++){
//				strTmp=strArr[i];
//
//
//			}
		}
		callBack.setCriteria("assingFun",assignFun);
//		callBack.assingFun=assignFun;
		if(recordCount!=null && recordCount.length()>0){
			if(Integer.parseInt(recordCount)>0){
				callBack.setCriteria("recordCount",recordCount);
			}else{
				callBack.setCriteria("recordCount",String.valueOf(BaseInterface.ZQC_DEFULT_RECORD_COUNT));
			}
		}/*else{

		}*/
		callBack.setCriteria("agents",nlAgents);

		callBack.setCriteria("beginTime", timeBeg);
		callBack.setCriteria("endTime", timeEnd);
		callBack.setCriteria("dicValue", dicValue);
		callBack.setCriteria("assignRecord", "assignRecord");


		if(assignFun.equals("最大通话时长分配")){
			sbSelect.append("  and ( data_state = '初检未分配'  or data_state = '0' or data_state = '')   order by record_length desc limit 0 ,"+recordCount);
		}else if(assignFun.equals("随机分配")){
			sbSelect.append("  and ( data_state = '初检未分配'  or data_state = '0' or data_state = '')   order by begin_time asc limit 0 ,"+recordCount);
		}else{
				sbSelect.append(" order by begin_time asc");
		}

		System.out.println("sql===="+sbSelect.toString());
		LogUtil.debug(sbSelect.toString(), SKIP_Logger);
		DAOTools.execSelectCallBackS(sbSelect.toString()/*,int colNum*/,"ZQC",callBack,BaseInterface.CALL_BACK_SQL_TYPE_STREAM,userName);

//		String id=DOMTool.getAttributeValue((Element)clientRoot, "id");
		return -1;
	}

	public static int fjassignRecord(Document doc,String absPath,String userName) throws Exception{
		LogUtil.info("fjassignRecord begin ", SKIP_Logger);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String assignFun=null,dicValue=null,lengthM=null,
		lengthL=null,beginTime=null,endTime=null,businessType=null,agentId=null,
		recordCount=null;
		Timestamp timeBeg=null,timeEnd=null;

		CallBackInterface callBack =new CallBackAssign();


		StringBuffer sbSelect=new StringBuffer();
		Node clientRoot = DOMTool.getSingleNode(doc, "client");
		Node ZQCAgents = DOMTool.getSingleNode(clientRoot, "ZQCAgents");
		NodeList nlAgents = DOMTool.getMultiNodes(ZQCAgents, "agent");


		Node assignRecord= DOMTool.getSingleNode(clientRoot, "fjassignRecord");

		assignFun=DOMTool.getAttributeValue((Element)assignRecord,"assign_fun");
		dicValue=DOMTool.getAttributeValue((Element)assignRecord,"dict_type");
		lengthM=DOMTool.getAttributeValue((Element)assignRecord,"record_lengthM");
		lengthL=DOMTool.getAttributeValue((Element)assignRecord,"record_lengthL");
		beginTime=DOMTool.getAttributeValue((Element)assignRecord,"begin_time");
		if(beginTime!=null && beginTime.length()>0){
			timeBeg=new Timestamp(Long.parseLong(beginTime));
		}
		endTime=DOMTool.getAttributeValue((Element)assignRecord,"end_time");
		if(endTime!=null && endTime.length()>0){
			timeEnd=new Timestamp(Long.parseLong(endTime));
		}
		businessType=DOMTool.getAttributeValue((Element)assignRecord,"business_type");
		agentId=DOMTool.getAttributeValue((Element)assignRecord,"agent_id");
		String[] a =agentId.split(":");
		agentId = a[0];
		recordCount=DOMTool.getAttributeValue((Element)assignRecord,"record_count");


		sbSelect.append("select file_name,begin_time,id from  SU_QC_SOURCE_RECORD_DATA ");
		sbSelect.append("where 1=1 ");
		if(timeBeg!=null && timeEnd!=null){
			sbSelect.append(" and ");
			sbSelect.append(" begin_time ");
			sbSelect.append(" >= ");
			sbSelect.append(" '");
			sbSelect.append(f.format(timeBeg));
			sbSelect.append("' ");

			sbSelect.append(" and ");
			sbSelect.append(" end_time ");
			sbSelect.append(" <= ");
			sbSelect.append(" '");
			sbSelect.append(f.format(timeEnd));
			sbSelect.append("' ");
		}
		try {
			if(lengthM!=null && lengthM.length()>0 && Integer.parseInt(lengthM)>0){
				sbSelect.append(" and ");
				sbSelect.append(" record_length ");
				sbSelect.append(" >= ");
				sbSelect.append(lengthM);
			}

			if(lengthL!=null && lengthL.length()>0&& Integer.parseInt(lengthL)>0){
				sbSelect.append(" and ");
				sbSelect.append(" record_length ");
				sbSelect.append(" <= ");
				sbSelect.append(lengthL);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}


		if(businessType!=null && businessType.length()>0){
			sbSelect.append(" and ");
			sbSelect.append(" business_type ");
			sbSelect.append(" = ");
			sbSelect.append(" '");
			sbSelect.append(businessType);
			sbSelect.append("' ");

		}

		if(agentId!=null && agentId.length()>0){
			String agentIdIn=agentId.replaceAll(BaseInterface.MULTI_COMBO_BOX_SPLITOR, "','");
			sbSelect.append(" and ");
			sbSelect.append(" alias1 ");
			sbSelect.append(" in('");
			sbSelect.append(agentIdIn);
			sbSelect.append("') ");
//			agentIdIn="'"+agentIdIn+"'";
//			String[] strArr=agentId.split(BaseInterface.MULTI_COMBO_BOX_SPLITOR);
//			String strTmp=null;
//			for(int i=0;i<strArr.length;i++){
//				strTmp=strArr[i];
//
//
//			}
		}
		callBack.setCriteria("assingFun",assignFun);
//		callBack.assingFun=assignFun;
		if(recordCount!=null && recordCount.length()>0){
			if(Integer.parseInt(recordCount)>0){
				callBack.setCriteria("recordCount",recordCount);
			}else{
				callBack.setCriteria("recordCount",String.valueOf(BaseInterface.ZQC_DEFULT_RECORD_COUNT));
			}
		}/*else{

		}*/
		callBack.setCriteria("agents",nlAgents);

		callBack.setCriteria("beginTime", timeBeg);
		callBack.setCriteria("endTime", timeEnd);
		callBack.setCriteria("dicValue", dicValue);
		callBack.setCriteria("assignRecord", "fjassignRecord");


		if(assignFun.equals("最大通话时长分配")){
				sbSelect.append("  and  (score_state = '初检已评分'  or  score_state = '复检未评分' ) and data_state != '复检已分配'  order by record_length desc limit 0 ,"+recordCount);
		}else if(assignFun.equals("随机分配")){
				sbSelect.append("  and  (score_state = '初检已评分'  or  score_state = '复检未评分'  ) order by begin_time asc limit 0 ,"+recordCount);
		}else{
				sbSelect.append(" order by begin_time asc");
		}

		System.out.println("sql===="+sbSelect.toString());
		LogUtil.debug(sbSelect.toString(), SKIP_Logger);
		DAOTools.execSelectCallBackS(sbSelect.toString()/*,int colNum*/,"ZQC",callBack,BaseInterface.CALL_BACK_SQL_TYPE_STREAM,userName);

//		String id=DOMTool.getAttributeValue((Element)clientRoot, "id");
		return -1;
	}

	public static int assignRecordTxt(Document doc,String absPath,String assignRole,String userName) throws Exception{
		LogUtil.info("assignRecordTxt begin ", SKIP_Logger);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String assignFun=null,dicValue=null,lengthM=null,
		lengthL=null,beginTime=null,endTime=null,beginTimeTask=null,endTimeTask=null,businessType=null,agentId=null,
		recordCount=null;
		Timestamp timeBeg=null,timeEnd=null,timeBegTask=null,timeEndTask=null;

		CallBackInterface callBack =new CallBackAssign();


		StringBuffer sbSelect=new StringBuffer();
		Node clientRoot = DOMTool.getSingleNode(doc, "client");
		Node ZQCAgents = DOMTool.getSingleNode(clientRoot, "ZQCAgents");
		NodeList nlAgents = DOMTool.getMultiNodes(ZQCAgents, "agent");


		Node assignRecord= DOMTool.getSingleNode(clientRoot, "assignRecordTxt");

		assignFun=DOMTool.getAttributeValue((Element)assignRecord,"assign_fun");
		dicValue=DOMTool.getAttributeValue((Element)assignRecord,"dict_type");
		lengthM=DOMTool.getAttributeValue((Element)assignRecord,"record_lengthM");
		lengthL=DOMTool.getAttributeValue((Element)assignRecord,"record_lengthL");
		beginTime=DOMTool.getAttributeValue((Element)assignRecord,"begin_time");

		if(beginTime!=null && beginTime.length()>0){
			timeBeg=new Timestamp(Long.parseLong(beginTime));
		}
		endTime=DOMTool.getAttributeValue((Element)assignRecord,"end_time");
		if(endTime!=null && endTime.length()>0){
			timeEnd=new Timestamp(Long.parseLong(endTime));
		}
		beginTimeTask=DOMTool.getAttributeValue((Element)assignRecord,"begin_time_devoir");
		if(beginTimeTask!=null && beginTimeTask.length()>0){
			timeBegTask=new Timestamp(Long.parseLong(beginTimeTask));
		}
		endTimeTask=DOMTool.getAttributeValue((Element)assignRecord,"end_time_devoir");
		if(endTimeTask!=null && endTimeTask.length()>0){
			timeEndTask=new Timestamp(Long.parseLong(endTimeTask));
		}
		businessType=DOMTool.getAttributeValue((Element)assignRecord,"business_type");
		agentId=DOMTool.getAttributeValue((Element)assignRecord,"agent_id");
		if(agentId==""||agentId.equals("")){

		}else{
			String[] a =agentId.split(";");
			agentId = a[1];
		}

		recordCount=DOMTool.getAttributeValue((Element)assignRecord,"record_count");


		sbSelect.append("select file_name,begin_time,id from  SU_QC_SOURCE_RECORD_DATA ");
		sbSelect.append("where 1=1 ");
		if(timeBeg!=null && timeEnd!=null){
			sbSelect.append(" and ");
			sbSelect.append(" begin_time ");
			sbSelect.append(" >= ");
			sbSelect.append(" '");
			sbSelect.append(f.format(timeBeg));
			sbSelect.append("' ");

			sbSelect.append(" and ");
			sbSelect.append(" begin_time ");
			sbSelect.append(" <= ");
			sbSelect.append(" '");
			sbSelect.append(f.format(timeEnd));
			sbSelect.append("' ");
		}
		try {
			if(lengthM!=null && lengthM.length()>0 && Integer.parseInt(lengthM)>0){
				sbSelect.append(" and ");
				sbSelect.append(" record_length ");
				sbSelect.append(" >= ");
				sbSelect.append(lengthM);
			}

			if(lengthL!=null && lengthL.length()>0&& Integer.parseInt(lengthL)>0){
				sbSelect.append(" and ");
				sbSelect.append(" record_length ");
				sbSelect.append(" <= ");
				sbSelect.append(lengthL);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}


		if(businessType!=null && businessType.length()>0){
			sbSelect.append(" and ");
			sbSelect.append(" business_type ");
			sbSelect.append(" = ");
			sbSelect.append(" '");
			sbSelect.append(businessType);
			sbSelect.append("' ");
		}

		if(agentId!=null && agentId.length()>0){
			String agentIdIn=agentId.replaceAll(BaseInterface.MULTI_COMBO_BOX_SPLITOR, "','");
			sbSelect.append(" and ");
			sbSelect.append(" phone_number ");
			sbSelect.append(" in('");
			sbSelect.append(agentIdIn);
			sbSelect.append("') ");
//			agentIdIn="'"+agentIdIn+"'";
//			String[] strArr=agentId.split(BaseInterface.MULTI_COMBO_BOX_SPLITOR);
//			String strTmp=null;
//			for(int i=0;i<strArr.length;i++){
//				strTmp=strArr[i];
//
//
//			}
		}
		callBack.setCriteria("assingFun",assignFun);
//		callBack.assingFun=assignFun;
		if(recordCount!=null && recordCount.length()>0){
			if(Integer.parseInt(recordCount)>0){
				callBack.setCriteria("recordCount",recordCount);
			}else{
				callBack.setCriteria("recordCount",String.valueOf(BaseInterface.ZQC_DEFULT_RECORD_COUNT));
			}
		}/*else{

		}*/
		callBack.setCriteria("agents",nlAgents);

		callBack.setCriteria("beginTime", timeBeg);
		callBack.setCriteria("endTime", timeEnd);
		callBack.setCriteria("beginTimeTask", timeBegTask);
		callBack.setCriteria("endTimeTask", timeEndTask);
		callBack.setCriteria("dicValue", dicValue);
		callBack.setCriteria("assignRecord", "assignRecordTxt");
		callBack.setCriteria("assignRole", assignRole);


		if(assignFun.equals("最大通话时长分配")){
			//sbSelect.append("  or  data_state = '初检未分配'  or data_state = '0' or data_state = ''   order by record_length desc limit 0 ,"+recordCount);
			sbSelect.append("  and ( data_state = '初检未分配'  or data_state = '0' or data_state = '')   order by record_length desc limit 0 ,"+recordCount);
		}else if(assignFun.equals("随机分配")){
			sbSelect.append("  and ( data_state = '初检未分配'  or data_state = '0' or data_state = '')   order by begin_time asc limit 0 ,"+recordCount);
			//sbSelect.append("  or  data_state = '初检未分配'  or data_state = '0' or data_state = ''   order by begin_time asc limit 0 ,"+recordCount);
		}else{
				sbSelect.append(" order by begin_time asc");
		}

		System.out.println("sql===="+sbSelect.toString());
		LogUtil.debug(sbSelect.toString(), SKIP_Logger);
		DAOTools.execSelectCallBackS(sbSelect.toString()/*,int colNum*/,"ZQC",callBack,BaseInterface.CALL_BACK_SQL_TYPE_STREAM,userName);

//		String id=DOMTool.getAttributeValue((Element)clientRoot, "id");
		return -1;
	}

	public static int fjassignRecordTxt(Document doc,String absPath,String userName) throws Exception{
		LogUtil.info("fjassignRecordTxt begin ", SKIP_Logger);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String assignFun=null,dicValue=null,lengthM=null,
		lengthL=null,beginTime=null,endTime=null,businessType=null,agentId=null,
		recordCount=null;
		Timestamp timeBeg=null,timeEnd=null;

		CallBackInterface callBack =new CallBackAssign();


		StringBuffer sbSelect=new StringBuffer();
		Node clientRoot = DOMTool.getSingleNode(doc, "client");
		Node ZQCAgents = DOMTool.getSingleNode(clientRoot, "ZQCAgents");
		NodeList nlAgents = DOMTool.getMultiNodes(ZQCAgents, "agent");


		Node assignRecord= DOMTool.getSingleNode(clientRoot, "fjassignRecordTxt");

		assignFun=DOMTool.getAttributeValue((Element)assignRecord,"assign_fun");
		dicValue=DOMTool.getAttributeValue((Element)assignRecord,"dict_type");
		lengthM=DOMTool.getAttributeValue((Element)assignRecord,"record_lengthM");
		lengthL=DOMTool.getAttributeValue((Element)assignRecord,"record_lengthL");
		beginTime=DOMTool.getAttributeValue((Element)assignRecord,"begin_time");
		if(beginTime!=null && beginTime.length()>0){
			timeBeg=new Timestamp(Long.parseLong(beginTime));
		}
		endTime=DOMTool.getAttributeValue((Element)assignRecord,"end_time");
		if(endTime!=null && endTime.length()>0){
			timeEnd=new Timestamp(Long.parseLong(endTime));
		}
		businessType=DOMTool.getAttributeValue((Element)assignRecord,"business_type");
		agentId=DOMTool.getAttributeValue((Element)assignRecord,"agent_id");
		String[] a =agentId.split(";");
		agentId = a[1];
		recordCount=DOMTool.getAttributeValue((Element)assignRecord,"record_count");


		sbSelect.append("select file_name,begin_time,id from  SU_QC_SOURCE_RECORD_DATA ");
		sbSelect.append("where 1=1 ");
		if(timeBeg!=null && timeEnd!=null){
			sbSelect.append(" and ");
			sbSelect.append(" begin_time ");
			sbSelect.append(" >= ");
			sbSelect.append(" '");
			sbSelect.append(f.format(timeBeg));
			sbSelect.append("' ");

			sbSelect.append(" and ");
			sbSelect.append(" begin_time ");
			sbSelect.append(" <= ");
			sbSelect.append(" '");
			sbSelect.append(f.format(timeEnd));
			sbSelect.append("' ");
		}
		try {
			if(lengthM!=null && lengthM.length()>0 && Integer.parseInt(lengthM)>0){
				sbSelect.append(" and ");
				sbSelect.append(" record_length ");
				sbSelect.append(" >= ");
				sbSelect.append(lengthM);
			}

			if(lengthL!=null && lengthL.length()>0&& Integer.parseInt(lengthL)>0){
				sbSelect.append(" and ");
				sbSelect.append(" record_length ");
				sbSelect.append(" <= ");
				sbSelect.append(lengthL);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}


		if(businessType!=null && businessType.length()>0){
			sbSelect.append(" and ");
			sbSelect.append(" business_type ");
			sbSelect.append(" = ");
			sbSelect.append(" '");
			sbSelect.append(businessType);
			sbSelect.append("' ");
		}

		if(agentId!=null && agentId.length()>0){
			String agentIdIn=agentId.replaceAll(BaseInterface.MULTI_COMBO_BOX_SPLITOR, "','");
			sbSelect.append(" and ");
			sbSelect.append(" phone_number ");
			sbSelect.append(" in('");
			sbSelect.append(agentIdIn);
			sbSelect.append("') ");
//			agentIdIn="'"+agentIdIn+"'";
//			String[] strArr=agentId.split(BaseInterface.MULTI_COMBO_BOX_SPLITOR);
//			String strTmp=null;
//			for(int i=0;i<strArr.length;i++){
//				strTmp=strArr[i];
//
//
//			}
		}
		callBack.setCriteria("assingFun",assignFun);
//		callBack.assingFun=assignFun;
		if(recordCount!=null && recordCount.length()>0){
			if(Integer.parseInt(recordCount)>0){
				callBack.setCriteria("recordCount",recordCount);
			}else{
				callBack.setCriteria("recordCount",String.valueOf(BaseInterface.ZQC_DEFULT_RECORD_COUNT));
			}
		}/*else{

		}*/
		callBack.setCriteria("agents",nlAgents);

		callBack.setCriteria("beginTime", timeBeg);
		callBack.setCriteria("endTime", timeEnd);
		callBack.setCriteria("dicValue", dicValue);
		callBack.setCriteria("assignRecord", "fjassignRecordTxt");


		if(assignFun.equals("最大通话时长分配")){
				//sbSelect.append("  or  score_state = '初检已评分'  or  score_state = '复检未评分'  order by record_length desc limit 0 ,"+recordCount);
				sbSelect.append("  and  (score_state = '初检已评分'  or  score_state = '复检未评分' ) and data_state != '复检已分配'  order by record_length desc limit 0 ,"+recordCount);
		}else if(assignFun.equals("随机分配")){
				sbSelect.append("  and  (score_state = '初检已评分'  or  score_state = '复检未评分'  ) order by begin_time asc limit 0 ,"+recordCount);
				//sbSelect.append("  or  score_state = '初检已评分'  or  score_state = '复检未评分'   order by begin_time asc limit 0 ,"+recordCount);
		}else{
				sbSelect.append(" order by begin_time asc");
		}

		System.out.println("sql===="+sbSelect.toString());
		LogUtil.debug(sbSelect.toString(), SKIP_Logger);
		DAOTools.execSelectCallBackS(sbSelect.toString()/*,int colNum*/,"ZQC",callBack,BaseInterface.CALL_BACK_SQL_TYPE_STREAM,userName);

//		String id=DOMTool.getAttributeValue((Element)clientRoot, "id");
		return -1;
	}

	static class CallBackAssign implements CallBackInterface{

//		public CallBackAssign(){}
//		public static CallBackAssign getInstance(){
//			CallBackAssign ins=new CallBackAssign();
//			return ins;
//		}
		public void execute(CallBackParam param){
			String    assignRecord = null;
			if(begTime==null){
				assingFun=criteriaMap.get("assingFun").toString();
				assignRecord=criteriaMap.get("assignRecord").toString();
//				System.out.println("assignRecord==="+assignRecord);
				NodeList agents=(NodeList)criteriaMap.get("agents");
				recordCount=Integer.parseInt(String.valueOf(criteriaMap.get("recordCount")));

				String agentId=null;
				begTime=(Timestamp)criteriaMap.get("beginTime");
				endTime=(Timestamp)criteriaMap.get("endTime");

				recordPerHour = (recordCount / 24)*agents.getLength();

				// if(agentRecordList.size()==0){
				for (int i = 0; i < agents.getLength(); i++) {
					agentId = DOMTool.getAttributeValue((Element) agents.item(i), "agentId");
					// agentRecordList.put(agentId, al);
					agentList.add(agentId);
				}
				// }
				befHourLack=(recordCount % 24)*agents.getLength();
				agentPos=0;
			}
			assignRecord=criteriaMap.get("assignRecord").toString();
			Timestamp recBegTime=(Timestamp)param.nameValueMap.get("begin_time");
			Object recordName=param.nameValueMap.get("file_name");
			if(recordName!=null && recBegTime!=null){
				LogUtil.debug(assingFun.equals("小时日均分配")+" assingFun:"+assingFun+"befBeginTime : "+befBeginTime+" recBegTime : "+recBegTime+" 小时日均分配",SKIP_Logger);
				if(assingFun.equals("最大通话时长分配")){
					if(assignRecord.equals("fjassignRecord") || assignRecord.equals("fjassignRecordTxt")){
						fjassignRecordHourSJ(param);
						hourRecord.clear();
					}else if(assignRecord.equals("assignRecordTxt")){
						assignRecordHourSJ(param);
						hourRecord.clear();
					}else{
						assignRecordHourSJRecord(param);
						hourRecord.clear();
					}
				}else if(assingFun.equals("小时日均分配") || assingFun.equals("简单平均分配")) {
					if(befBeginTime==null){
						befBeginTime=new Timestamp(recBegTime.getTime());
					}else if(befBeginTime.getHours()!=recBegTime.getHours()){
						if(assignRecord.equals("assignRecordTxt")){
							assignRecordHourTask();
							hourRecord.clear();
						}else{
							assignRecordHour();
							hourRecord.clear();
						}
						assignRecordHour();
						hourRecord.clear();
					}
					hourRecord.add(String.valueOf(recordName));
					befBeginTime.setTime(recBegTime.getTime());
				}else if(assingFun.equals("随机分配")){
					String a = "";
					if(assignRecord.equals("fjassignRecord") || assignRecord.equals("fjassignRecordTxt")){
						fjassignRecordHourSJ(param);
						hourRecord.clear();
					}else if(assignRecord.equals("assignRecordTxt")){
						assignRecordHourSJ(param);
						hourRecord.clear();
					}else{
						assignRecordHourSJRecord(param);
						hourRecord.clear();
					}
				}else{
					LogUtil.debug("请选择分配方式", SKIP_Logger);
				}



			}
		}

		public void executeEnd(CallBackParam param){
			LogUtil.debug(sqlExec.size()+" sqlExec.size() ",SKIP_Logger);
			if(sqlExec.size()>0){
				DAOTools.execBatchS(sqlExec,"ZQC",false);
				sqlExec.clear();
			}
			LogUtil.debug(sqlExec.size()+" sqlExec.size() end ",SKIP_Logger);
			if(criteriaMap!=null){
				criteriaMap.clear();
				criteriaMap=null;
			}
			if(agentList!=null){
				agentList.clear();
				agentList=null;
			}
			if(hourRecord!=null){
				hourRecord.clear();
				hourRecord=null;
			}
			befBeginTime=null;
			begTime=null;
			endTime=null;
			recordPerHour=0;
			befHourLack=0;
			assingFun=null;
			agentPos=0;
		}

		public void assignRecordHourSJ(CallBackParam param){
			String sql = "";
			String agentId ="" ;

			agentId=agentList.get(agentPos++);
			sql="UPDATE SU_QC_SOURCE_RECORD_DATA set data_state='初检已分配',score_state='初检未评分' , " +
					"grade_name = '"+criteriaMap.get("dicValue").toString()+"',assign_role = '"+criteriaMap.get("assignRole").toString()+"',begin_time_devoir = '"+criteriaMap.get("beginTimeTask").toString()+"',end_time_devoir = '"+criteriaMap.get("endTimeTask").toString()+"',owner = '"+agentId+"' where file_name = '"+param.nameValueMap.get("file_name")+"'";
			sqlExec.add(sql);
			LogUtil.debug(sqlExec.size()+" sqlExec.size() ",SKIP_Logger);
			if(sqlExec.size()<3000){
				DAOTools.execBatchS(sqlExec,"ZQC",false);
				sqlExec.clear();
			}
			if(agentList.size() <= agentPos){
				agentPos=0;
			}
		}

		public void assignRecordHourSJRecord(CallBackParam param){
			String sql = "";
			String agentId ="" ;

			agentId=agentList.get(agentPos++);
			sql="UPDATE SU_QC_SOURCE_RECORD_DATA set data_state='初检已分配',score_state='初检未评分' , " +
					"grade_name = '"+criteriaMap.get("dicValue").toString()+"',owner = '"+agentId+"' where file_name = '"+param.nameValueMap.get("file_name")+"'";
			sqlExec.add(sql);
			LogUtil.debug(sqlExec.size()+" sqlExec.size() ",SKIP_Logger);
			if(sqlExec.size()<3000){
				DAOTools.execBatchS(sqlExec,"ZQC",false);
				sqlExec.clear();
			}
			if(agentList.size() <= agentPos){
				agentPos=0;
			}
		}

		public void fjassignRecordHourSJ(CallBackParam param){
			String sql = "";
			String agentId ="" ;

			agentId=agentList.get(agentPos++);
			sql="UPDATE SU_QC_SOURCE_RECORD_DATA set data_state='复检已分配',score_state='复检未评分' , " +
					"grade_name = '"+criteriaMap.get("dicValue").toString()+"',owner = '"+agentId+"' where file_name = '"+param.nameValueMap.get("file_name")+"'";
			sqlExec.add(sql);
			LogUtil.debug(sqlExec.size()+" sqlExec.size() ",SKIP_Logger);
			if(sqlExec.size()<3000){
				DAOTools.execBatchS(sqlExec,"ZQC",false);
				sqlExec.clear();
			}
			if(agentList.size() <= agentPos){
				agentPos=0;
			}
		}

		public void assignRecordHour(){

			int pos;
			/**
			 * 当前时段分配数量
			 */

			LogUtil.debug("befHourLack:"+befHourLack+" recordPerHour:"+recordPerHour+" hourRecord.size() :"+hourRecord.size(),SKIP_Logger);
			int nowHourAssignCount=hourRecord.size()>recordPerHour+befHourLack?recordPerHour+befHourLack:hourRecord.size();
			String agentId=null;
			String fileNameTmp=null;
			String sql=null;

			/**
			 * 前一时段少分配数量
			 */
			if(hourRecord.size()>recordPerHour+befHourLack){
				befHourLack=0;
			}else{
				befHourLack=recordPerHour+befHourLack-hourRecord.size();
			}

			if(assingFun.equals("最大通话时长分配")){

			}else if(assingFun.equals("小时日均分配") || assingFun.equals("简单平均分配")){
				for(int i=0;i<nowHourAssignCount;i++){
					pos=Utility.randIntScope(0,hourRecord.size()-1);
					fileNameTmp=hourRecord.remove(pos);
					agentId=agentList.get(agentPos);
					sql="UPDATE SU_QC_SOURCE_RECORD_DATA set data_state='初检已分配',score_state='初检未评分' , " +
							"grade_name = '"+criteriaMap.get("dicValue").toString()+"',owner = '"+agentId+"' where file_name = '"+fileNameTmp+"'";
					LogUtil.debug(nowHourAssignCount+" i:"+i+" agentPos :"+agentPos+" pos: "+pos+" sql:"+sql,SKIP_Logger);
					sqlExec.add(sql);
					agentPos=++agentPos<agentList.size()?agentPos:0;
				}
			}else if(assingFun.equals("随机分配")){

			}

			LogUtil.debug(sqlExec.size()+" sqlExec.size() ",SKIP_Logger);
			if(sqlExec.size()>3000){
				DAOTools.execBatchS(sqlExec,"ZQC",false);
				sqlExec.clear();
			}
		}

public void assignRecordHourTask(){

			int pos;
			/**
			 * 当前时段分配数量
			 */

			LogUtil.debug("befHourLack:"+befHourLack+" recordPerHour:"+recordPerHour+" hourRecord.size() :"+hourRecord.size(),SKIP_Logger);
			int nowHourAssignCount=hourRecord.size()>recordPerHour+befHourLack?recordPerHour+befHourLack:hourRecord.size();
			String agentId=null;
			String fileNameTmp=null;
			String sql=null;

			/**
			 * 前一时段少分配数量
			 */
			if(hourRecord.size()>recordPerHour+befHourLack){
				befHourLack=0;
			}else{
				befHourLack=recordPerHour+befHourLack-hourRecord.size();
			}

			if(assingFun.equals("最大通话时长分配")){

			}else if(assingFun.equals("小时日均分配") || assingFun.equals("简单平均分配")){
				for(int i=0;i<nowHourAssignCount;i++){
					pos= Utility.randIntScope(0,hourRecord.size()-1);
					fileNameTmp=hourRecord.remove(pos);
					agentId=agentList.get(agentPos);
					sql="UPDATE SU_QC_SOURCE_RECORD_DATA set data_state='初检已分配',score_state='初检未评分' , " +
							"grade_name = '"+criteriaMap.get("dicValue").toString()+"',begin_time_devoir = '"+criteriaMap.get("beginTimeTask").toString()+"',end_time_devoir = '"+criteriaMap.get("endTimeTask").toString()+"',owner = '"+agentId+"' where file_name = '"+fileNameTmp+"'";
					LogUtil.debug(nowHourAssignCount+" i:"+i+" agentPos :"+agentPos+" pos: "+pos+" sql:"+sql,SKIP_Logger);
					sqlExec.add(sql);
					agentPos=++agentPos<agentList.size()?agentPos:0;
				}
			}else if(assingFun.equals("随机分配")){

			}

			LogUtil.debug(sqlExec.size()+" sqlExec.size() ",SKIP_Logger);
			if(sqlExec.size()>3000){
				DAOTools.execBatchS(sqlExec,"ZQC",false);
				sqlExec.clear();
			}
		}

		int agentPos;

		HashMap<String, Object> criteriaMap=new HashMap<String, Object>();
		ArrayList<String> agentList=new ArrayList<String>();
//		HashMap<String, ArrayList<String>> agentRecordList=new HashMap<String, ArrayList<String>>();
//		ArrayList<String> recordList=new ArrayList<String>();
		Timestamp befBeginTime;
		Timestamp begTime;
		Timestamp endTime;
		int recordPerHour;
		int befHourLack;
		int agent;
		int recordCount;
		String assingFun;
		ArrayList<String> hourRecord=new ArrayList<String>();
		ArrayList<String> sqlExec=new ArrayList<String>();
//		int dayBetween;

//		public String assingFun;
//		public NodeList agents;
//		public int recordCount;
		public void setCriteria(String name, Object value) {
			// TODO Auto-generated method stub
			criteriaMap.put(name, value);
		}

	}


	/**
	 *
	 * 2010-6-28 下午03:52:08
	 * @param comboType
	 * @param comboParam
	 * @return
	 * Description & TODO
	 * @throws Exception
	 */
	public static String loadComboXml(String comboType,String comboParam) throws Exception{
//		LogUtil.warn(comboType+"---combo--"+comboParam, SKIP_Logger);
		String ret=null,sql=null,dbID=null;
		if(comboType.equals(BaseInterface.COMBO_TYPE_SKILL)){
			sql="select DISTINCT group_name from `DA_SGINFO`";
			dbID="z3000";
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_SKILL_AGENT)){
			dbID="z3000";
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT a.phone_number from `DA_SGINFO`a  ,SU_CALLCENTER_PHONE_USER b where a.phone_number=b.phone_number and group_name='"+comboParam+"'";
			}else{
				sql="select DISTINCT a.phone_number from `DA_SGINFO`a  ,SU_CALLCENTER_PHONE_USER b where a.phone_number=b.phone_number";
			}
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_PHY)){
			dbID="ZDesk";
			sql="select DISTINCT `alias5` from `suSecurityUser`  where `alias5`!=''";
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_ALL_COMBO)){
			dbID=ConfInfo.confMapDBConf.get("comboDBId");
			sql="select DISTINCT comboName from ComboDic";
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_SELECT_COMBO_VALUE)){
			dbID=ConfInfo.confMapDBConf.get("comboDBId");
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT name from ComboDic where comboName='"+comboParam+"' ";
			}else{
				sql="select DISTINCT name from ComboDic";
			}
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_ALL_GROUP)){
			if(comboParam!=null && comboParam.equals("物理组")){

			}else if(comboParam!=null && comboParam.equals("技能组")){
				dbID="z3000";
				sql="select DISTINCT group_name from `DA_SGINFO`";
			}else if(comboParam!=null && comboParam.equals("座席")){
				dbID="z3000";
				sql="select DISTINCT phone_number from `DA_SGINFO`";
			}else{
				StringBuffer retTmp=new StringBuffer();
				retTmp.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
				retTmp.append("<objects>");
				retTmp.append("</objects>");
				return retTmp.toString();
			}
		}else if(comboType.equals(BaseInterface.COMBO_QC_DICT)){
			dbID="ZQC";
			sql="select grade_name  from SA_QC_GRADE_DICTINFO where grade_oneticket_disable='可用'";

		}else if(comboType.equals(BaseInterface.QC_RECORD_ZJY)){
			dbID="ZQC";
			sql="select distinct owner  from SU_QC_SOURCE_RECORD_DATA ";
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXT_BUSINESS_TYPE)){
			//wenben
			dbID="ZQC";
			sql="select DISTINCT business_type from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXT_AGENT_CALL_NUMBER)){
			//wenben
			dbID="ZQC";
			sql="select DISTINCT call_number from SU_QC_SOURCE_RECORD_DATA order by call_number";
		}else if(comboType.equals(BaseInterface.COMBO_QC_BUSINESS_TYPE)){
			dbID="ZQC";
			sql="select DISTINCT business_type from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_RECORD_AGENT_LEVEL)){
			dbID="ZQC";
			sql="select DISTINCT agent_level from SU_QC_SOURCE_RECORD_DATA order by agent_level";
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXTE_LONGUEUR)){
			dbID="ZQC";
			sql="select DISTINCT texte_longueur from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_CLIENT_ATTENDRE_LENGTH)){
			dbID="ZQC";
			sql="select DISTINCT client_attendre_length from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_CLIENT_NAME)){
			dbID="ZQC";
			sql="select DISTINCT client_name from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXTE_QUARTIER)){
			dbID="ZQC";
			sql="select DISTINCT quartier from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXTE_CLIENT_SATISFY)){
			dbID="ZQC";
			sql="select DISTINCT client_satisfy from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXTE_IP_ADRESSE)){
			dbID="ZQC";
			sql="select DISTINCT ip_adresse from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXTE_REPETER_NUMERO)){
			dbID="ZQC";
			sql="select DISTINCT repeter_numero from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEACH_TYPE )){
			dbID="ZQC";
			sql="select DISTINCT TEACH_DIR_DESCRIPTION from T_TEACH_LIB_DIR";
		}else if(comboType.equals(BaseInterface.COMBO_QC_DATA_STATE )){
			dbID="ZQC";
			sql="select  distinct data_state from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_QC_SCORE_STATE)){
			dbID="ZQC";
			sql="select  distinct score_state from SU_QC_SOURCE_RECORD_DATA";
		}else if(comboType.equals(BaseInterface.COMBO_FJ_SCORE_STATE)){
			dbID="ZQC";
			sql="select  distinct score_state from SU_QC_SOURCE_RECORD_DATA where score_state='已评分'";
		}else if(comboType.equals(BaseInterface.COMBO_QC_ZHI_BIAO_STATE)){
			dbID="ZQC";
			sql="select  distinct grade_oneticket_disable from SA_QC_GRADE_DICTINFO ";
		}else if(comboType.equals(BaseInterface.COMBO_QC_ZHI_BIAO_YPFJ)){
			dbID="ZQC";
			sql="select  distinct grade_yipiaofoujue from SA_QC_GRADE_DICTINFO ";
		}else if(comboType.equals(BaseInterface.COMBO_QC_RECORD_AGENT_PHONE_NUMBER)){
			dbID="ZQC";
			sql="select  distinct phone_number from SU_QC_SOURCE_RECORD_DATA ";
		}else if(comboType.equals(BaseInterface.COMBO_QC_BUSINESS_TYPE_AGENT)){
			dbID="ZQC";
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT CONCAT(alias1,':', alias3) from SU_QC_SOURCE_RECORD_DATA where business_type='"+comboParam+"'";
			}else{
				sql="select DISTINCT CONCAT(alias1,':', alias3)  from SU_QC_SOURCE_RECORD_DATA ";
			}
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXT_BUSINESS_TYPE_AGENT)){
			//wenben
			dbID="ZQC";
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT phone_number from SU_QC_SOURCE_RECORD_DATA where business_type='"+comboParam+"'";
			}else{
				sql="select DISTINCT phone_number  from SU_QC_SOURCE_RECORD_DATA ";
			}
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXT_BUSINESS_TYPE_AGENT_NUMBER)){
			//wenben
			dbID="ZQC";
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT alias1 from SU_QC_SOURCE_RECORD_DATA where business_type='"+comboParam+"'";
			}else{
				sql="select DISTINCT alias1  from SU_QC_SOURCE_RECORD_DATA ";
			}
		}else if(comboType.equals(BaseInterface.COMBO_QC_TEXT_WORK_NUMBER)){
			//wenben
			dbID="ZQC";
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT alias1 from SU_QC_SOURCE_RECORD_DATA where business_type='"+comboParam+"' order by alias1";
			}else{
				sql="select DISTINCT alias1  from SU_QC_SOURCE_RECORD_DATA order by alias1";
			}
		}else if(comboType.equals(BaseInterface.COMBO_QC_PHYSIQUE_GROUP)){
			//wenben
			dbID="ZQC";

			sql="select DISTINCT alias2  from SU_QC_SOURCE_RECORD_DATA order by alias2";

		}else if(comboType.equals(BaseInterface.COMBO_QC_QUALITY_GROUP)){
			//wenben
			dbID="ZQC";

			sql="select DISTINCT alias4  from SU_QC_SOURCE_RECORD_DATA order by alias4";

		}else if(comboType.equals(BaseInterface.COMBO_QC_AGENT_LEVEL_ID)){
			dbID="ZQC";
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT CONCAT(agent_level,':', alias3) from SU_QC_SOURCE_RECORD_DATA where business_type='"+comboParam+"'";
			}else{
				sql="select DISTINCT CONCAT(agent_level,':', alias3)  from SU_QC_SOURCE_RECORD_DATA ";
			}
		}else if(comboType.equals(BaseInterface.COMBO_QC_AGENT_LEVEL_NUMBER)){
			dbID="ZQC";
			if(comboParam!=null && comboParam.length()>0){
				System.out.println("comboParam==="+comboParam);
				sql="select DISTINCT CONCAT(alias1,':', alias3 ) from SU_QC_SOURCE_RECORD_DATA where business_type='"+comboParam+"'";
			}else{
				sql="select DISTINCT  CONCAT(alias1,':', alias3 ) from SU_QC_SOURCE_RECORD_DATA";
			}
		}else if(comboType.equals(BaseInterface.COMBO_QC_AGENT_LEVEL_PHONE_NUMBER)){
			dbID="ZQC";
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT alias1 from SU_QC_SOURCE_RECORD_DATA where business_type='"+comboParam+"' ";
			}else{
				sql="select DISTINCT alias1 from SU_QC_SOURCE_RECORD_DATA";
			}
		}else if(comboType.equals(BaseInterface.COMBO_WE_CHAT_MENU_SIGN)){
			//for weixin
			dbID="ZDesk";
			sql="select DISTINCT name from JSONeditorSaver";
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_ALL_CALL_USER)){
			dbID=ConfInfo.confMapDBConf.get("securityDBId");
			sql=" SELECT DISTINCT loginName FROM suSecurityUser a,suSecurityRole b,suSecurityRoleMapPermission c WHERE " +
					" a.roleName=b.name AND b.name=c.roleName AND (c.name='文本座席' OR c.name='语音座席') ";
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_ALL_USERS)){
			dbID=ConfInfo.confMapDBConf.get("securityDBId");
			sql="select DISTINCT loginName from `suSecurityUser` ";
		}else if (comboType.equals(BaseInterface.COMBO_QC_ROLES)){
			dbID=ConfInfo.confMapDBConf.get("securityDBId");
			sql="select `name`  from suSecurityRole";
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_TYPE_RESOURCE)){
			dbID=ConfInfo.confMapDBConf.get("securityDBId");
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT modleId from suSecurityPermission where typeName='"+comboParam+"' ";
			}else{
				sql="select DISTINCT modleId from SU_QC_SOURCE_RECORD_DATA";
			}
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_ALL_FLOW_NAME)){
			dbID=ConfInfo.confMapDBConf.get("securityDBId");
			if(comboParam!=null && comboParam.length()>0){
				sql="select flowName from Z_FlowDetail where flowType='"+comboParam+"'";
			}else{
				sql="select flowName from Z_FlowDetail";
			}
		}else if(comboType.equals(BaseInterface.COMBO_TYPE_ALL_TEMPLATE_NAME)){
			dbID=ConfInfo.confMapDBConf.get("securityDBId");
			if(comboParam!=null && comboParam.length()>0){
				sql="select templateName from Z_MsgTemplate where templateType='"+comboParam+"'";
			}else{
				sql="select templateName from Z_MsgTemplate";
			}
		}else{
			dbID=ConfInfo.confMapDBConf.get("comboDBId");
			if(comboParam!=null && comboParam.length()>0){
				sql="select DISTINCT name from ComboDic where comboName='"+comboType+"' and parentComboValue='"+comboParam+"'";
			}else{
				sql="select DISTINCT name from ComboDic where comboName='"+comboType+"'";
			}
		}
		return DAOTools.queryNVXml(sql.toString(), dbID,true);
	}


}
