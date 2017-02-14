package com.zinglabs.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.AgentInfo;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.db.SkillGranularity;
import com.zinglabs.db.SkillInfo;
import com.zinglabs.db.SumGranularity;
import com.zinglabs.log.LogUtil;

public class AgentCheckThread extends BaseAbs implements Runnable {


	public AgentCheckThread(Timestamp calcDay) {
		init(SKIP_Logger);
//		this.ip=proerties.getProperty("db_ip");
//		this.port=Integer.parseInt(proerties.getProperty("RTAPort"));
//		z3000IP=proerties.getProperty("db_ip");
//		extIP=proerties.getProperty("db_ext_ip");
		this.initDay=new Timestamp(calcDay.getTime());
	}

	
	public void run() {
		long nowTime=System.currentTimeMillis();
		while (flag) {
			warn("Agent check Thread running");
			if( System.currentTimeMillis()-nowTime>=6000){
				syncAvailableAgentData();
				nowTime=System.currentTimeMillis();
			}
			try {
				Thread.sleep(30000L);
			} catch (InterruptedException e) {
				error("exception in RTASCOKECT run");
				error(e.toString());
			}
		}
	}
	
	
	public void syncAvailableAgentData(){
//		debug("-----------syncAvailableAgentData begin-----------");
//		总中心id
		HashMap<String, String> sgIdMap=ConfInfo.dynamicFields.get("sgIdMap");
		HashMap<String, String> sgIdName=ConfInfo.dynamicFields.get("sgNameMap");
		HashMap<String, String> swapSkillMap=ConfInfo.dynamicFields.get("swapSkillMap");
		
		StringBuffer whereTmp=new StringBuffer();
		if(ConfInfo.initSkipSkill!=null){
			for(int i=0;i<ConfInfo.initSkipSkill.length;i++){
				whereTmp.append(" and a.group_name !='");
				whereTmp.append(ConfInfo.initSkipSkill[i]);
				whereTmp.append("' ");
			}
		}
		
		if(ConfInfo.virtualSkill!=null){
			for(int i=0;i<ConfInfo.virtualSkill.length;i++){
				whereTmp.append(" and a.group_name !='");
				whereTmp.append(ConfInfo.virtualSkill[i]);
				whereTmp.append("' ");
			}
		}
		String sql="select DISTINCT  c.phone_number,c.person_name,a.group_name   from DA_SGINFO a,  " +
		"SU_CALLCENTER_PHONE_USER c ,SU_INIT_SKILLGROUP f   where  a.phone_number = c.phone_number  and " +
		"f.group_name= a.group_name "+whereTmp.toString()+" order by c.phone_number";
		String sqlLevel="select `agent_level` from `suSecurityUser`   where phone_number=";
		String sqlLevelTmp=null;
		ArrayList<Object[]> al=null;
		ArrayList<Object[]> al2=null;
		Object[] resTmp=null;
		Object[] resTmp2=null;
		AgentInfo agent=null;
		SkillInfo skill=null;
		ArrayList<String> sqlTmp=new ArrayList<String>();
		String extNum=null,skName=null,skId=null,skZH=null;
		SumGranularity sumTmp=null;
		int sumI=-1;
		StringBuffer sbTmp=new StringBuffer();
		ArrayList<Object[]> phyAl=null;
		al=DAOTools.execSelectS(sql, "z3000");
//		debug("al.size____ "+al.size());
//		debug(AgentInfo.SKIP_agentMap.size()+" _____");
		for(int i=0 ;i<al.size();i++){
			try{
				resTmp=al.get(i);
				if(resTmp[2]!=null && swapSkillMap!=null){
					debug("swapSkillMap.containsKey(resTmp[2]) "+swapSkillMap.containsKey(resTmp[2]));
					if(swapSkillMap.containsKey(resTmp[2]))continue;
				}
				if(AgentInfo.SKIP_agentMap.containsKey(resTmp[0])){
					agent=AgentInfo.SKIP_agentMap.get(resTmp[0]);
				}else{
					agent=new AgentInfo();
					agent.AgentInfo$Num=(String)resTmp[0];
					agent.AgentInfo$Name=(String)resTmp[1];	
					agent.init(initDay);
					
					
					sqlLevelTmp=sqlLevel+"'"+agent.AgentInfo$Num+"'";
					al2=DAOTools.execSelectS(sqlLevelTmp, "ZDesk");
					if(al2.size()>0){
						resTmp2=al2.get(0);
						agent.AgentInfo$AgentLevel=String.valueOf(resTmp2[0]);
					}else{
						agent.AgentInfo$AgentLevel="";
					}
				}
				
				agent.AgentInfo$SkillGroup.add((String)resTmp[2]);
//				if(resTmp[0].equals("6933"))
	debug(resTmp[0]+"-------AgentInfo$SkillGroup---"+agent.AgentInfo$SkillGroup.size()+"------"+resTmp[2]);		
				
				if(SkillInfo.SKIP_skillMap.containsKey(resTmp[2])){
					skill=SkillInfo.SKIP_skillMap.get(resTmp[2]);
				}else{
					skill=new SkillInfo();
					skill.SkillInfo$Name=(String)resTmp[2];
					skill.init(initDay);
				}
				
				
//				if(ConfInfo.procMode.equals("abcTest")){
					agent.AgentInfo$Alias1=agent.AgentInfo$Num;
					skill.SkillInfo$Alias4=skill.SkillInfo$Name;
					skill.SkillInfo$Alias6=sgIdMap.get(skill.SkillInfo$Alias4);
//				}
//				
				agent.agentSkill.add(skill);
				skill.skillAgent.put((String)resTmp[0], agent);
				
				SkillInfo.SKIP_allSkillName.add((String)resTmp[2]);
				SkillInfo.SKIP_skillMap.put((String)resTmp[2], skill);
				AgentInfo.SKIP_allPhone.add((String)resTmp[0]);
				AgentInfo.SKIP_agentMap.put((String)resTmp[0], agent);
				
			}catch (Exception e) {
				error("error in load  Agent Data exec sql: "+sql +" in z3000 pos :"+ i +" skip it");
				error(e);
			}
		}
		
//		for(Iterator ia=AgentInfo.SKIP_agentMap.entrySet().iterator();ia.hasNext();){
//			java.util.Map.Entry eAgent=(java.util.Map.Entry)ia.next();
//			agent=(AgentInfo)eAgent.getValue();
//			String phone=(String)eAgent.getKey();
//			
////			debug(phone+"----------------agent.AgentInfo$Num-----------------"+agent.AgentInfo$Num);
//		}
		
	
		
		
		if("abc".equals(ConfInfo.confMapDBConf.get("procMode"))){
			
			HashMap<String, String> phoneMap=new HashMap<String, String>();
			sql="select phone_number from suSecurityUser ";
			al=null;
			resTmp=null;
			al=DAOTools.execSelectS(sql, ConfInfo.confMapDBConf.get("securityDBId"));
			for(int i=0 ;i<al.size();i++){
				resTmp=al.get(i);
				phoneMap.put(String.valueOf(resTmp[0]), String.valueOf(resTmp[0]));
			}
			Iterator<String> ii=null;
//			<column name="AgentId" type="varchar" isPK="true" maxLength="25"  value="SKIP_$SKIP_$1111$"  baseCol="halfHour$agent$AgentInfo$Alias1"/>
//			<column name="PhyGrpCode" type="varchar" isPK="false" maxLength="40"  value=""  baseCol="halfHour$agent$AgentInfo$Alias2"/>
//			<column name="PhyGrpName" type="varchar" isPK="false" maxLength="100"  value=""  baseCol="halfHour$agent$AgentInfo$Alias3"/>
//			<column name="OldGroupId" type="varchar" isPK="false" maxLength="40"  value=""  baseCol="halfHour$agent$AgentInfo$Alias4"/>
//			<column name="OldGroupName" type="varchar" isPK="false" maxLength="100"  value=""  baseCol="halfHour$agent$AgentInfo$Alias5"/>
//			<column name="GroupId" type="varchar" isPK="false" maxLength="40"  value=""  baseCol="halfHour$agent$AgentInfo$Alias6"/>
//			<column name="GroupName" type="varchar" isPK="false" maxLength="100"  value=""  baseCol="halfHour$agent$AgentInfo$Alias7"/>
//			<column name="Departcode" type="varchar" isPK="false" maxLength="10"  value=""  baseCol="halfHour$agent$AgentInfo$Alias8"/>
//			<column name="Centercode" type="varchar" isPK="false" maxLength="10"  value=""  baseCol="halfHour$agent$AgentInfo$Alias9"/>
			
			
			
/*			<column name="Departcode" type="varchar" isPK="false" maxLength="10"  value=""  baseCol="halfHour$skill$SkilInfo$Alias8"/>
			<column name="OldGroupId" type="varchar" isPK="false" maxLength="40"  value=""  baseCol="halfHour$skill$SkilInfo$Alias4"/>
			<column name="OldGroupName" type="varchar" isPK="false" maxLength="100"  value=""  baseCol="halfHour$skill$SkilInfo$Alias5"/>
			<column name="GroupId" type="varchar" isPK="false" maxLength="40"  value="SKIP_$sgIdMap$SKIP_$"  baseCol="halfHour$skill$SkilInfo$Alias6"/>
			<column name="GroupName" type="varchar" isPK="false" maxLength="100"  value="SKIP_$sgNameMap$SKIP_$"  baseCol="halfHour$skill$SkilInfo$Alias7"/>
			<column name="Centercode" type="varchar" isPK="false" maxLength="10"  value=""  baseCol="halfHour$skill$SkilInfo$Alias9"/>*/
			sql = " select distinct  a.phonenum as phone_number,username ,a.userid ,b.skillgrpcode,c.skillgrpname,a.phygrpcode ,"
					+ "orgrpname , a.departcode ,  e.centercode from sys_user a,sys_usertoskillgrp b ,sys_skillgroup c ,sys_orgroup d ,centercode e where a.userid = "
					+ "b.userid and b.skillgrpcode != '' and a.phonenum !='' and a.available='0' and b.skillgrpcode=c.skillgrpcode "
					+ "and a.phygrpcode=d.orgrpcode  and e.departcode=a.departcode  order by phone_number";
			al = DAOTools.execSelectS(sql, "csrSyncDB");
			debug(sql+"------al.size()------------"+al.size());
			for (int i = 0; i < al.size(); i++) {
				try {
					resTmp = al.get(i);
					extNum = (String) resTmp[0];
					agent = AgentInfo.SKIP_agentMap.get(extNum);
//					debug(agent+"-------------"+extNum+"-----");
					logger.debug(agent+"-------------"+extNum+"-----");
					
					if (agent == null)
						continue;
					agent.AgentInfo$Name = (String) resTmp[1];
					agent.AgentInfo$Alias1 = (String) resTmp[2];
					agent.AgentInfo$Alias4 = (String) resTmp[3];
					agent.AgentInfo$Alias5 = (String) resTmp[4];
					agent.AgentInfo$Alias2 = (String) resTmp[5];
					agent.AgentInfo$Alias3 = (String) resTmp[6];
					agent.AgentInfo$Alias8 = (String) resTmp[7];
					agent.AgentInfo$Alias9 = (String)resTmp[8];
					
				
				
					if((extNum =="2041" || extNum.equals("2041"))||(extNum =="2681" || extNum.equals("2681"))|| (extNum =="2148" || extNum.equals("2148"))|| (extNum =="2030" || extNum.equals("2030"))|| (extNum =="3015" || extNum.equals("3015")) || (extNum =="2069" || extNum.equals("2069")) || (extNum =="2079" || extNum.equals("2079")) ||(extNum =="2554" || extNum.equals("2554"))|| (extNum =="2038" || extNum.equals("2038"))){
						agent.AgentInfo$roleNameAbc="质检员";
						agent.AgentInfo$organizationCodeAbc="org_00203";
					}else if((extNum =="2026" || extNum.equals("2026"))){
						agent.AgentInfo$roleNameAbc="质检主管";
						agent.AgentInfo$organizationCodeAbc="org_00203";
					}else{
						agent.AgentInfo$roleNameAbc="语音座席";
						agent.AgentInfo$organizationCodeAbc="org_00203";
					}
					
					
					
//					debug(" agent.AgentInfo$Name "+ (String) resTmp[1]+" agent.AgentInfo$Alias1 "+ (String) resTmp[2]+" agent.AgentInfo$Alias4 "+ (String) resTmp[3]+" agent.AgentInfo$Alias5 "+ (String) resTmp[4]+" agent.AgentInfo$Alias2 "+ (String) resTmp[5]+" agent.AgentInfo$Alias3 "+ (String) resTmp[6]+" agent.AgentInfo$Alias8 "+ (String) resTmp[7]+" agent.AgentInfo$Alias9 "+ (String)resTmp[8]);
					
					ii=agent.AgentInfo$SkillGroup.iterator();
					while(ii.hasNext()){
						skName=ii.next();
						agent.AgentInfo$Alias6=sgIdMap.get(skName);
						agent.AgentInfo$Alias7=sgIdName.get(skName);
						
						if((skill=SkillInfo.SKIP_skillMap.get(skName))==null)continue;
						skill.SkillInfo$Alias8=(String) resTmp[7];
						skill.SkillInfo$Alias4=(String) resTmp[3];
						skill.SkillInfo$Alias5 =(String) resTmp[4];
						skill.SkillInfo$Alias6 =sgIdMap.get(skName);
						skill.SkillInfo$Alias7 =sgIdName.get(skName);
						skill.SkillInfo$Alias9 =(String)resTmp[8];
						
						skill.skillSumId=skill.SkillInfo$Alias8+skill.SkillInfo$Alias9;
//debug(skName+"-------!!!----------"+sgIdMap.get(skName));

					}
//					debug(agent.AgentInfo$Alias6+"---"+agent.AgentInfo$Num+"--------AgentInfo$Alias7-------"+agent.AgentInfo$Alias4+"------"+skName+"------"+agent.AgentInfo$Alias7);
					
				} catch (Exception e) {
					error("error in sync  Agent Data from csrDB exec sql: "+ sql + " in csrSyncDB pos :" + i + " skip it");
					error(e);
				}
			}
			
//			Iterator iAgent=AgentInfo.SKIP_agentMap.keySet().iterator();
//			Iterator<String> iskName=null;
//			while (iAgent.hasNext()) {
//				extNum=iAgent.next().toString();
//				if ((agent = AgentInfo.SKIP_agentMap.get(extNum)) != null
//						&& (agent.AgentInfo$Alias1 == null || agent.AgentInfo$Alias1.length()==0)) {
//					iskName=agent.skillGroup.iterator();
//					while(iskName.hasNext()){
//						skName=iskName.next();
//		        		skill=SkillInfo.SKIP_skillMap.get(skName);
//		        		skill.skillAgent.remove(extNum);
//					}
//					AgentInfo.SKIP_agentMap.remove(agent);
//		        	AgentInfo.SKIP_allPhone.remove(extNum);
//		        	agent=null;
//				}
//			}
			for (Iterator iAgent = AgentInfo.SKIP_agentMap.entrySet().iterator(); iAgent.hasNext();) {
				try {
					java.util.Map.Entry eAgent = (java.util.Map.Entry) iAgent.next();
					agent = (AgentInfo) eAgent.getValue();
					extNum = (String) eAgent.getKey();
					if (agent != null&& (agent.AgentInfo$Alias1 == null
									|| agent.AgentInfo$Alias1.length() == 0
									|| agent.AgentInfo$Alias4 == null || agent.AgentInfo$Alias4
									.length() == 0)) {
						// debug(agent.AgentInfo$Alias1.length()+"
						// ############### agent info "+agent.AgentInfo$Alias1);
						for (Iterator<String> iskName = agent.AgentInfo$SkillGroup.iterator(); iskName.hasNext();) {
							skName = iskName.next();
							skill = SkillInfo.SKIP_skillMap.get(skName);
							skill.skillAgent.remove(extNum);
						}
						AgentInfo.SKIP_agentMap.remove(extNum);
						AgentInfo.SKIP_allPhone.remove(extNum);
						agent = null;
						continue;
					} else {
						// debug(agent.AgentInfo$Alias6+"---"+agent.AgentInfo$Num+"--------AgentInfo$Alias7-------"+agent.AgentInfo$Alias4+"------------"+agent.AgentInfo$Alias7);

						for (Iterator<String> iskName = agent.AgentInfo$SkillGroup.iterator(); iskName.hasNext();) {
							skName = iskName.next();
							skill = SkillInfo.SKIP_skillMap.get(skName);
							for (Iterator iSkill = skill.skillGranularity.entrySet().iterator(); iSkill.hasNext();) {
								java.util.Map.Entry eSkill = (java.util.Map.Entry) iSkill.next();
								SkillGranularity gTmp = (SkillGranularity) eSkill.getValue();
								int key = (Integer) eSkill.getKey();
								if (gTmp.skill$granularityBeginTime == null|| gTmp.skill$granularityEndTime == null)
									debug("skill " + skill.SkillInfo$Name+ "  end or begin is null  " + key);
							}
							if (skill.SkillInfo$Name == null || skill.SkillInfo$Name.length() == 0) {
								error("syncAvailableAgentData info skill null name ");
								continue;
							}
						}
					}

					sbTmp = new StringBuffer();
					if (phoneMap.containsKey(extNum)) {
		        	sbTmp.append("update suSecurityUser  set ");
					sbTmp.append(" alias1");
					sbTmp.append("=");
					sbTmp.append("'");
					sbTmp.append(agent.AgentInfo$Alias1);
					sbTmp.append("'");
					sbTmp.append(",");
					sbTmp.append(" alias2");
					sbTmp.append("=");
					sbTmp.append("'");
					sbTmp.append(agent.AgentInfo$Alias3);
					sbTmp.append("'");
					sbTmp.append(",");
					
					sbTmp.append(" alias3");
					sbTmp.append("=");
					sbTmp.append("'");
					sbTmp.append(agent.AgentInfo$Alias7);
					sbTmp.append("'");
					sbTmp.append(",");
					
//					Departcode
					sbTmp.append(" alias4");
					sbTmp.append("=");
					sbTmp.append("'");
					sbTmp.append(agent.AgentInfo$Alias8);
					sbTmp.append("'");
					sbTmp.append(",");
					
					sbTmp.append(" alias5");
					sbTmp.append("=");
					sbTmp.append("'");
					sbTmp.append(agent.AgentInfo$Alias9);
					sbTmp.append("'");
					sbTmp.append(",");
					
					sbTmp.append(" roleName");
					sbTmp.append("=");
					sbTmp.append("'");
					sbTmp.append(agent.AgentInfo$roleNameAbc);
					sbTmp.append("'");
					sbTmp.append(",");
					
					sbTmp.append(" organizationCode");
					sbTmp.append("=");
					sbTmp.append("'");
					sbTmp.append(agent.AgentInfo$organizationCodeAbc);
					sbTmp.append("'");
					sbTmp.append(",");
					
					sbTmp.append("name");
					sbTmp.append("=");
					sbTmp.append("'");
					sbTmp.append(agent.AgentInfo$Name);
					sbTmp.append("'");
					
					sbTmp.append(" where ");
					sbTmp.append(" phone_number ");
					sbTmp.append("=");
					sbTmp.append("'");
					sbTmp.append(extNum);
					sbTmp.append("'");
					sqlTmp.add(sbTmp.toString());
			        } else {
						sbTmp.append("insert into suSecurityUser ");
						sbTmp.append("(loginName,pwd,name,phone_number,"
								+ "alias1,alias2,alias3,alias4,alias5,roleName,organizationCode)");
						sbTmp.append("values (");
						sbTmp.append("'");
//						sbTmp.append(extNum);
						sbTmp.append(agent.AgentInfo$Alias1);
						sbTmp.append("'");
						sbTmp.append(",");
						sbTmp.append("'");
						sbTmp.append("000000");
						sbTmp.append("'");
						sbTmp.append(",");
						sbTmp.append("'");
						sbTmp.append(agent.AgentInfo$Alias1);
						sbTmp.append("'");
						sbTmp.append(",");
						sbTmp.append("'");
						sbTmp.append(extNum);
						sbTmp.append("'");
						sbTmp.append(",");
						sbTmp.append("'");
						sbTmp.append(agent.AgentInfo$Alias1);
						sbTmp.append("'");
						sbTmp.append(",");
						sbTmp.append("'");
						sbTmp.append(agent.AgentInfo$Alias3);
						sbTmp.append("'");
						sbTmp.append(",");
						sbTmp.append("'");
						sbTmp.append(agent.AgentInfo$Alias7);
						sbTmp.append("'");
						sbTmp.append(",");
						sbTmp.append("'");
						sbTmp.append(agent.AgentInfo$Alias8);
						sbTmp.append("'");
						sbTmp.append(",");
						sbTmp.append("'");
						sbTmp.append(agent.AgentInfo$Alias9);
						sbTmp.append("'");
						sbTmp.append(",");
						
						sbTmp.append("'");
						sbTmp.append(agent.AgentInfo$roleNameAbc);
						sbTmp.append("'");
						sbTmp.append(",");
					
						sbTmp.append("'");
						sbTmp.append(agent.AgentInfo$organizationCodeAbc);
						sbTmp.append("'");
						sbTmp.append(")");
						sqlTmp.add(sbTmp.toString());
					}
		        
		       
				}catch (Exception e) {
					error("error in after sync check Agent Data agent is "+extNum+" skip it");
					error(e);
				}
			}
			for(Iterator iS = SkillInfo.SKIP_skillMap.entrySet().iterator(); iS.hasNext();) {
		        java.util.Map.Entry eS = (java.util.Map.Entry)iS.next();
		        skill =(SkillInfo)eS.getValue();
		        skName=(String)eS.getKey();
		        debug(skill.skillSumId+"-------------"+skName);
		        if(skill.SkillInfo$Alias4==null || skill.SkillInfo$Alias4.length()==0){
		        	SkillInfo.SKIP_skillMap.remove(skName);
		        	SkillInfo.SKIP_allSkillName.remove(skName);
		        	skill.aggregaeMap=null;
		        	skill=null;
		        }
			}
			
			DAOTools.execBatchS(sqlTmp, ConfInfo.confMapDBConf.get("securityDBId"),false);
			
			String phyName=null;
			
			sqlTmp.clear();
			sql="select DISTINCT  `alias2` from `suSecurityUser` ";
			phyAl=DAOTools.execSelectS(sql, ConfInfo.confMapDBConf.get("securityDBId"));
			for(int i=0;i<phyAl.size();i++){
				resTmp=phyAl.get(i);
				if(resTmp[0]!=null ){
					phyName=resTmp[0].toString();
					if(phyName.length()>0){
						sbTmp = new StringBuffer();
						sbTmp.append("insert ignore into ZS_PhysicalUse ");
						sbTmp.append(" (phyName,inUse) values ('"+phyName+"','否')");
						sqlTmp.add(sbTmp.toString());
					}
				}
			}
			DAOTools.execBatchS(sqlTmp, ConfInfo.confMapDBConf.get("securityDBId"),false);
			
			
//			for(Iterator iS = sgIdMap.entrySet().iterator(); iS.hasNext();) {
//				 java.util.Map.Entry eS = (java.util.Map.Entry)iS.next();
//			     skName=(String)eS.getKey();
//			     skId=(String)eS.getValue();
//			     if(!SkillInfo.SKIP_skillMap.containsKey(skName)){
//			    	 sql="select skillgrpname,skillgrpcode,a.departcode,centercode from sys_skillgroup a,centercode b where a.departcode=b.departcode and skillgrpcode like '"+skId+"%'";
//			    	 al = dao.execSelect(sql, "csrSyncDB");
//						for (int i = 0; i < al.size(); i++) {
//							try {
//								resTmp = al.get(i);
//								
//								skill=new SkillInfo();
//								skill.SkillInfo$Name=skName;
//								skill.init(initDay);
//								skill.SkillInfo$Alias4=(String) resTmp[1];
//								skill.SkillInfo$Alias5 =(String) resTmp[0];
//								skill.SkillInfo$Alias8=(String) resTmp[2];
//								skill.SkillInfo$Alias9 =(String)resTmp[3];
//								skill.SkillInfo$Alias6 =sgIdMap.get(skName);
//								skill.SkillInfo$Alias7 =sgIdName.get(skName);
//								
//								skill.skillSumId=skill.SkillInfo$Alias8+skill.SkillInfo$Alias9;
//								
//								debug(skill.SkillInfo$Alias4+"###"+skName+"########"+skill.SkillInfo$Alias5+"#######"+skill.skillSumId);
//								SkillInfo.SKIP_allSkillName.add(skName);
//								SkillInfo.SKIP_skillMap.put(skName, skill);
//							} catch (Exception e) {
//								error("refetch skill Data from csrDB exec sql: "+ sql + " in csrSyncDB pos :" + i + " skip it");
//								error(e);
//							}
//						}
//			     }
//			}
			
//			
//			for(Iterator iAgent = AgentInfo.SKIP_agentMap.entrySet().iterator(); iAgent.hasNext();) {
//		        java.util.Map.Entry eAgent = (java.util.Map.Entry)iAgent.next();
//		        agent =(AgentInfo)eAgent.getValue();
//		        extNum=(String)eAgent.getKey();
//		        if(agent!=null && (agent.AgentInfo$Alias1==null || agent.AgentInfo$Alias1.length()==0)){
//debug(agent.AgentInfo$Num+" ###############  agent info "+extNum);
//		        }
//			}		        
			
		}else if("common".equals(ConfInfo.confMapDBConf.get("procMode")) ||"abcTest".equals(ConfInfo.confMapDBConf.get("procMode"))  ){
			for(Iterator iAgent = AgentInfo.SKIP_agentMap.entrySet().iterator(); iAgent.hasNext();) {
				try{
		        java.util.Map.Entry eAgent = (java.util.Map.Entry)iAgent.next();
		        agent =(AgentInfo)eAgent.getValue();
		        extNum=(String)eAgent.getKey();
		        if(agent!=null && (agent.AgentInfo$Num==null || agent.AgentInfo$Num.length()==0)){
//debug(agent.AgentInfo$Alias1.length()+" ###############  agent info "+agent.AgentInfo$Alias1);
		        	for(Iterator<String> iskName=agent.AgentInfo$SkillGroup.iterator();iskName.hasNext();){
		        		skName=iskName.next();
		        		skill=SkillInfo.SKIP_skillMap.get(skName);
		        		skill.skillAgent.remove(extNum);
		        	}
		        	AgentInfo.SKIP_agentMap.remove(extNum);
		        	AgentInfo.SKIP_allPhone.remove(extNum);
		        	agent=null;
		        }else{
//		        	debug(agent.AgentInfo$Alias6+"---"+agent.AgentInfo$Num+"--------AgentInfo$Alias7-------"+agent.AgentInfo$Alias4+"------------"+agent.AgentInfo$Alias7);
		        	for(Iterator<String> iskName=agent.AgentInfo$SkillGroup.iterator();iskName.hasNext();){
		        		skName=iskName.next();
		        		
//		        		if(extNum.equals("6933"))
//		        			debug(extNum+"-------AgentInfo$SkillGroup----"+agent.AgentInfo$SkillGroup.size()+"-----"+skName);
//		        		
		        		skill=SkillInfo.SKIP_skillMap.get(skName);
		        		if(skill.SkillInfo$Name==null || skill.SkillInfo$Name.length()==0){
		        			error("syncAvailableAgentData info skill null name ");
							continue;
		        		}else{
		        			skill.skillSumId="0";  // defult keep all sum data in id 0
		        		}
		        	}
		        }
				}catch (Exception e) {
					error("error in after sync check Agent Data agent is "+extNum+" skip it");
					error(e);
				}
			}
			for(Iterator iS = SkillInfo.SKIP_skillMap.entrySet().iterator(); iS.hasNext();) {
		        java.util.Map.Entry eS = (java.util.Map.Entry)iS.next();
		        skill =(SkillInfo)eS.getValue();
		        skName=(String)eS.getKey();
		        if(skill.SkillInfo$Name==null || skill.SkillInfo$Name.length()==0){
		        	SkillInfo.SKIP_skillMap.remove(skName);
		        	SkillInfo.SKIP_allSkillName.remove(skName);
		        	skill.aggregaeMap=null;
		        	skill=null;
		        }else{
		        	skill.skillSumId="0";
		        }
			}
		}
	}
	
	
//	public void syncAvailableAgentData(){
////		debug("-----------syncAvailableAgentData begin-----------");
////		总中心id
//		HashMap<String, String> sgIdMap=ConfInfo.dynamicFields.get("sgIdMap");
//		HashMap<String, String> sgIdName=ConfInfo.dynamicFields.get("sgNameMap");
//		HashMap<String, String> swapSkillMap=ConfInfo.dynamicFields.get("swapSkillMap");
//		
//		StringBuffer whereTmp=new StringBuffer();
//		if(ConfInfo.initSkipSkill!=null){
//			for(int i=0;i<ConfInfo.initSkipSkill.length;i++){
//				whereTmp.append(" and a.group_name !='");
//				whereTmp.append(ConfInfo.initSkipSkill[i]);
//				whereTmp.append("' ");
//			}
//		}
//		
//		if(ConfInfo.virtualSkill!=null){
//			for(int i=0;i<ConfInfo.virtualSkill.length;i++){
//				whereTmp.append(" and a.group_name !='");
//				whereTmp.append(ConfInfo.virtualSkill[i]);
//				whereTmp.append("' ");
//			}
//		}
//		String sql="select DISTINCT  c.phone_number,c.person_name,a.group_name   from DA_SGINFO a,  " +
//		"SU_CALLCENTER_PHONE_USER c ,SU_INIT_SKILLGROUP f   where  a.phone_number = c.phone_number  and " +
//		"f.group_name= a.group_name "+whereTmp.toString()+" order by c.phone_number";
//		ArrayList<Object[]> al=null;
//		Object[] resTmp=null;
//		AgentInfo agent=null;
//		SkillInfo skill=null;
//		SumInfo sum=null;
//		ArrayList<String> sqlTmp=new ArrayList<String>();
//		String extNum=null,skName=null,skId=null,skZH=null;
//		SumGranularity sumTmp=null;
//		int sumI=-1;
//		StringBuffer sbTmp=new StringBuffer();
//		
//		al=DAOTools.execSelectS(sql, "z3000");
////		debug("al.size____ "+al.size());
////		debug(AgentInfo.SKIP_agentMap.size()+" _____");
//		for(int i=0 ;i<al.size();i++){
//			try{
//				resTmp=al.get(i);
//				if(resTmp[2]!=null && swapSkillMap!=null){
//					if(swapSkillMap.containsKey(resTmp[2]))continue;
//				}
//				if(AgentInfo.SKIP_agentMap.containsKey(resTmp[0])){
//					agent=AgentInfo.SKIP_agentMap.get(resTmp[0]);
//				}else{
//					agent=new AgentInfo();
//					agent.AgentInfo$Num=(String)resTmp[0];
//					agent.AgentInfo$Name=(String)resTmp[1];	
//					agent.init(initDay);
//					
//				}
//				
//				agent.AgentInfo$SkillGroup.add((String)resTmp[2]);
////				if(resTmp[0].equals("6933"))
////	debug(resTmp[0]+"-------AgentInfo$SkillGroup---"+agent.AgentInfo$SkillGroup.size()+"------"+resTmp[2]);		
//				
//				if(SkillInfo.SKIP_skillMap.containsKey(resTmp[2])){
//					skill=SkillInfo.SKIP_skillMap.get(resTmp[2]);
//				}else{
//					skill=new SkillInfo();
//					skill.SkillInfo$Name=(String)resTmp[2];
//					skill.init(initDay);
//				}
//				
//				
//				if(ConfInfo.procMode.equals("abcTest")){
//					agent.AgentInfo$Alias1=agent.AgentInfo$Num;
//					skill.SkillInfo$Alias4=skill.SkillInfo$Name;
//					skill.SkillInfo$Alias6=sgIdMap.get(skill.SkillInfo$Alias4);
//				}
////				
//				agent.agentSkill.add(skill);
//				skill.skillAgent.put((String)resTmp[0], agent);
//				
//				SkillInfo.SKIP_allSkillName.add((String)resTmp[2]);
//				SkillInfo.SKIP_skillMap.put((String)resTmp[2], skill);
//				AgentInfo.SKIP_allPhone.add((String)resTmp[0]);
//				AgentInfo.SKIP_agentMap.put((String)resTmp[0], agent);
//				
//			}catch (Exception e) {
//				error("error in load  Agent Data exec sql: "+sql +" in z3000 pos :"+ i +" skip it");
//				error(e);
//			}
//		}
//		
////		for(Iterator ia=AgentInfo.SKIP_agentMap.entrySet().iterator();ia.hasNext();){
////			java.util.Map.Entry eAgent=(java.util.Map.Entry)ia.next();
////			agent=(AgentInfo)eAgent.getValue();
////			String phone=(String)eAgent.getKey();
////			
//////			debug(phone+"----------------agent.AgentInfo$Num-----------------"+agent.AgentInfo$Num);
////		}
//		
//		if(ConfInfo.procMode.equals("abc")){
//			
//			HashMap<String, String> phoneMap=new HashMap<String, String>();
//			sql="select phone_number from suSecurityUser ";
//			al=null;
//			resTmp=null;
//			al=DAOTools.execSelectS(sql, ConfInfo.securityDBId);
//			for(int i=0 ;i<al.size();i++){
//				resTmp=al.get(i);
//				phoneMap.put(String.valueOf(resTmp[0]), String.valueOf(resTmp[0]));
//			}
//			
//			
//			Iterator<String> ii=null;
////			<column name="AgentId" type="varchar" isPK="true" maxLength="25"  value="SKIP_$SKIP_$1111$"  baseCol="halfHour$agent$AgentInfo$Alias1"/>
////			<column name="PhyGrpCode" type="varchar" isPK="false" maxLength="40"  value=""  baseCol="halfHour$agent$AgentInfo$Alias2"/>
////			<column name="PhyGrpName" type="varchar" isPK="false" maxLength="100"  value=""  baseCol="halfHour$agent$AgentInfo$Alias3"/>
////			<column name="OldGroupId" type="varchar" isPK="false" maxLength="40"  value=""  baseCol="halfHour$agent$AgentInfo$Alias4"/>
////			<column name="OldGroupName" type="varchar" isPK="false" maxLength="100"  value=""  baseCol="halfHour$agent$AgentInfo$Alias5"/>
////			<column name="GroupId" type="varchar" isPK="false" maxLength="40"  value=""  baseCol="halfHour$agent$AgentInfo$Alias6"/>
////			<column name="GroupName" type="varchar" isPK="false" maxLength="100"  value=""  baseCol="halfHour$agent$AgentInfo$Alias7"/>
////			<column name="Departcode" type="varchar" isPK="false" maxLength="10"  value=""  baseCol="halfHour$agent$AgentInfo$Alias8"/>
////			<column name="Centercode" type="varchar" isPK="false" maxLength="10"  value=""  baseCol="halfHour$agent$AgentInfo$Alias9"/>
//			
//			
//			
///*			<column name="Departcode" type="varchar" isPK="false" maxLength="10"  value=""  baseCol="halfHour$skill$SkilInfo$Alias8"/>
//			<column name="OldGroupId" type="varchar" isPK="false" maxLength="40"  value=""  baseCol="halfHour$skill$SkilInfo$Alias4"/>
//			<column name="OldGroupName" type="varchar" isPK="false" maxLength="100"  value=""  baseCol="halfHour$skill$SkilInfo$Alias5"/>
//			<column name="GroupId" type="varchar" isPK="false" maxLength="40"  value="SKIP_$sgIdMap$SKIP_$"  baseCol="halfHour$skill$SkilInfo$Alias6"/>
//			<column name="GroupName" type="varchar" isPK="false" maxLength="100"  value="SKIP_$sgNameMap$SKIP_$"  baseCol="halfHour$skill$SkilInfo$Alias7"/>
//			<column name="Centercode" type="varchar" isPK="false" maxLength="10"  value=""  baseCol="halfHour$skill$SkilInfo$Alias9"/>*/
//			sql = " select distinct  a.phonenum as phone_number,username ,a.userid ,b.skillgrpcode,c.skillgrpname,a.phygrpcode ,"
//					+ "orgrpname , a.departcode ,  e.centercode from sys_user a,sys_usertoskillgrp b ,sys_skillgroup c ,sys_orgroup d ,centercode e where a.userid = "
//					+ "b.userid and b.skillgrpcode != '' and a.phonenum !='' and a.available='0' and b.skillgrpcode=c.skillgrpcode "
//					+ "and a.phygrpcode=d.orgrpcode  and e.departcode=a.departcode  order by phone_number";
//			al = DAOTools.execSelectS(sql, "csrSyncDB");
//			debug(sql+"------al.size()------------"+al.size());
//			for (int i = 0; i < al.size(); i++) {
//				try {
//					resTmp = al.get(i);
//					extNum = (String) resTmp[0];
//					agent = AgentInfo.SKIP_agentMap.get(extNum);
////					debug(agent+"-------------"+extNum+"-----");
//					
//					if (agent == null)
//						continue;
//					agent.AgentInfo$Name = (String) resTmp[1];
//					agent.AgentInfo$Alias1 = (String) resTmp[2];
//					agent.AgentInfo$Alias4 = (String) resTmp[3];
//					agent.AgentInfo$Alias5 = (String) resTmp[4];
//					agent.AgentInfo$Alias2 = (String) resTmp[5];
//					agent.AgentInfo$Alias3 = (String) resTmp[6];
//					agent.AgentInfo$Alias8 = (String) resTmp[7];
//					agent.AgentInfo$Alias9 = (String)resTmp[8];
//					
////					debug(" agent.AgentInfo$Name "+ (String) resTmp[1]+" agent.AgentInfo$Alias1 "+ (String) resTmp[2]+" agent.AgentInfo$Alias4 "+ (String) resTmp[3]+" agent.AgentInfo$Alias5 "+ (String) resTmp[4]+" agent.AgentInfo$Alias2 "+ (String) resTmp[5]+" agent.AgentInfo$Alias3 "+ (String) resTmp[6]+" agent.AgentInfo$Alias8 "+ (String) resTmp[7]+" agent.AgentInfo$Alias9 "+ (String)resTmp[8]);
//					
//					ii=agent.AgentInfo$SkillGroup.iterator();
//					while(ii.hasNext()){
//						skName=ii.next();
//						agent.AgentInfo$Alias6=sgIdMap.get(skName);
//						agent.AgentInfo$Alias7=sgIdName.get(skName);
//						
//						if((skill=SkillInfo.SKIP_skillMap.get(skName))==null)continue;
//						skill.SkillInfo$Alias8=(String) resTmp[7];
//						skill.SkillInfo$Alias4=(String) resTmp[3];
//						skill.SkillInfo$Alias5 =(String) resTmp[4];
//						skill.SkillInfo$Alias6 =sgIdMap.get(skName);
//						skill.SkillInfo$Alias7 =sgIdName.get(skName);
//						skill.SkillInfo$Alias9 =(String)resTmp[8];
//						
//						skill.skillSumId=skill.SkillInfo$Alias8+skill.SkillInfo$Alias9;
////debug(skName+"-------!!!----------"+sgIdMap.get(skName));
//						
//						
//						if(SumInfo.SKIP_sumMap.containsKey(skill.skillSumId)){
//							sum=SumInfo.SKIP_sumMap.get(skill.skillSumId);
//						}else{
//							sum=new SumInfo();
//							sum.SumInfo$Alias8=skill.SkillInfo$Alias8;
//							sum.SumInfo$Alias9=skill.SkillInfo$Alias9;
//							sum.init(initDay);
//						}
//						
//						for(Iterator iS = sum.sumGranularity.entrySet().iterator(); iS.hasNext();){
//							 java.util.Map.Entry eS = (java.util.Map.Entry)iS.next();
//							 sumTmp =(SumGranularity)eS.getValue();
//							 sumTmp.sumId=skill.SkillInfo$Alias8+skill.SkillInfo$Alias9;
////							 sumTmp.sumTmp=skill.SkillInfo$Name;
//							 sumI=(Integer)eS.getKey();
////debug(skill.SkillInfo$Name+"------sum$map$skillGranularity------------"+sumI);
//							 if(!sumTmp.sum$map$skillGranularity.containsKey(skill.SkillInfo$Name)){
//								 sumTmp.sum$map$skillGranularity.put(skill.SkillInfo$Name, skill.skillGranularity.get(sumI));
//							 }
//						}
//						SumInfo.SKIP_sumMap.put(skill.skillSumId, sum);
//					}
////					debug(agent.AgentInfo$Alias6+"---"+agent.AgentInfo$Num+"--------AgentInfo$Alias7-------"+agent.AgentInfo$Alias4+"------"+skName+"------"+agent.AgentInfo$Alias7);
//					
//				} catch (Exception e) {
//					error("error in sync  Agent Data from csrDB exec sql: "+ sql + " in csrSyncDB pos :" + i + " skip it");
//					error(e);
//				}
//			}
//			
////			Iterator iAgent=AgentInfo.SKIP_agentMap.keySet().iterator();
////			Iterator<String> iskName=null;
////			while (iAgent.hasNext()) {
////				extNum=iAgent.next().toString();
////				if ((agent = AgentInfo.SKIP_agentMap.get(extNum)) != null
////						&& (agent.AgentInfo$Alias1 == null || agent.AgentInfo$Alias1.length()==0)) {
////					iskName=agent.skillGroup.iterator();
////					while(iskName.hasNext()){
////						skName=iskName.next();
////		        		skill=SkillInfo.SKIP_skillMap.get(skName);
////		        		skill.skillAgent.remove(extNum);
////					}
////					AgentInfo.SKIP_agentMap.remove(agent);
////		        	AgentInfo.SKIP_allPhone.remove(extNum);
////		        	agent=null;
////				}
////			}
//			for(Iterator iAgent = AgentInfo.SKIP_agentMap.entrySet().iterator(); iAgent.hasNext();) {
//				try{
//		        java.util.Map.Entry eAgent = (java.util.Map.Entry)iAgent.next();
//		        agent =(AgentInfo)eAgent.getValue();
//		        extNum=(String)eAgent.getKey();
//		        sbTmp=new StringBuffer();
//		        if(phoneMap.containsKey(extNum)){
//		        	sbTmp.append("update suSecurityUser  set ");
//					sbTmp.append(" alias1");
//					sbTmp.append("=");
//					sbTmp.append("'");
//					sbTmp.append(agent.AgentInfo$Alias1);
//					sbTmp.append("'");
//					sbTmp.append(",");
//					sbTmp.append(" alias2");
//					sbTmp.append("=");
//					sbTmp.append("'");
//					sbTmp.append(agent.AgentInfo$Alias3);
//					sbTmp.append("'");
//					sbTmp.append(",");
//					
//					sbTmp.append(" alias3");
//					sbTmp.append("=");
//					sbTmp.append("'");
//					sbTmp.append(agent.AgentInfo$Alias7);
//					sbTmp.append("'");
//					sbTmp.append(",");
//					
////					Departcode
//					sbTmp.append(" alias4");
//					sbTmp.append("=");
//					sbTmp.append("'");
//					sbTmp.append(agent.AgentInfo$Alias8);
//					sbTmp.append("'");
//					sbTmp.append(",");
//					
//					sbTmp.append(" alias5");
//					sbTmp.append("=");
//					sbTmp.append("'");
//					sbTmp.append(agent.AgentInfo$Alias9);
//					sbTmp.append("'");
//					sbTmp.append(",");
//					
//					sbTmp.append("name");
//					sbTmp.append("=");
//					sbTmp.append("'");
//					sbTmp.append(agent.AgentInfo$Name);
//					sbTmp.append("'");
//					
//					sbTmp.append(" where ");
//					sbTmp.append(" phone_number ");
//					sbTmp.append("=");
//					sbTmp.append("'");
//					sbTmp.append(extNum);
//					sbTmp.append("'");
//					sqlTmp.add(sbTmp.toString());
//			        } else {
//						sbTmp.append("insert into suSecurityUser ");
//						sbTmp.append("(loginName,pwd,name,phone_number,"
//								+ "alias1,alias2,alias3,alias4,alias5)");
//						sbTmp.append("values (");
//						sbTmp.append("'");
//						//sbTmp.append(extNum);
//						sbTmp.append(agent.AgentInfo$Alias1);
//						sbTmp.append("'");
//						sbTmp.append(",");
//						sbTmp.append("'");
//						sbTmp.append("000000");
//						sbTmp.append("'");
//						sbTmp.append(",");
//						sbTmp.append("'");
//						sbTmp.append(agent.AgentInfo$Alias1);
//						sbTmp.append("'");
//						sbTmp.append(",");
//						sbTmp.append("'");
//						sbTmp.append(extNum);
//						sbTmp.append("'");
//						sbTmp.append(",");
//						sbTmp.append("'");
//						sbTmp.append(agent.AgentInfo$Alias1);
//						sbTmp.append("'");
//						sbTmp.append(",");
//						sbTmp.append("'");
//						sbTmp.append(agent.AgentInfo$Alias3);
//						sbTmp.append("'");
//						sbTmp.append(",");
//						sbTmp.append("'");
//						sbTmp.append(agent.AgentInfo$Alias7);
//						sbTmp.append("'");
//						sbTmp.append(",");
//						sbTmp.append("'");
//						sbTmp.append(agent.AgentInfo$Alias8);
//						sbTmp.append("'");
//						sbTmp.append(",");
//						sbTmp.append("'");
//						sbTmp.append(agent.AgentInfo$Alias9);
//						sbTmp.append("'");
//						sbTmp.append(")");
//						sqlTmp.add(sbTmp.toString());
//					}
//		        
//		        if(agent!=null && (agent.AgentInfo$Alias1==null || agent.AgentInfo$Alias1.length()==0 || agent.AgentInfo$Alias4 == null || agent.AgentInfo$Alias4.length()==0)){
////debug(agent.AgentInfo$Alias1.length()+" ###############  agent info "+agent.AgentInfo$Alias1);
//		        	for(Iterator<String> iskName=agent.AgentInfo$SkillGroup.iterator();iskName.hasNext();){
//		        		skName=iskName.next();
//		        		skill=SkillInfo.SKIP_skillMap.get(skName);
//		        		skill.skillAgent.remove(extNum);
//		        	}
//		        	AgentInfo.SKIP_agentMap.remove(extNum);
//		        	AgentInfo.SKIP_allPhone.remove(extNum);
//		        	agent=null;
//		        }else{
////		        	debug(agent.AgentInfo$Alias6+"---"+agent.AgentInfo$Num+"--------AgentInfo$Alias7-------"+agent.AgentInfo$Alias4+"------------"+agent.AgentInfo$Alias7);
//		        	
//		        	for(Iterator<String> iskName=agent.AgentInfo$SkillGroup.iterator();iskName.hasNext();){
//		        		skName=iskName.next();
//		        		skill=SkillInfo.SKIP_skillMap.get(skName);
//		        		
//		        		for(Iterator iSkill = skill.skillGranularity.entrySet().iterator(); iSkill.hasNext();){
//		        			 java.util.Map.Entry eSkill = (java.util.Map.Entry)iSkill.next();
//		        			 SkillGranularity gTmp =(SkillGranularity)eSkill.getValue();
//		       		       	int key=(Integer)eSkill.getKey();
//if(gTmp.skill$granularityBeginTime==null || gTmp.skill$granularityEndTime==null)		       		       	
//debug("skill "+skill.SkillInfo$Name +"  end or begin is null  "+key);
//		        		}
//		        		if(skill.SkillInfo$Name==null || skill.SkillInfo$Name.length()==0){
//		        			error("syncAvailableAgentData info skill null name ");
//							continue;
//		        		}
//		        	}
//		        }
//				}catch (Exception e) {
//					error("error in after sync check Agent Data agent is "+extNum+" skip it");
//					error(e);
//				}
//			}
//			for(Iterator iS = SkillInfo.SKIP_skillMap.entrySet().iterator(); iS.hasNext();) {
//		        java.util.Map.Entry eS = (java.util.Map.Entry)iS.next();
//		        skill =(SkillInfo)eS.getValue();
//		        skName=(String)eS.getKey();
//		        debug(skill.skillSumId+"-------------"+skName);
//		        if(skill.SkillInfo$Alias4==null || skill.SkillInfo$Alias4.length()==0){
//		        	SkillInfo.SKIP_skillMap.remove(skName);
//		        	SkillInfo.SKIP_allSkillName.remove(skName);
//		        	skill.aggregaeMap=null;
//		        	skill=null;
//		        }
//			}
//			
//			DAOTools.execBatchS(sqlTmp, "ZQC");
//			
////			for(Iterator iS = sgIdMap.entrySet().iterator(); iS.hasNext();) {
////				 java.util.Map.Entry eS = (java.util.Map.Entry)iS.next();
////			     skName=(String)eS.getKey();
////			     skId=(String)eS.getValue();
////			     if(!SkillInfo.SKIP_skillMap.containsKey(skName)){
////			    	 sql="select skillgrpname,skillgrpcode,a.departcode,centercode from sys_skillgroup a,centercode b where a.departcode=b.departcode and skillgrpcode like '"+skId+"%'";
////			    	 al = dao.execSelect(sql, "csrSyncDB");
////						for (int i = 0; i < al.size(); i++) {
////							try {
////								resTmp = al.get(i);
////								
////								skill=new SkillInfo();
////								skill.SkillInfo$Name=skName;
////								skill.init(initDay);
////								skill.SkillInfo$Alias4=(String) resTmp[1];
////								skill.SkillInfo$Alias5 =(String) resTmp[0];
////								skill.SkillInfo$Alias8=(String) resTmp[2];
////								skill.SkillInfo$Alias9 =(String)resTmp[3];
////								skill.SkillInfo$Alias6 =sgIdMap.get(skName);
////								skill.SkillInfo$Alias7 =sgIdName.get(skName);
////								
////								skill.skillSumId=skill.SkillInfo$Alias8+skill.SkillInfo$Alias9;
////								
////								debug(skill.SkillInfo$Alias4+"###"+skName+"########"+skill.SkillInfo$Alias5+"#######"+skill.skillSumId);
////								SkillInfo.SKIP_allSkillName.add(skName);
////								SkillInfo.SKIP_skillMap.put(skName, skill);
////							} catch (Exception e) {
////								error("refetch skill Data from csrDB exec sql: "+ sql + " in csrSyncDB pos :" + i + " skip it");
////								error(e);
////							}
////						}
////			     }
////			}
//			
////			
////			for(Iterator iAgent = AgentInfo.SKIP_agentMap.entrySet().iterator(); iAgent.hasNext();) {
////		        java.util.Map.Entry eAgent = (java.util.Map.Entry)iAgent.next();
////		        agent =(AgentInfo)eAgent.getValue();
////		        extNum=(String)eAgent.getKey();
////		        if(agent!=null && (agent.AgentInfo$Alias1==null || agent.AgentInfo$Alias1.length()==0)){
////debug(agent.AgentInfo$Num+" ###############  agent info "+extNum);
////		        }
////			}		        
//			
//		}else if(ConfInfo.procMode.equals("common") || ConfInfo.procMode.equals("abcTest")){
//			for(Iterator iAgent = AgentInfo.SKIP_agentMap.entrySet().iterator(); iAgent.hasNext();) {
//				try{
//		        java.util.Map.Entry eAgent = (java.util.Map.Entry)iAgent.next();
//		        agent =(AgentInfo)eAgent.getValue();
//		        extNum=(String)eAgent.getKey();
//		        if(agent!=null && (agent.AgentInfo$Num==null || agent.AgentInfo$Num.length()==0)){
////debug(agent.AgentInfo$Alias1.length()+" ###############  agent info "+agent.AgentInfo$Alias1);
//		        	for(Iterator<String> iskName=agent.AgentInfo$SkillGroup.iterator();iskName.hasNext();){
//		        		skName=iskName.next();
//		        		skill=SkillInfo.SKIP_skillMap.get(skName);
//		        		skill.skillAgent.remove(extNum);
//		        	}
//		        	AgentInfo.SKIP_agentMap.remove(extNum);
//		        	AgentInfo.SKIP_allPhone.remove(extNum);
//		        	agent=null;
//		        }else{
////		        	debug(agent.AgentInfo$Alias6+"---"+agent.AgentInfo$Num+"--------AgentInfo$Alias7-------"+agent.AgentInfo$Alias4+"------------"+agent.AgentInfo$Alias7);
//		        	for(Iterator<String> iskName=agent.AgentInfo$SkillGroup.iterator();iskName.hasNext();){
//		        		skName=iskName.next();
//		        		
////		        		if(extNum.equals("6933"))
////		        			debug(extNum+"-------AgentInfo$SkillGroup----"+agent.AgentInfo$SkillGroup.size()+"-----"+skName);
////		        		
//		        		skill=SkillInfo.SKIP_skillMap.get(skName);
//		        		if(skill.SkillInfo$Name==null || skill.SkillInfo$Name.length()==0){
//		        			error("syncAvailableAgentData info skill null name ");
//							continue;
//		        		}else{
//		        			skill.skillSumId="0";  // defult keep all sum data in id 0
//		        			if(SumInfo.SKIP_sumMap.containsKey(skill.skillSumId)){
//								sum=SumInfo.SKIP_sumMap.get(skill.skillSumId);
//							}else{
//								sum=new SumInfo();
//								sum.init(initDay);
//							}
//		        			
//		        			for(Iterator iS = sum.sumGranularity.entrySet().iterator(); iS.hasNext();){
//								 java.util.Map.Entry eS = (java.util.Map.Entry)iS.next();
//								 sumTmp =(SumGranularity)eS.getValue();
//								 sumTmp.sumId=skill.skillSumId;
//								 sumI=(Integer)eS.getKey();
//								 if(!sumTmp.sum$map$skillGranularity.containsKey(skill.SkillInfo$Name)){
//									 sumTmp.sum$map$skillGranularity.put(skill.SkillInfo$Name, skill.skillGranularity.get(sumI));
//								 }
//							}
//		        			SumInfo.SKIP_sumMap.put(skill.skillSumId, sum);
//		        		}
//		        	}
//		        }
//				}catch (Exception e) {
//					error("error in after sync check Agent Data agent is "+extNum+" skip it");
//					error(e);
//				}
//			}
//			for(Iterator iS = SkillInfo.SKIP_skillMap.entrySet().iterator(); iS.hasNext();) {
//		        java.util.Map.Entry eS = (java.util.Map.Entry)iS.next();
//		        skill =(SkillInfo)eS.getValue();
//		        skName=(String)eS.getKey();
//		        if(skill.SkillInfo$Name==null || skill.SkillInfo$Name.length()==0){
//		        	SkillInfo.SKIP_skillMap.remove(skName);
//		        	SkillInfo.SKIP_allSkillName.remove(skName);
//		        	skill.aggregaeMap=null;
//		        	skill=null;
//		        }else{
//		        	skill.skillSumId="0";
//		        }
//			}
//		}
//	}
	
	

	public synchronized boolean sotpIt() {
		this.flag = false;
		return true;
	}

	private volatile boolean flag = true;
//	public static String z3000IP = "192.168.0.167";
//	public static String extIP = "192.168.0.167";
	public Timestamp initDay;

	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
