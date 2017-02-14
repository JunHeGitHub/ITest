

package com.zinglabs.work;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;


import com.zinglabs.db.DAOTools;

import com.zinglabs.util.*;

class CMyCellNew
{
	
	public int 		ID 	 = 0;		
	public int 		PARENTID 	 = 0;		

	public int 		TOPPARENTID	= 0 ;
	
	public String 	DESCRIPTION 	 = "";		
	public int 		nRowSpan = 0;		
	public int 		nColSpan = 0;		
	public boolean 	isLeaf 	 = false;	
	public int 		nRow 	 = 0;	
	public float    reference_value = 0;
	
	public int      perent = 0;
}

class CMyRowNew
{
	public ArrayList cells;
	CMyRowNew()
	{
		cells = new ArrayList();
	}
	
	public CMyCellNew AppendCell()
	{
		CMyCellNew cell = new CMyCellNew();
		cell.ID = 100;
		cells.add(cell);
		return cell;
	}
	
	public int CellCount()
	{
		return cells.size();
	}
	
}

class CMyTreeNew
{
	public ArrayList leafcells;
	public ArrayList rows;
	CMyTreeNew()
	{
		leafcells = new ArrayList();
		rows = new ArrayList();
	}
	
	public CMyRowNew AppendRow()
	{
		CMyRowNew row = new CMyRowNew();
		rows.add(row);
		return row;
	}
	
	public int RowCount()
	{
		return rows.size();
	}
	
	public void ClearRows()
	{
		rows.clear();
		leafcells.clear();
	}
}

class CMyTableNew
{
	private Connection m_conn;
	private CMyTreeNew m_tree;
	public String sBeforeRow = "";
	public String sAfterRow = "";
	public String sTdProp = ""; 
    public String gradeIndex = "0";

	CMyTableNew(Connection conn, String index)
	{
		m_conn = conn;
        gradeIndex = index;
		m_tree = new CMyTreeNew();
	}
	
	private int ProcessCell(int iRow, int PARENTID,String gradeIndex)
	{
		try {
			String sSql = "SELECT ID,DESCRIPTION,reference_value,IDPATH,percent FROM dictinfo where PARENTID=" + PARENTID+" and grade_index='"+gradeIndex+"'";
	  		Statement stmt = m_conn.createStatement(); 
	  		ResultSet rs = stmt.executeQuery(sSql); 
			CMyRowNew row = null;
			int nCols = 0;
		  	while(rs.next())
		  	{ 
		  		if( null == row )
		  		{
					if( iRow > m_tree.RowCount() )
						row = m_tree.AppendRow();
					else
						row = (CMyRowNew)m_tree.rows.get(iRow-1);
		  		}
		  		
		  		CMyCellNew cell = row.AppendCell();
		  		cell.nRow = iRow;
		  		cell.ID = rs.getInt(1);
		  		cell.PARENTID = PARENTID;
		  		
		  		cell.DESCRIPTION = rs.getString(2);
		  		cell.perent = rs.getInt("percent");
		  		cell.reference_value = rs.getFloat(3);

		  		String strPath = rs.getString(4);
		  		strPath = strPath.trim();
		  		String[] IDS = strPath.split("\\.");
		  		
		  		if( IDS.length <= 2 )
		  			cell.TOPPARENTID = cell.ID;
		  		else
		  			cell.TOPPARENTID = Integer.valueOf(IDS[2]).intValue();
		  		
		  		cell.nRowSpan = 1;
		  		
		  		cell.nColSpan = ProcessCell(iRow+1, cell.ID,gradeIndex);
		  		
		  		if( cell.nColSpan < 1 )	
		  		{
		  			cell.nColSpan = 1;
		  			cell.isLeaf = true;
		  			m_tree.leafcells.add(cell);	
		  		}
		  		else
		  		{
		  			cell.isLeaf = false;
		  		}
		  		
		  		nCols += cell.nColSpan;
			} 
			rs.close();
			stmt.close();
			
			return nCols;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	private int ProcessDictCell(int iRow, int PARENTID,String gradeIndex)
	{
		try {
			String sSql = "SELECT ID,DESCRIPTION,reference_value,IDPATH,percent FROM dictinfo where PARENTID=" + PARENTID+" and grade_index='"+gradeIndex+"'";
	  		Statement stmt = m_conn.createStatement(); 
	  		ResultSet rs = stmt.executeQuery(sSql); 
			CMyRowNew row = null;
			int nCols = 0;
		  	while(rs.next())
		  	{ 
		  		if( null == row )
		  		{
					if( iRow > m_tree.RowCount() )
						row = m_tree.AppendRow();
					else
						row = (CMyRowNew)m_tree.rows.get(iRow-1);
		  		}
		  		
		  		CMyCellNew cell = row.AppendCell();
		  		cell.nRow = iRow;
		  		cell.ID = rs.getInt(1);
		  		cell.PARENTID = PARENTID;
		  		cell.DESCRIPTION = rs.getString(2);
		  		cell.reference_value = rs.getFloat(3);
		  		cell.perent = rs.getInt("percent");

		  		String strPath = rs.getString(4);
		  		cell.perent = rs.getInt(5);
		  		strPath = strPath.trim();
		  		String[] IDS = strPath.split("\\.");
		  		
		  		if( IDS.length <= 2 )
		  			cell.TOPPARENTID = cell.ID;
		  		else
		  			cell.TOPPARENTID = Integer.valueOf(IDS[2]).intValue();
		  		
		  		cell.nRowSpan = 1;
		  		
		  		cell.nColSpan = ProcessCell(iRow+1, cell.ID,gradeIndex);
		  		
		  		if( cell.nColSpan < 1 )	
		  		{
		  			cell.nColSpan = 1;
		  			cell.isLeaf = true;
		  			m_tree.leafcells.add(cell);	
		  		}
		  		else
		  		{
		  			cell.isLeaf = false;
		  		}
		  		
		  		nCols += cell.nColSpan;
			} 
			rs.close();
			stmt.close();
			
			return nCols;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	public boolean PrcessTree()
	{
		m_tree.ClearRows();
		
		int nCols = ProcessCell(1, 0, gradeIndex);
		
		int nYZCnt = m_tree.leafcells.size();
		int nTreeHeight = m_tree.RowCount();
		CMyCellNew yzCell = null;
		for(int i=0; i<nYZCnt; i++ )
		{
			yzCell = (CMyCellNew)m_tree.leafcells.get(i);
			yzCell.nRowSpan = nTreeHeight - yzCell.nRow + 1;
		}
		
		return true;
	}
	public boolean PrcessTreeDict()
	{
		m_tree.ClearRows();
		
		int nCols = ProcessDictCell(1, 0, gradeIndex);
		
		int nYZCnt = m_tree.leafcells.size();
		int nTreeHeight = m_tree.RowCount();
		CMyCellNew yzCell = null;
		for(int i=0; i<nYZCnt; i++ )
		{
			yzCell = (CMyCellNew)m_tree.leafcells.get(i);
			yzCell.nRowSpan = nTreeHeight - yzCell.nRow + 1;
		}
		
		return true;
	}

	public String getTableHTML( ArrayList  scoreList, boolean bReadOnly, String gradeChange )

	{
		CMyRowNew row = null;

		CMyCellNew cell = null;

		int nCells = 0;
//		
		CMyRowNew first_row = null;
		CMyCellNew first_cell = null;
		int first_nCells = 0;

		int nRows = m_tree.RowCount();

		String sTable = "";
		
		String oneTickChange = "";
		Map changeMap = null;
		if( gradeChange != null && !gradeChange.equalsIgnoreCase("") )
		{
			changeMap = new HashMap();
			String[] changes = gradeChange.split(";");
			
			for( int i = 0; i < changes.length; i++ )
			{
				if( i == 0 )
				{
					oneTickChange = changes[0];
					if( oneTickChange.equalsIgnoreCase("1") )
					{
						oneTickChange = "<font color=red><b>(changed)</b></font>";
					}
					else
						oneTickChange = "";
				}
				else
				{
					String content = changes[i];
					String[] valuse = content.split(":");
					String key = valuse[0];
					String value = valuse[1];
					if( value != null && !value.equals("") && key != null && !key.equals("") )
						changeMap.put(key, value);
				}
			}
		}
//-------------------------------------------------------------------
		if( nRows > 0 )
		{
//--------------------------------------------------------------------------------------------------------------			
			boolean bOneTicket = false;
			String sProperty = "";
			String sOneticket = "";
			String sOnetkremark = "";
			
			
//----------------------------------------------------------------------------------------------------------
			
			
			sTable += "<tr align=middle >";
			sTable += "<td align=middle>";
					
			first_row = (CMyRowNew)m_tree.rows.get(0);
					
			first_nCells = first_row.CellCount();
			
			ArrayList leafCells = GetLeafCells();
			
			for(int k=0; k<first_nCells; k++)
			{			
				first_cell = (CMyCellNew)first_row.cells.get(k);
				
				sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
						
			//--
				for(int i=0; i<nRows; i++)
				{
					sTable += "<tr align=middle >";
					row = (CMyRowNew)m_tree.rows.get(i);
					nCells = row.CellCount();
					for(int j=0; j<nCells; j++)
					{
						cell = (CMyCellNew)row.cells.get(j);
						if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;

						sTable += "<td   align=middle class='tdcolour1' rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + 
						cell.nColSpan + "\" " + sTdProp + " >" + cell.DESCRIPTION + "("+cell.perent+")"+"</td>";			
					}
					sTable += "</tr>";
				}	
			//--	
				
				sTable += "<tr align=middle >";
				for(int h=0; h<leafCells.size(); h++)
				{
					cell = (CMyCellNew)leafCells.get(h);
					if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;
										
					StringBuffer bufRemark = new StringBuffer("");
					String range = WorkPublicDAO.getPfxMinMaxValue( m_conn, cell.ID, bufRemark );
					
					String title = "";
					String valueRemark = bufRemark.toString();
					if( !valueRemark.equalsIgnoreCase("") )
						title = " title='" + valueRemark + "' ";
//					System.out.println("getPfx1Pfx2MinMaxValue start++++++++++++++");
					ArrayList rangeList = WorkPublicDAO.getPfx1Pfx2MinMaxValue( m_conn, cell.ID, bufRemark );
//					System.out.println("getPfx1Pfx2MinMaxValue end++++++++++++++");
					//HashSet set = new HashSet(rangeList);       
					//rangeList.clear();       
					//rangeList.addAll(set); 
					
					
					
					sTable += "<td  valign=top align=center class=tdcolour2 rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + cell.nColSpan + "\" >"+

	                    "<input class=tdcolour2 style='width:100%;vertical-align:middle;' size='15' maxlength='5' name=\"pfx_" + cell.ID+ "\" value=" + range+"("+cell.reference_value+")"+" readonly=\"true\"  id=\"nnnn\">"+" <br>";

					 if(scoreList != null && scoreList.size() == leafCells.size()){
	                    	if( bReadOnly ){
	                    		sTable += "<select "+ title +"  style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h) + " readonly=\"true\" >";
	                    		for(int n=0;n<rangeList.size();n++){
	                    			sTable +="<option value='"+rangeList.get(n)+"'>"+rangeList.get(n)+"</option>";
	                    		}
	                    		sTable += "</select>";
	                    	}else{
	                    			sTable += "<select " + title +"  style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h) + " >" ;
	                    			for(int n=0;n<rangeList.size();n++){
		                    			sTable +="<option value='"+rangeList.get(n)+"'>"+rangeList.get(n)+"</option>";
		                    		}
		                    		sTable += "</select>";
	                    		}
	                    }else{
	                    	if( bReadOnly ){
	                    		sTable += "<select "+ title + "  style='vertical-align:middle;' readonly='true' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\"  value=" +" >" ;
	                    		for(int n=0;n<rangeList.size();n++){
	                    			sTable +="<option value='"+rangeList.get(n)+"'>"+rangeList.get(n)+"</option>";
	                    		}
	                    		sTable += "</select>";
	                    	}else{	
	                    		sTable += "<select "+ title +"  style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\" value="+" >" ;
	                    		for(int n=0;n<rangeList.size();n++){
	                    			sTable +="<option value='"+rangeList.get(n)+"'>"+rangeList.get(n)+"</option>";
	                    		}
	                    		sTable += "</select>";
	                    	}
	                    }
                    	if( changeMap != null )
                    	{
	                    	Integer iId = new Integer(cell.ID);
	                    	String value = (String)changeMap.get( iId.toString() );
	                    	if( value != null && !value.equals("") )
	                    		sTable+="<font color=red><b>("+value+")</b></font>";	                    		
                    	}	                    
						sTable += "</td>";
				}

				sTable += "</tr>";				
				//--					
				
				sTable += "</table>";	
				sTable += "<br/>";
			};
			sTable += "</td>";
			sTable += "</tr>";
		}
		return sTable;
	}	
	
	public ArrayList GetLeafCells()
	{
		return m_tree.leafcells;
	}
	
	public String getTableHTMLMap( ArrayList  scoreList,  boolean bReadOnly, String gradeChange,HashMap map )

	{
		CMyRowNew row = null;

		CMyCellNew cell = null;

		int nCells = 0;
//		
		CMyRowNew first_row = null;
		CMyCellNew first_cell = null;
		int first_nCells = 0;

		int nRows = m_tree.RowCount();

		String sTable = "";
		
		String oneTickChange = "";
		Map changeMap = null;
		if( gradeChange != null && !gradeChange.equalsIgnoreCase("") )
		{
			changeMap = new HashMap();
			String[] changes = gradeChange.split(";");
			
			for( int i = 0; i < changes.length; i++ )
			{
				if( i == 0 )
				{
					oneTickChange = changes[0];
					if( oneTickChange.equalsIgnoreCase("1") )
					{
						oneTickChange = "<font color=red><b>(changed)</b></font>";
					}
					else
						oneTickChange = "";
				}
				else
				{
					String content = changes[i];
					String[] valuse = content.split(":");
					String key = valuse[0];
					String value = valuse[1];
					if( value != null && !value.equals("") && key != null && !key.equals("") )
						changeMap.put(key, value);
				}
			}
		}
//-------------------------------------------------------------------
		if( nRows > 0 )
		{
//--------------------------------------------------------------------------------------------------------------			
			boolean bOneTicket = false;
			String sProperty = "";
			String sOneticket = "";
			String sOnetkremark = "";
			
			
			
//----------------------------------------------------------------------------------------------------------
			
			
			sTable += "<tr align=middle >";
			sTable += "<td align=middle>";
					
			first_row = (CMyRowNew)m_tree.rows.get(0);
					
			first_nCells = first_row.CellCount();
			
			ArrayList leafCells = GetLeafCells();
			
			for(int k=0; k<first_nCells; k++)
			{			
				first_cell = (CMyCellNew)first_row.cells.get(k);
				
				sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
						
			//--
				for(int i=0; i<nRows; i++)
				{
					sTable += "<tr align=middle >";
					row = (CMyRowNew)m_tree.rows.get(i);
					nCells = row.CellCount();
					for(int j=0; j<nCells; j++)
					{
						cell = (CMyCellNew)row.cells.get(j);
						if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;

						sTable += "<td   align=middle class='tdcolour1' rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + 
						cell.nColSpan + "\" " + sTdProp + " >" + cell.DESCRIPTION + "("+cell.perent+")"+"</td>";			
					}
					sTable += "</tr>";
				}	
			//--	
				
				sTable += "<tr align=middle >";
				for(int h=0; h<leafCells.size(); h++)
				{
					cell = (CMyCellNew)leafCells.get(h);
					if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;
										
					StringBuffer bufRemark = new StringBuffer("");
					String range = WorkPublicDAO.getPfxMinMaxValue( m_conn, cell.ID, bufRemark );
					
					String title = "";
					String valueRemark = bufRemark.toString();
					if( !valueRemark.equalsIgnoreCase("") )
						title = " title='" + valueRemark + "' ";
//					System.out.println("getPfx1Pfx2MinMaxValue start -----------------------");
					ArrayList rangeList = WorkPublicDAO.getPfx1Pfx2MinMaxValue( m_conn, cell.ID, bufRemark );
//					System.out.println("getPfx1Pfx2MinMaxValue end -----------------------");
					//HashSet set = new HashSet(rangeList);       
					//rangeList.clear();       
					//rangeList.addAll(set); 
			//		Iterator iter = map.entrySet().iterator(); 
					
					Set key = map.keySet();     //通过keySet()方法 取出所有的key
					Object[] keys = key.toArray();     
					
					sTable += "<td  valign=top align=center class=tdcolour2 rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + cell.nColSpan + "\" >"+

	                    "<input class=tdcolour2 style='width:100%;vertical-align:middle;' size='15' maxlength='5' name=\"pfx_" + cell.ID+ "\" value=" + range+"("+cell.reference_value+")"+" readonly=\"true\"  id=\"nnnn\">"+" <br>";
					
					 if(scoreList != null && scoreList.size() == leafCells.size()){
	                    	if( bReadOnly ){
	                    		sTable += "<select "+ title +"  style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h) + " readonly=\"true\" >";
	                    		for(int n=0;n<rangeList.size();n++){
	                    			for (int i = 0; i < keys.length; i++) { 
                    					String ke = (String)keys[i];      // 通过下标取出数组里的key值,用k去接收取出的key值
                    					String val = (String)map.get(ke); 
	            					    if(ke.equals(cell.ID+"_"+h) && val.equals(rangeList.get(n)+"")){
			                    			sTable +="<option value='"+rangeList.get(n)+"' selected>"+rangeList.get(n)+"</option>";
			                    			break;
	            					    }else if((i+1) == keys.length){
			                    			sTable +="<option value='"+rangeList.get(n)+"'>"+rangeList.get(n)+"</option>";
			                    			break;
			                    		}
	                    		    }
	                    		}
	                    		sTable += "</select>";
	                    	}else{
	                    			sTable += "<select " + title +"  style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h) + " >" ;
	                    			for(int n=0;n<rangeList.size();n++){
	                    				for (int i = 0; i < keys.length; i++) { 
	                    					String ke = (String)keys[i];      // 通过下标取出数组里的key值,用k去接收取出的key值
	                    					String val = (String)map.get(ke); 
		            					    if(ke.equals(cell.ID+"_"+h) && val.equals(rangeList.get(n)+"")){
				                    			sTable +="<option value='"+rangeList.get(n)+"' selected>"+rangeList.get(n)+"</option>";
				                    			break;
		            					    }else if((i+1) == keys.length){
				                    			sTable +="<option value='"+rangeList.get(n)+"'>"+rangeList.get(n)+"</option>";
				                    			break;
				                    		}
		                    		    }
		                    		}
	                    		}
	                    }else{
	                    	if( bReadOnly ){
	                    		sTable += "<select "+ title + "  style='vertical-align:middle;' readonly='true' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\"  value=" +" >" ;
	                    		for(int n=0;n<rangeList.size();n++){
	                    			sTable +="<option value='"+rangeList.get(n)+"'>"+rangeList.get(n)+"</option>";
	                    		}
	                    		sTable += "</select>";
	                    	}else{	
	                    		sTable += "<select "+ title +"  style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\" value="+" >" ;
	                    		for(int n=0;n<rangeList.size();n++){
	                    			sTable +="<option value='"+rangeList.get(n)+"'>"+rangeList.get(n)+"</option>";
	                    		}
	                    		sTable += "</select>";
	                    	}
	                    }
					
                    	if( changeMap != null )
                    	{
	                    	Integer iId = new Integer(cell.ID);
	                    	String value = (String)changeMap.get( iId.toString() );
	                    	if( value != null && !value.equals("") )
	                    		sTable+="<font color=red><b>("+value+")</b></font>";	                    		
                    	}	                    
						sTable += "</td>";
				}

				sTable += "</tr>";				
				//--					
				
				sTable += "</table>";	
				sTable += "<br/>";
			};
			sTable += "</td>";
			sTable += "</tr>";
		}
		return sTable;
	}
	
	//最新的
	public String getTableDictHTML(  boolean bReadOnly )

	{
		CMyRowNew row = null;

		CMyCellNew cell = null;

		int nCells = 0;
//		
		CMyRowNew first_row = null;
		CMyCellNew first_cell = null;
		int first_nCells = 0;

		int nRows = m_tree.RowCount();

		String sTable = "";

//-------------------------------------------------------------------
		if( nRows > 0 )
		{
			sTable += "<tr align=middle >";
			sTable += "<td align=middle>";
					
			first_row = (CMyRowNew)m_tree.rows.get(0);
					
			first_nCells = first_row.CellCount();
			
			ArrayList leafCells = GetLeafCells();
			
			for(int k=0; k<first_nCells; k++)
			{			
				first_cell = (CMyCellNew)first_row.cells.get(k);
				
				sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
						
			//--
				for(int i=0; i<nRows; i++)
				{
					sTable += "<tr align=middle >";
					row = (CMyRowNew)m_tree.rows.get(i);
					nCells = row.CellCount();
					for(int j=0; j<nCells; j++)
					{
						cell = (CMyCellNew)row.cells.get(j);
						if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;

						sTable += "<td   align=middle class='tdcolour1' rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + 
						cell.nColSpan + "\" " + sTdProp + " >" + cell.DESCRIPTION + "("+cell.perent+"%)"+ "</td>";			
					}
					sTable += "</tr>";
				}	
			//--	
				
				sTable += "<tr align=middle >";
				for(int h=0; h<leafCells.size(); h++)
				{
					cell = (CMyCellNew)leafCells.get(h);
					if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;
					
					StringBuffer bufRemark = new StringBuffer("");
					String range = WorkPublicDAO.getPfxMinMaxValue( m_conn, cell.ID, bufRemark );
					
					String title = "";
					String valueRemark = bufRemark.toString();
					if( !valueRemark.equalsIgnoreCase("") )
						title = " title='" + valueRemark + "' ";
					
					sTable += "<td valign=top align=center class=tdcolour2 rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + cell.nColSpan + "\" >"+

	                    "<input "+title+" class=tdcolour2 style='width:100%;vertical-align:middle;' size='15' maxlength='5' name=\"pfx_" + cell.ID+ "\" value=" + range+"("+cell.reference_value+")"+" readonly=\"true\" >";

					//String rangePercnet = WorkPublicDAO.getPfxPercentValue( m_conn, cell.ID );
					//sTable += "<input "+title+" class=tdcolour2 size='10' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" + rangePercnet+"%" + " readonly=\"true\" >" ;
	                sTable += "</td>";

				}

				sTable += "</tr>";				
					
					
				//--					
				
				sTable += "</table>";	
				sTable += "<br/>";
			};
			sTable += "</td>";
			sTable += "</tr>";
		}
		return sTable;
	}	
}

public class MyTableNew 
{
	
	public String MyTestFuncByMap(String grade_index, Map errorValueMap, ArrayList onetkList )
	 throws SQLException
	{
		Connection conn = null;
		try
		{ 
		 	//conn = Utility.getConnection();
			conn = DAOTools.getConnectionOutS("ZQC");
			CMyTableNew table = new CMyTableNew(conn,grade_index);
			table.PrcessTree();
			
			 ArrayList scoreList = new ArrayList();
			 for(int i = 0; i < 100; i ++){
                String pfx_id = "";
                if(i < 10)
                    pfx_id = "pfx_00"+i;
                else
                    pfx_id = "pfx_0" +i;
                
                String pfx = (String) errorValueMap.get(pfx_id);
                if( pfx == null )
                    break;
                scoreList.add(pfx);
            }

			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getTableHTML( scoreList, false, "" );
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "出错了";
		} 
		finally
		{	
			if( conn != null )
				DAOTools.releaseConnectionOutS("ZQC", conn);
		}		
    }
	
	public String MyTestFuncByHashMap(String grade_index, Map errorValueMap,HashMap map )
	 throws SQLException
	{
		Connection conn = null;
		try
		{ 
			conn = DAOTools.getConnectionOutS("ZQC");
			CMyTableNew table = new CMyTableNew(conn,grade_index);
			table.PrcessTree();
			
			 ArrayList scoreList = new ArrayList();
			 for(int i = 0; i < 100; i ++){
               String pfx_id = "";
               if(i < 10)
                   pfx_id = "pfx_00"+i;
               else
                   pfx_id = "pfx_0" +i;
              String pfx = (String) errorValueMap.get(pfx_id);
	               if( pfx == null )
	            	   continue;
	                   //break;
	               scoreList.add(pfx);
              // }
           }

			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getTableHTMLMap( scoreList, false, "" ,map);
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "出错了";
		} 
		finally
		{	
			if( conn != null )
				DAOTools.releaseConnectionOutS("ZQC", conn);
		}		
   }
	public String MyTestFuncByHashMapNew(String grade_index, Map errorValueMap,HashMap map )
	 throws SQLException
	{
		Connection conn = null;
		try
		{ 
			conn = DAOTools.getConnectionOutS("ZQC");
			CMyTableNew table = new CMyTableNew(conn,grade_index);
			table.PrcessTree();
			
			 ArrayList scoreList = new ArrayList();
			 for(int i = 0; i < 1000; i ++){
              String pfx="";
              for(int x =0;x<10;x++){
           	   if(i < 10)
           		   pfx = (String) errorValueMap.get("00"+i+"_"+x);
                  else if (i<100 && i>9)
               	   pfx = (String) errorValueMap.get("0"+i+"_"+x);
                  else 
                   pfx = (String) errorValueMap.get(i+"_"+x);
                      //pfx_id = "0" +i+"_"+x;
               	  // pfx_id = i+"_"+x;
	               if( pfx == null )
	            	   continue;
	                   //break;
	               scoreList.add(pfx);
              }
          }

			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getTableHTMLMap( scoreList, false, "" ,map);
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "出错了";
		} 
		finally
		{	
			if( conn != null )
				DAOTools.releaseConnectionOutS("ZQC", conn);
		}		
  }
	public String MyTestFuncByMapDict(String grade_index, Map errorValueMap )throws SQLException
	{
		Connection conn = null;
		try
		{ 
			conn = DAOTools.getConnectionOutS("ZQC");
			CMyTableNew table = new CMyTableNew(conn,grade_index);
			table.PrcessTreeDict();
			
			 ArrayList scoreList = new ArrayList();
			 for(int i = 0; i < 100; i ++){
                String pfx_id = "";
                if(i < 10)
                    pfx_id = i+"";
                else
                    pfx_id = i+"";
                
                String pfx = (String) errorValueMap.get(pfx_id);
                if( pfx == null )
                    break;
                scoreList.add(pfx);
            }

			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getTableDictHTML( false );
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			//System.out.println("MyTestFuncByMapDict");
			return "出错了";
		} 
		finally
		{	
			if( conn != null )
				DAOTools.releaseConnectionOutS("ZQC", conn);
		}
    }
	public ArrayList getLeafIds( String grade_index )
	 throws SQLException
	{
		Connection conn = null;
		ArrayList leafList = new ArrayList();
		try
		{ 
			conn = DAOTools.getConnectionOutS("ZQC");
			CMyTableNew table = new CMyTableNew(conn,grade_index);
			table.PrcessTree();
			ArrayList leafCells = table.GetLeafCells();
			for(int h=0; h<leafCells.size(); h++)
			{
				CMyCellNew cell = (CMyCellNew)leafCells.get(h);
				Integer iID = new Integer(cell.ID);
				leafList.add(iID);				
			}			
		} 
		catch(Exception ex)
		{ 
			return leafList;
		} 
		finally
		{	
			if( conn != null )
				DAOTools.releaseConnectionOutS("ZQC", conn);
		}	
		return leafList;
   }	

	public ArrayList MyTestLeafs( String grade_index )
	throws SQLException
	{
    	Connection conn = null;
    	ArrayList list = new ArrayList();
		try
		{ 
			conn = DAOTools.getConnectionOutS("ZQC");
			CMyTableNew table = new CMyTableNew(conn,grade_index);
			table.PrcessTree();            
			ArrayList tmplist = table.GetLeafCells();
			for( int i = 0; i < tmplist.size(); i++ )
			{
				CMyCellNew cell = (CMyCellNew)tmplist.get(i);
				if( cell.isLeaf )
				{
					Integer it = new Integer(cell.ID);
					list.add(it);
				}
			}
		} 
		catch(Exception ex)
		{ 
			return list;
		}
		finally
		{	
			if( conn != null )
				DAOTools.releaseConnectionOutS("ZQC", conn);
		}		
		return list;
	}
	
    public  String MyTestFunc(String grade_index, ArrayList scoreList)//String grade_id, String qcUser, int planid, int gradestep)
    throws SQLException
    {
    	Connection conn = null;
		try
		{ 
			conn = DAOTools.getConnectionOutS("ZQC");
			CMyTableNew table = new CMyTableNew(conn,grade_index);
			table.PrcessTree();            
			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getTableHTML( scoreList, false, "" );
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "出错了";
		}
		finally
		{	
			if( conn != null )
				DAOTools.releaseConnectionOutS("ZQC", conn);
		}
    }
    
    public  String MyTestFuncView(String grade_index, ArrayList scoreList, ArrayList onetkList )//String grade_id,String qcUser, int planid, int gradestep)
    throws SQLException
    {
    	Connection conn = null;
		try
		{ 
			conn = DAOTools.getConnectionOutS("ZQC"); 
			CMyTableNew table = new CMyTableNew(conn,grade_index);
			table.PrcessTree();            
			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getTableHTML( scoreList, true, "" );
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "出错了";
		} 
		finally
		{	
			if( conn != null )
				DAOTools.releaseConnectionOutS("ZQC", conn);
		}		
    }	  

    public  String MyTestFuncView2(String grade_index, ArrayList scoreList, ArrayList onetkList, String gradeChange )//String grade_id,String qcUser, int planid, int gradestep)
    throws SQLException
    {
    	Connection conn = null;
		try
		{ 
			conn = DAOTools.getConnectionOutS("ZQC"); 
			CMyTableNew table = new CMyTableNew(conn,grade_index);
			table.PrcessTree();            
			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getTableHTML( scoreList,true, gradeChange );
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "出错了";
		} 
		finally
		{	
			if( conn != null )
				DAOTools.releaseConnectionOutS("ZQC", conn);
		}		
    }	 
    
	 public  String MyTestDictView(String grade_index,String qcUser, String gradestep)throws SQLException
	    {
		 Connection conn = null;
			try
			{ 
				conn = DAOTools.getConnectionOutS("ZQC"); 
				CMyTableNew table = new CMyTableNew(conn,grade_index);
				table.PrcessTreeDict();
	            //ArrayList scoreList = WorkPublicDAO.queryDictBrowse(conn,grade_index,qcUser,gradestep); 
				table.sTdProp = "bgcolor=\"buttonface\" align=center";
				String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
				sTable += table.getTableDictHTML(  true );
				sTable += "</table>";
				return sTable;
			} 
			catch(Exception ex)
			{ 
				//System.out.println("MyTestDictView");
				return "出错了";			
			} 
			finally
			{	
				if( conn != null )
					DAOTools.releaseConnectionOutS("ZQC", conn);
			}
	    }	 
}
/* End of class HelloWorld */

