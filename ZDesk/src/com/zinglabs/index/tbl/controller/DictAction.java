package com.zinglabs.index.tbl.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import com.zinglabs.servlet.CommandService;
import com.zinglabs.util.*;
import com.zinglabs.work.WorkPublic;
import com.zinglabs.work.WorkPublicDAO;

import com.zinglabs.db.DAOTools;
import com.zinglabs.index.tbl.sys.*;
import com.zinglabs.index.tbl.util.*;
import com.zinglabs.log.LogUtil;




public class DictAction extends DispatchAction {
	private static final long serialVersionUID = 3656534994576080649L;
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	
	public ActionForward showDictTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		
		String actionHandler = "showDictDetail.jsp?dictId=";
		String actionHandler1 = "showDictDetail1.jsp?dictId=";
		String target = "content";
		String grade_index = request.getParameter("grade_index");
		ResultSet dictSet1 = this.findShipTypeName( grade_index, conn, stmt );
		String grade_name = "";
		while(dictSet1.next()){
			grade_name = dictSet1.getString("grade_name");
		}

        ResultSet dictSet = null;
		try {
			StringBuffer buffer = new StringBuffer();


			XTreeNode rootNode = new XTreeNode(String.valueOf(Dict.ROOT_NODE_ID),
					grade_name, String.valueOf(Dict.ROOT_NODE_PARENTID),				
					actionHandler + Dict.ROOT_NODE_ID +"&grade_index="+grade_index, target);
   
			rootNode.setLevel(1);
			XTreeManager.beginTree(buffer, rootNode);
			List dictList = new ArrayList();
			dictSet = this.findShipTypeId(Dict.ROOT_NODE_ID ,grade_index,conn,stmt1);
			

			Dict dict = new Dict();
            int  i=0;
			while(dictSet.next()){

				dict.setId(dictSet.getInt("ID")) ;
				dict.setParentId(dictSet.getInt("PARENTID"));		
				dict.setIdPath(dictSet.getString("IDPATH"));
				dict.setGrade_index(dictSet.getString("grade_index"));
				dict.setDescription(dictSet.getString("DESCRIPTION"));
				dict.setPercent(dictSet.getInt("percent"));
				dictList.add(dict);
				
           
				

				
				if(!WorkPublicDAO.isGradeIndexLeaf( conn, grade_index, dict.getId() ) )
				{
					XTreeNode node = new XTreeNode(String.valueOf(dict.getId()),
							dict.getDescription() + "(" + dict.getPercent()+ ")", String.valueOf(dict
									.getParentId()), actionHandler + dict.getId()+"&grade_index="+grade_index,
							"images/dept.png", "images/dept.png", target);
					if (dict.getParentId() == Dict.ROOT_NODE_ID) {
						node.setLevel(1);
					}
					
					XTreeManager.addTreeNode(buffer, node);
				} else {
		
					XTreeNode node = new XTreeNode(String.valueOf(dict.getId()),
							dict.getDescription() + "(" + dict.getPercent() + ")", String.valueOf(dict.getParentId()), 
                            actionHandler1 + dict.getId()+"&grade_index="+grade_index, "images/file.png",
							"images/file.png", target);
					if (dict.getParentId() == Dict.ROOT_NODE_ID) {
						node.setLevel(1);
					}
					
					XTreeManager.addTreeNode(buffer, node);
				}
			}
		
				
   
			XTreeManager.endTree(buffer);

			
			String dictTree = buffer.toString();
			
    
			RequestHelper.setRequestObject(request, "dictTree", dictTree);

		} catch (Exception e) {
			LogUtil.debug("show dict tree false", SKIP_Logger);
//			logger.debug("Exception happen:"+ e.toString());
		}
		finally{
			if (dictSet1 != null) {
				dictSet1.close();// add by 12 03 
				dictSet1 = null;
			}						
			if (dictSet != null) {
				dictSet.close();// add by 12 03 
				dictSet = null;
			}	
			 DAOTools.releaseConnectionOutS("ZQC", conn);
//			if( stmt != null )
//			{
//				stmt.close();
//				stmt = null;
//			}
//			if( stmt1 != null )
//			{
//				stmt1.close();
//				stmt1 = null;
//			}
//			if( stmt2 != null )
//			{
//				stmt2.close();
//				stmt2 = null;
//			}			
//			if( conn != null )
//			{
//				DAOTools.releaseConnectionOutS("ZQC", conn);
//				
//			}
		}

		request.setAttribute("grade_name", grade_name);	
		request.setAttribute("grade_index", grade_index);
		return mapping.findForward("showDictTree");
	}
	
	public ResultSet findShipTypeId(int id, String grade_index, Connection conn,Statement stmt ) throws Exception{
		
		List All = null;
		ResultSet set = null;
		try{
			stmt = conn.createStatement();
			
           
            String sql ="select * from dictinfo where IDPATH="+id+ " and grade_index ='"+grade_index+"' order by id";
          
			set = stmt.executeQuery(sql);
        
        }catch(Exception e){
//            logger.debug("Exception :"+e.toString());
        	LogUtil.debug("findShipTypeId false", SKIP_Logger);
        }
        return set;
	}

	public ActionForward viewDict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		Statement stmt1 = null;
		
		int dictId = RequestHelper.getIntParam(request, "dictId", 0);
		int a = RequestHelper.getIntParam(request, "a", 0);
		
		String grade_index = request.getParameter("grade_index");
		ResultSet dictSet = this .findShipTypeName(grade_index, conn, stmt );
		String grade_name = "";
		while(dictSet.next()){
			grade_name = dictSet.getString("grade_name");
		}
		

		List permittedList = new ArrayList();
		RequestHelper.setRequestObject(request, SystemConfig.permittedList,
				permittedList);
		ResultSet set = null;
		Dict dict = new Dict();
		try {

			set = this.findByPrimaryKey(dictId,conn,stmt1);
			List dictList = new ArrayList();
			

			while(set.next()){

				dict.setId(set.getInt("id")) ;
				dict.setParentId(set.getInt("parentId"));		
				dict.setIdPath(set.getString("idPath"));
				dict.setGrade_index(set.getString("grade_index"));
				dict.setMin_value(set.getString("min_value"));
				dict.setMax_value(set.getString("max_value"));
				dict.setReference_value(set.getString("reference_value"));
				String reference2_value =set.getString("reference2_value");
				if(reference2_value.equals("null") ){
					dict.setReference2_value(set.getString("reference_value"));
				}else{
					dict.setReference2_value(set.getString("reference2_value"));
				}
			//	dict.setReference3_value(set.getString("reference3_value"));
			//	dict.setReference4_value(set.getString("reference4_value"));
				dict.setDescription(set.getString("description"));
//��Ӱٷֱ�--2009-02-09
				dict.setPercent(set.getInt("percent"));
				dict.setValue_remark(set.getString("value_remark"));
//�ж��Ƿ���Ҷ�ӽڵ�
				if(WorkPublicDAO.isGradeIndexLeaf( conn, grade_index, dict.getId())){
					dict.setTest(0);
				}else{
					dict.setTest(1);
				}
				dictList.add(dict);
				
			}
				RequestHelper.setRequestObject(request, Dict.CTX_DICT, dict);
			

		} catch (Exception e) {
	
			LogUtil.error(e, SKIP_Logger);
			return mapping.findForward("actionResult");
		}
		finally
		
		{				
			if (dictSet != null) {
				dictSet.close();// add by 12 03 
				dictSet = null;
			}	
			if (set != null) {
				set.close();// add by 12 03 
				set = null;
			}			
			if( stmt != null )
			{
				stmt.close();
				stmt = null;
			}
			if( stmt1 != null )
			{
				stmt1.close();
				stmt1 = null;
			}			
			if( conn != null )
			{
				DAOTools.releaseConnectionOutS("ZQC", conn);
				
			}			
		}
		if(a==1 && dict.getParentId() != 0){
			request.setAttribute("grade_name", grade_name);
			request.setAttribute("grade_index", grade_index);
			return mapping.findForward("view");
		}
		else {
			request.setAttribute("grade_name", grade_name);
			request.setAttribute("grade_index", grade_index);
			return mapping.findForward("view1");
		}
	}

	public ResultSet findByPrimaryKey(int id, Connection conn, Statement stmt )throws Exception{
		List All = null;
		ResultSet set = null;
		try{
			stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from dictinfo where id =" );
			sql.append(id);
			set =  (ResultSet) stmt.executeQuery(sql.toString());
			}catch(Exception e){
				LogUtil.error(e, SKIP_Logger);
			}
			return set;
	}
	public ResultSet findShipTypeName(String grade_index, Connection conn, Statement stmt )throws Exception{
		ResultSet set = null;
		List All = null;
		try{
			stmt = conn.createStatement();
			String sql = "select grade_name from SA_QC_GRADE_DICTINFO where id ="+grade_index;

			set = stmt.executeQuery(sql);
		}catch(Exception e){
			LogUtil.error("Utility.findShipTypeName", SKIP_Logger);
		}
		return set; 
	}
	

	

	public ActionForward addDict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet percent_set = null;
		
		int parentId = RequestHelper.getIntParam(request, "parentId", 0);
		String grade_index = request.getParameter("grade_index");
		//ResultSet dictSet = this .findAllOfGradeId(grade_index, conn, stmt );
		ResultSet dictSet = this .findShipTypeName(grade_index, conn, stmt );
		String grade_name = "";
		while(dictSet.next()){
			grade_name = dictSet.getString("grade_name");
		}
		
		ResultSet set = null;
		try {
			set = this.getDictEntity(parentId, conn, stmt1 );
			Dict dict = new Dict();
			while(set.next()){
				
				dict.setId(set.getInt("id")) ;
				dict.setParentId(set.getInt("parentId"));		
				dict.setIdPath(set.getString("idPath"));
				dict.setGrade_index(set.getString("grade_index"));
				dict.setDescription(set.getString("description"));		
				//today add
				dict.setMin_value(set.getString("min_value"));
				dict.setMax_value(set.getString("max_value"));
				dict.setReference_value(set.getString("reference_value"));
				String reference2_value =set.getString("reference2_value");
				if(reference2_value.equals("null") ){
					dict.setReference2_value(set.getString("reference_value"));
				}else{
					dict.setReference2_value(set.getString("reference2_value"));
				}
				dict.setPercent(set.getInt("percent"));
				/*dict.setReference3_value(set.getString("reference3_value"));
				dict.setReference4_value(set.getString("reference4_value"));*/
				
			}
			RequestHelper.setRequestObject(request, Dict.CTX_DICT, dict);
//���ͬ�Դ�IdΪparentId��ѡ�ʣ������еİٷֱ�֮��2009-02-09=--------------
			
			percent_set = getCurrentlypercentCount(parentId,Integer.valueOf(grade_index),conn,stmt2);
			int percent_count = 0;
			while(percent_set.next()){
				percent_count += percent_set.getInt("percent");	
			}
			
				//percent_count = 100 - percent_count ;
				//percent_count = percent_count ;
				percent_count = dict.getPercent() - percent_count;
				String S_percent_count = "" + percent_count;
		/*		if(dict.getParentId() == 0);{
					S_percent_count = "0";
				}*/
				request.setAttribute("percent_count", S_percent_count);
			
		} catch (Exception e) {
		
			log.error(e);
			return mapping.findForward("actionResult");
		}
		finally
		{
			if (set != null) {
				set.close();// add by 12 03 
				set = null;
			}	
			if( dictSet != null )
			{
				dictSet.close();
				dictSet = null;
			}
			if( percent_set != null )
			{
				percent_set.close();
				percent_set = null;				
			}
			if( stmt != null )
			{
				stmt.close();
				stmt = null;
			}
			if( stmt1 != null )
			{
				stmt1.close();
				stmt1 = null;
			}
			if( stmt2 != null )
			{
				stmt2.close();
				stmt2 = null;
			}			
			if( conn != null ){
				DAOTools.releaseConnectionOutS("ZQC", conn);
				
			}
		}
		

		request.setAttribute("grade_name", grade_name);
		request.setAttribute("grade_index", grade_index);
		return mapping.findForward("add");
	}
	
	public ResultSet findAllOfGradeId(String grade_index, Connection conn, Statement stmt )throws Exception{
		ResultSet set = null;
		List All = null;
		try{
			stmt = conn.createStatement();
			String sql = "select grade_name from SA_QC_GRADE_DICTINFO where id ="+grade_index;

			set = stmt.executeQuery(sql);
		}catch(Exception e){
			LogUtil.error("Utility.findShipTypeName", SKIP_Logger);
		}
		return set; 
	}
	
	private ResultSet getDictEntity(int dictId, Connection conn, Statement stmt )
			throws Exception {
		
		List All = null;
		ResultSet set = null;
		try{
			stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from dictinfo where id =" );
			sql.append(dictId);
			set =  (ResultSet) stmt.executeQuery(sql.toString());
			}catch(Exception e){
				LogUtil.debug("getDictEntity", SKIP_Logger);
			}
			return set;
	}

	public ActionForward saveDict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//��Ҫ�޸� 2009-02-11--��������
		Dict dict = ((DictForm) form).getDict();
		String grade_index = request.getParameter("grade_index");
		
		if (dict.getId() <= 0) {
			try {
				dict.setGrade_index(grade_index);
				if(dict.getMax_value() == null || dict.getMax_value().equals("")){
					dict.setMax_value("0");
				}
				
				if(dict.getMin_value() == null || dict.getMin_value().equals("")){
					dict.setMin_value("0");
				}
				
				if(dict.getReference_value() == null || dict.getReference_value().equals("")){
					dict.setReference_value("0");
				}	
				maintainIdPath(dict);
				this.createDict(dict);
				String[] description = { "log.dict.create", dict.getGrade_index(),
						dict.getDescription(),/*today add*/dict.getMin_value(),dict.getMax_value(),dict.getReference_value() };
	
				
        
			} catch (Exception e) {
	
				LogUtil.error(e, SKIP_Logger);
			}
		} else {
			try {
				dict.setGrade_index(grade_index);
				// 11 18 add
				if(dict.getMax_value() == null || dict.getMax_value().equals("")){
					dict.setMax_value("0");
				}
				
				if(dict.getMin_value() == null || dict.getMin_value().equals("")){
					dict.setMin_value("0");
				}
				
				if(dict.getReference_value() == null || dict.getReference_value().equals("")){
					dict.setReference_value("0");
				}
				
				if(dict.getReference2_value() == null || dict.getReference2_value().equals("")){
					dict.setReference2_value("0");
				}
				maintainIdPath(dict);
				this.updateDict(dict);
				//System.out.println("grade_index="+dict.getGrade_index());
				//System.out.println("min_value="+dict.getMin_value());
				String[] description = { "log.dict.update", dict.getGrade_index(),
						dict.getDescription(),/*today add*/dict.getMin_value(),dict.getMax_value(),dict.getReference_value() };
		
				
			} catch (Exception e) {
				
				LogUtil.error(e, SKIP_Logger);
			}
		}
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		
		ResultSet dictSet = this .findShipTypeName(grade_index,conn, stmt);
		String grade_name = "";
		while(dictSet.next()){
			grade_name = dictSet.getString("grade_name");
		}
		if( dictSet != null )
		{
			dictSet.close();
			dictSet = null;			
		}
		if( stmt != null )
		{
			stmt.close();
			stmt = null;
		}
		if( conn != null )
		{
			DAOTools.releaseConnectionOutS("ZQC", conn);
			
		}

		request.setAttribute("grade_name", grade_name);
		request.setAttribute("grade_index", grade_index);

		return mapping.findForward("actionResult");
	}
	
	private void maintainIdPath(Dict dict) throws Exception {
		if (dict.getParentId() == Dict.ROOT_NODE_ID) {
			dict.setIdPath("." + Dict.ROOT_NODE_ID + ".");
		} else {
			Connection conn = DAOTools.getConnectionOutS("ZQC");
			Statement stmt = null;
			ResultSet set = this.findByPrimaryKey(dict.getParentId(),conn,stmt);
			Dict parentDict = new Dict();
			while(set.next()){
				parentDict.setId(set.getInt("id")) ;
				parentDict.setParentId(set.getInt("parentId"));		
				parentDict.setIdPath(set.getString("idPath"));
				parentDict.setGrade_index(set.getString("grade_index"));
				parentDict.setDescription(set.getString("description"));		
			}
			dict.setIdPath(parentDict.getIdPath() + dict.getParentId() + ".");
			if( set != null )
			{
				set.close();
				set = null;
			}
			if( stmt != null )
			{
				stmt.close();
				stmt = null;
			}
			if( conn != null )
			{
				DAOTools.releaseConnectionOutS("ZQC", conn);
				
			}
		}
	}

	public boolean createDict(Dict dict)throws Exception{
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		List All = null;
		ResultSet set = null;
		boolean type = true;
		try{
			stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO dictinfo(parentId,idPath,grade_index,description,min_value,max_value,reference_value,reference2_value,percent,value_remark) VALUES (" );
			sql.append(dict.getParentId());
			sql.append(",'");
			sql.append(dict.getIdPath());
			sql.append("','");
			sql.append(dict.getGrade_index());
			sql.append("','");
			sql.append(dict.getDescription());
			sql.append("','");
			sql.append(dict.getMin_value());
			sql.append("','");
			sql.append(dict.getMax_value());
			sql.append("','");
			sql.append(dict.getReference_value());
// 2009-02-09----------			
			sql.append("','");
			sql.append(dict.getReference2_value());
			sql.append("','");
/*			sql.append(dict.getReference3_value());
			sql.append("','");
			sql.append(dict.getReference4_value());
			sql.append("','");*/
			sql.append(dict.getPercent());
			sql.append("','");
			sql.append(dict.getValue_remark());
			sql.append("')");
			LogUtil.debug("sql--",SKIP_Logger);
			int a = stmt.executeUpdate(sql.toString());
			if(a<1){
				type = true;
			}
			}catch(Exception e){
				//System.out.println(e.getMessage());
				LogUtil.error(e, SKIP_Logger);
				type = false;		
			}finally
			{
				if( set != null )
				{
					set.close();
					set = null;
				}
				if( stmt != null )
				{
					stmt.close();
					stmt = null;
				}				
				if( conn != null )
				{
					DAOTools.releaseConnectionOutS("ZQC", conn);
					
				}
			}
			
		return type;
	}
	
	public boolean updateDict(Dict dict)throws Exception{
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		List All = null;
		ResultSet set = null;
		boolean type = true;
		try{
			stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE dictinfo set parentId=" );
			sql.append(dict.getParentId());
			sql.append(",grade_index='");
			sql.append(dict.getGrade_index());
			sql.append("',description='");
			sql.append(dict.getDescription());
			sql.append("',min_value='");
			sql.append(dict.getMin_value());
			sql.append("',max_value='");
			sql.append(dict.getMax_value());
			sql.append("',reference_value='");
			sql.append(dict.getReference_value());
			sql.append("',reference2_value='");
			sql.append(dict.getReference2_value());
			/*sql.append("',reference3_value='");
			sql.append(dict.getReference3_value());
			sql.append("',reference4_value='");
			sql.append(dict.getReference4_value());*/
//����޸İٷֱ�2009-02-09			
			sql.append("',percent='");
			sql.append(dict.getPercent());			
			sql.append("',value_remark='");
			sql.append(dict.getValue_remark());			
			sql.append("' where id = ");
			sql.append(dict.getId());
			int a = stmt.executeUpdate(sql.toString());
			if(a<1){
				type = true;
			}
			}catch(Exception e){
				//System.out.println(e.getMessage());
				LogUtil.debug("updateDict", SKIP_Logger);
				type = false;
			}finally
			{
				if( set != null )
				{
					set.close();
					set = null;
				}
				if( stmt != null )
				{
					stmt.close();
					stmt = null;
				}				
				if( conn != null )
				{
					DAOTools.releaseConnectionOutS("ZQC", conn);
					
				}
			}
		return type;
	}
	
	public ActionForward editDict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int dictId = RequestHelper.getIntParam(request, "dictId", 0);
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
		Statement stmt4 = null;
		
		ResultSet set = null;
		ResultSet parentSet = null;
		ResultSet dictSet = null;
		ResultSet precnet_set = null;
		ResultSet precnetMin_set  = null;
		ResultSet modify_precnet_set = null;		
		//System.out.println("--------------------------editDict");
		try {			
			set = this.getDictEntity(dictId,conn,stmt);
			Dict dict = new Dict();
			while(set.next()){
				dict.setId(set.getInt("id")) ;
				dict.setParentId(set.getInt("parentId"));		
				dict.setIdPath(set.getString("idPath"));
				dict.setGrade_index(set.getString("grade_index"));
				dict.setDescription(set.getString("description"));		
	//11 18 add			
				dict.setMin_value(set.getString("min_value"));
				dict.setMax_value(set.getString("max_value"));
				dict.setReference_value(set.getString("reference_value"));
				String reference2_value =set.getString("reference2_value");
				if(reference2_value.equals("null") ){
					dict.setReference2_value(set.getString("reference_value"));
				}else{
					dict.setReference2_value(set.getString("reference2_value"));
				}
				/*dict.setReference3_value(set.getString("reference3_value"));
				dict.setReference4_value(set.getString("reference4_value"));*/
// 2009-02-09---add----
				dict.setPercent(set.getInt("percent"));
				dict.setValue_remark(set.getString("value_remark"));
			}
			RequestHelper.setRequestObject(request, Dict.CTX_DICT, dict);

			Dict parentDict = new Dict();
			parentSet = this.getDictEntity(dict.getParentId(),conn,stmt1);
			while(parentSet.next()){
				parentDict.setId(parentSet.getInt("id")) ;
				parentDict.setParentId(parentSet.getInt("parentId"));		
				parentDict.setIdPath(parentSet.getString("idPath"));
				parentDict.setGrade_index(parentSet.getString("grade_index"));
				parentDict.setDescription(parentSet.getString("description"));	
//	 2009-02-09---add----
				parentDict.setPercent(parentSet.getInt("percent"));
			}
			RequestHelper.setRequestObject(request, Dict.CTX_PARENT_DICT,
					parentDict);
			
			String grade_index = (String)request.getParameter("grade_index");
			//logger.debug("editDict grade_index:"+grade_index);
			dictSet = this .findShipTypeName(grade_index,conn,stmt2);
			String grade_name = "";
			while(dictSet.next()){
				grade_name = dictSet.getString("grade_name");
			}
			request.setAttribute("grade_name", grade_name);
			request.setAttribute("grade_index", grade_index);	
//���ͬ�Դ�IdΪparentId��ѡ�ʣ������еİٷֱ�֮��2009-02-09=--------------

			precnet_set = getCurrentlypercentCount(dict.getParentId(),Integer.valueOf(grade_index),conn,stmt3);
			modify_precnet_set = getCurrentlypercent(dict.getId(),conn,stmt4);
			int percent_count = 0;
			int percentMin_count = 0;
			int modify_percent = 0;
			while(precnet_set.next()){
				percent_count += precnet_set.getInt("percent");		
			}
			while(modify_precnet_set.next()){
				modify_percent = modify_precnet_set.getInt("percent");		
			}
			//percent_count = 100 - percent_count + modify_percent;
			percent_count =parentDict.getPercent() - percent_count + dict.getPercent();
			String S_percent_count = "" + percent_count;
			precnetMin_set = getCurrentlypercentCount(dict.getId(),Integer.valueOf(grade_index),conn,stmt3);
			while(precnetMin_set.next()){
				percentMin_count += precnetMin_set.getInt("percent");		
			}
			String S_percentMin_count = "" + percentMin_count;
			
			//System.out.println("ʣ��ٷֱȣ�" + S_percent_count);
			request.setAttribute("percentMin_count", S_percentMin_count);	
			request.setAttribute("percent_count", S_percent_count);			

		} catch (Exception e) {
			
			LogUtil.error(e, SKIP_Logger);

			return mapping.findForward("actionResult");
		}finally
		{
			if( set != null )
			{
				set.close();
				set = null;
			}
			if( parentSet != null )
			{
				parentSet.close();
				parentSet = null;
			}
			if( dictSet != null )
			{
				dictSet.close();
				dictSet = null;
			}
			if( precnet_set != null )
			{
				precnet_set.close();
				precnet_set = null;
			}
			if( modify_precnet_set != null )
			{
				modify_precnet_set.close();
				modify_precnet_set = null;
			}
			if( stmt != null )
			{
				stmt.close();
				stmt = null;
			}
			if( stmt1 != null )
			{
				stmt1.close();
				stmt1 = null;
			}
			if( stmt2 != null )
			{
				stmt2.close();
				stmt2 = null;
			}
			if( stmt3 != null )
			{
				stmt3.close();
				stmt3 = null;
			}
			if( stmt4 != null )
			{
				stmt4.close();
				stmt4 = null;
			}			
			if( conn != null )
			{
				DAOTools.releaseConnectionOutS("ZQC", conn);
				
			}
		}



		return mapping.findForward("edit");
	}

	public ActionForward delDict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		Statement stmt1 = null;
		int dictId = RequestHelper.getIntParam(request, "dictId", 0);
		ResultSet set = null;
		ResultSet dictSet = null;
		try {
			set = this.findParentid(dictId,conn,stmt);
			Dict dict = new Dict();
			int i = 0;
			while(set.next()){
				i++;
				dict.setId(set.getInt("id")) ;
				dict.setParentId(set.getInt("parentId"));		
				dict.setIdPath(set.getString("idPath"));
				dict.setGrade_index(set.getString("grade_index"));
				dict.setDescription(set.getString("description"));		
			}
			if(i>0){
				
			}else{
				this.deleteDict(dictId);
			}
			
			String[] description = { "log.dict.delete", dict.getGrade_index(),
					dict.getDescription() };
			
			String grade_index = request.getParameter("grade_index");
			
			dictSet = this .findShipTypeName(grade_index,conn,stmt1);
			String grade_name = "";
			while(dictSet.next()){
				grade_name = dictSet.getString("grade_name");
			}						

		} catch (Exception e) {
			
	
			LogUtil.error(e, SKIP_Logger);
		}finally
		{
			if( set != null )
			{
				set.close();
				set = null;
			}
			if( dictSet != null )
			{
				dictSet.close();
				dictSet = null;
			}
			if( stmt != null)
			{
				stmt.close();
				stmt = null;
			}
			if( stmt1 != null)
			{
				stmt1.close();
				stmt1 = null;
			}			
			if( conn != null )
			{
				DAOTools.releaseConnectionOutS("ZQC", conn);
				
			}
		}


		return mapping.findForward("actionResult");
	}
	
	public ResultSet findParentid(int id, Connection conn, Statement stmt )throws Exception{
		List All = null;
		ResultSet set = null;
		try{
			stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer();	
			sql.append("select * from dictinfo where parentid =" );
			sql.append(id);
			sql.append(" ORDER BY idPath");
			
			set =  (ResultSet) stmt.executeQuery(sql.toString());
			}catch(Exception e){
		
				LogUtil.error(e, SKIP_Logger);
			}
			
			return set;
	}
	
	public boolean deleteDict(int dictId)throws Exception{
		
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		List All = null;
		ResultSet set = null;
		boolean type = true;
		try{
			stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM dictinfo WHERE id =" );
			sql.append(dictId);
			int a = stmt.executeUpdate(sql.toString());
			if(a<1){
				type = true;
			}
			}catch(Exception e){
				//System.out.println(e.getMessage());
				LogUtil.error(e, SKIP_Logger);
				type = false;
			}finally
			{
				if( set != null )
				{
					set.close();
					set = null;
				}
				if( stmt != null )
				{
					stmt.close();
					stmt = null;
				}				
				if( conn != null )
				{
					DAOTools.releaseConnectionOutS("ZQC", conn);
					
				}
			}
		return type;
	}
	
	public ActionForward showDict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		String grade_index = request.getParameter("grade_index");
		ResultSet dictSet = this .findShipTypeName(grade_index,conn,stmt);
		String grade_name = "";
		while(dictSet.next()){
			grade_name = dictSet.getString("grade_name");
		}	
		request.setAttribute("grade_name", grade_name);
		request.setAttribute("grade_index", grade_index);
		if( dictSet != null )
		{
			dictSet.close();
			dictSet = null;
		}
		if( stmt != null )
		{
			stmt.close();
			stmt = null;
		}
		if( conn != null )
		{
			DAOTools.releaseConnectionOutS("ZQC", conn);
			
		}
		return mapping.findForward("index");
	}  

	public ActionForward frameDict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt = null;
		String grade_index = request.getParameter("grade_index");
		ResultSet dictSet = this .findShipTypeName(grade_index,conn,stmt);
		String grade_name = "";
		while(dictSet.next()){
			grade_name = dictSet.getString("grade_name");
		}
		request.setAttribute("grade_name", grade_name);
		request.setAttribute("grade_index", grade_index);
		if( dictSet != null )
		{
			dictSet.close();
			dictSet = null;
		}
		if( stmt != null )
		{
			stmt.close();
			stmt = null;
		}
		if( conn != null )
		{
			DAOTools.releaseConnectionOutS("ZQC", conn);
			
		}		
		return mapping.findForward("frame");
	}  
	
	
	
	public ResultSet getCurrentlypercentCount(int parentId,int grade_index, Connection conn, Statement stmt )throws Exception{
		ResultSet set = null;
		List All = null;
		try{
			stmt = conn.createStatement();
			String sql = "select percent from dictinfo where PARENTID = "+parentId + " and grade_index=" + grade_index;
			
			set = stmt.executeQuery(sql);
		}catch(Exception e){
			LogUtil.error(e, SKIP_Logger);
		}
		return set; 
	}
	
	public ResultSet getCurrentlypercent(int id, Connection conn, Statement stmt )throws Exception{
		ResultSet set = null;
		List All = null;
		try{
			stmt = conn.createStatement();
			String sql = "select percent from dictinfo where id = "+ id;
			
			set = stmt.executeQuery(sql);
		}catch(Exception e){
			LogUtil.error(e, SKIP_Logger);
		}
		return set; 
	}	
	
	public ActionForward countPoint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt3 = null;
		
		String grade_index = request.getParameter("grade_index");
		double countResult = WorkPublic.getCountTotal( conn, grade_index, null, 1 );
		ResultSet dictSet = this.findShipTypeName(grade_index,conn,stmt3);
		String grade_name = "";
		while(dictSet.next()){
			grade_name = dictSet.getString("grade_name");
		}
		DecimalFormat df = new DecimalFormat("#.00"); 
		String str_countResult = df.format(countResult);
		//System.out.println("���������" + str_countResult);
		request.setAttribute("grade_name", grade_name);
		request.setAttribute("grade_index", grade_index);
		request.setAttribute("showcountResult", "SHOW");
		request.setAttribute("countResult", str_countResult);
		if( stmt3 != null )
		{
			stmt3.close();
			stmt3 = null;
		}		
		if( conn != null )
		{
			DAOTools.releaseConnectionOutS("ZQC", conn);
			
		}				
		//System.out.println("�����ǣ�" + countResult);
		return mapping.findForward("index");
	} 		
	public ActionForward countPointSum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DAOTools.getConnectionOutS("ZQC");
		Statement stmt3 = null;
		
		String grade_index = request.getParameter("grade_index");
		double countResult = WorkPublic.getCountSum( conn, grade_index, 1 );
		ResultSet dictSet = this.findShipTypeName(grade_index,conn,stmt3);
		String grade_name = "";
		while(dictSet.next()){
			grade_name = dictSet.getString("grade_name");
		}
		DecimalFormat df = new DecimalFormat("#.00"); 
		String str_countResult = df.format(countResult);
		//System.out.println("���������" + str_countResult);
		request.setAttribute("grade_name", grade_name);
		request.setAttribute("grade_index", grade_index);
		request.setAttribute("showcountResult", "SHOW");
		request.setAttribute("countResult", str_countResult);
		if( stmt3 != null )
		{
			stmt3.close();
			stmt3 = null;
		}		
		if( conn != null )
		{
			DAOTools.releaseConnectionOutS("ZQC", conn);
			
		}				
		//System.out.println("�����ǣ�" + countResult);
		return mapping.findForward("index");
	} 	
}
