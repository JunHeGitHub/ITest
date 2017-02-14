package com.zinglabs.work;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

import com.zinglabs.util.*;

class CMyCellRecord
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

class CMyRowRecord
{
	public ArrayList cells;
	CMyRowRecord()
	{
		cells = new ArrayList();
	}
	
	public CMyCellRecord AppendCell()
	{
		CMyCellRecord cell = new CMyCellRecord();
		cell.ID = 100;
		cells.add(cell);
		return cell;
	}
	
	public int CellCount()
	{
		return cells.size();
	}
	
}

class CMyTreeRecord
{
	public ArrayList leafcells;
	public ArrayList rows;
	CMyTreeRecord()
	{
		leafcells = new ArrayList();
		rows = new ArrayList();
	}
	
	public CMyRowRecord AppendRow()
	{
		CMyRowRecord row = new CMyRowRecord();
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

class CMyTableRecord
{
	private Connection m_conn;
	private CMyTreeRecord m_tree;
	public String sBeforeRow = "";
	public String sAfterRow = "";
	public String sTdProp = ""; 
    public String gradeIndex = "0";

	CMyTableRecord(Connection conn, String index)
	{
		m_conn = conn;
        gradeIndex = index;
		m_tree = new CMyTreeRecord();
	}
	
	private int ProcessCell(int iRow, int PARENTID,String gradeIndex)
	{
		try {
			String sSql = "SELECT ID,DESCRIPTION,reference_value,IDPATH,percent FROM dictinfo where PARENTID=" + PARENTID+" and grade_index='"+gradeIndex+"'";
	  		Statement stmt = m_conn.createStatement(); 
	  		ResultSet rs = stmt.executeQuery(sSql); 
			CMyRowRecord row = null;
			int nCols = 0;
		  	while(rs.next())
		  	{ 
		  		if( null == row )
		  		{
					if( iRow > m_tree.RowCount() )
						row = m_tree.AppendRow();
					else
						row = (CMyRowRecord)m_tree.rows.get(iRow-1);
		  		}
		  		
		  		CMyCellRecord cell = row.AppendCell();
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
			CMyRowRecord row = null;
			int nCols = 0;
		  	while(rs.next())
		  	{ 
		  		if( null == row )
		  		{
					if( iRow > m_tree.RowCount() )
						row = m_tree.AppendRow();
					else
						row = (CMyRowRecord)m_tree.rows.get(iRow-1);
		  		}
		  		
		  		CMyCellRecord cell = row.AppendCell();
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
		CMyCellRecord yzCell = null;
		for(int i=0; i<nYZCnt; i++ )
		{
			yzCell = (CMyCellRecord)m_tree.leafcells.get(i);
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
		CMyCellRecord yzCell = null;
		for(int i=0; i<nYZCnt; i++ )
		{
			yzCell = (CMyCellRecord)m_tree.leafcells.get(i);
			yzCell.nRowSpan = nTreeHeight - yzCell.nRow + 1;
		}
		
		return true;
	}
/*	
	public String getTableHTML( ArrayList  scoreList, ArrayList onetkList, boolean bReadOnly )

	{
		CMyRow row = null;

		CMyCell cell = null;

		int nCells = 0;
//		
		CMyRow first_row = null;
		CMyCell first_cell = null;
		int first_nCells = 0;

		int nRows = m_tree.RowCount();

		String sTable = "";

//-------------------------------------------------------------------
		if( nRows > 0 )
		{
//--------------------------------------------------------------------------------------------------------------			
			boolean bOneTicket = false;
			String sProperty = "";
			String sOneticket = "";
			String sOnetkremark = "";
			if( onetkList.size() == 4 )
			{
				String content = (String)onetkList.get( 0 );
				if( content != null )
					bOneTicket = content.equalsIgnoreCase("1") ? true : false;	
				content = (String)onetkList.get( 1 );
				if( content != null )
					sOnetkremark = content;	
				content = (String)onetkList.get( 2 );
				if( content != null )
					sProperty = content;		
				content = (String)onetkList.get( 3 );
				if( content != null )
					sOneticket = content;						
			}
			
			sTable += "<tr align=left >";
			sTable += "<td align=left >";			
			sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += "<tr align=left>";
			sTable += "<td align=left class='tdcolour1'>";
			sTable += sProperty;
			sTable += "</td>";				
			sTable += "<td align=left class='tdcolour2'>";
			
			String readonlyp = "";
			if( bReadOnly )
				readonlyp = " disabled ";
			if( bOneTicket )
				sTable += "<input title='"+sOnetkremark+"' type='checkbox' checked name='oneticket' "+readonlyp+" />";
			else
				sTable += "<input title='"+sOnetkremark+"' type='checkbox' name='oneticket' "+readonlyp+" />";
			
			sTable += sOneticket;			
			sTable += "</td>";						
			sTable += "</tr>";	   
			sTable += "</table>";
			sTable += "</td>";						
			sTable += "</tr>";	   			
//----------------------------------------------------------------------------------------------------------
			
			
			sTable += "<tr align=middle >";
			sTable += "<td align=middle>";
					
			first_row = (CMyRow)m_tree.rows.get(0);
					
			first_nCells = first_row.CellCount();
			
			ArrayList leafCells = GetLeafCells();
			
			for(int k=0; k<first_nCells; k++)
			{			
				first_cell = (CMyCell)first_row.cells.get(k);
				
				sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
						
			//--
				for(int i=0; i<nRows; i++)
				{
					sTable += "<tr align=middle >";
					row = (CMyRow)m_tree.rows.get(i);
					nCells = row.CellCount();
					for(int j=0; j<nCells; j++)
					{
						cell = (CMyCell)row.cells.get(j);
						if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;

						sTable += "<td   align=middle class='tdcolour1' rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + 
						cell.nColSpan + "\" " + sTdProp + " >" + cell.DESCRIPTION + "("+cell.perent+"%)"+"</td>";			
					}
					sTable += "</tr>";
				}	
			//--	
				
				sTable += "<tr align=middle >";
				for(int h=0; h<leafCells.size(); h++)
				{
					cell = (CMyCell)leafCells.get(h);
					if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;
					
					StringBuffer bufRemark = new StringBuffer("");
					String range = WorkPublicDAO.getPfxMinMaxValue( m_conn, cell.ID, bufRemark );
					
					String title = "";
					String valueRemark = bufRemark.toString();
					if( !valueRemark.equalsIgnoreCase("") )
						title = " title='" + valueRemark + "' ";
					
					sTable += "<td  valign=top align=center class=tdcolour2 rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + cell.nColSpan + "\" >"+

	                    "<input class=tdcolour2 style='width:100%;vertical-align:middle;' size='15' maxlength='5' name=\"pfx_" + cell.ID+ "\" value=" + range+"("+cell.reference_value+")"+" readonly=\"true\" >"+" <br>";

	                    if(scoreList != null && scoreList.size() == leafCells.size()){
	                    	if( bReadOnly )
	                    		sTable += "<input "+ title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h) + " readonly=\"true\" >" ;
	                    	else
	                    		sTable += "<input " + title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h) + " >" ;
	                    }else{
	                    	if( bReadOnly )
	                    		sTable += "<input "+ title + " size='10' style='vertical-align:middle;' readonly='true' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\"  value=" +" >" ;
	                    	else	
	                    		sTable += "<input "+ title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\" value="+" >" ;

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
	*/
	public String getTableHTML( ArrayList  scoreList, boolean bReadOnly, String gradeChange )

	{
		CMyRowRecord row = null;

		CMyCellRecord cell = null;

		int nCells = 0;
//		
		CMyRowRecord first_row = null;
		CMyCellRecord first_cell = null;
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
			/*if( onetkList.size() == 4 )
			{
				String content = (String)onetkList.get( 0 );
				if( content != null )
					bOneTicket = content.equalsIgnoreCase("1") ? true : false;	
				content = (String)onetkList.get( 1 );
				if( content != null )
					sOnetkremark = content;	
				content = (String)onetkList.get( 2 );
				if( content != null )
					sProperty = content;		
				content = (String)onetkList.get( 3 );
				if( content != null )
					sOneticket = content;						
			}*/
			
			sTable += "<tr align=left >";
			sTable += "<td align=left >";			
			sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += "<tr align=left>";
			sTable += "<td align=left class='tdcolour1'>";
			sTable += sProperty;
			sTable += "</td>";				
			sTable += "<td align=left class='tdcolour2'>";
			
			String readonlyp = "";
			if( bReadOnly )
				readonlyp = " disabled ";
			/*if( bOneTicket )
				sTable += "<input title='"+sOnetkremark+"' type='checkbox' checked name='oneticket' "+readonlyp+" />";
			else
				sTable += "<input title='"+sOnetkremark+"' type='checkbox' name='oneticket' "+readonlyp+" />";*/
			
			sTable += sOneticket;
			sTable += oneTickChange;
			sTable += "</td>";						
			sTable += "</tr>";	   
			sTable += "</table>";
			sTable += "</td>";						
			sTable += "</tr>";	   			
//----------------------------------------------------------------------------------------------------------
			
			
			sTable += "<tr align=middle >";
			sTable += "<td align=middle>";
					
			first_row = (CMyRowRecord)m_tree.rows.get(0);
					
			first_nCells = first_row.CellCount();
			
			ArrayList leafCells = GetLeafCells();
			
			for(int k=0; k<first_nCells; k++)
			{			
				first_cell = (CMyCellRecord)first_row.cells.get(k);
				
				sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
						
			//--
				for(int i=0; i<nRows; i++)
				{
					sTable += "<tr align=middle >";
					row = (CMyRowRecord)m_tree.rows.get(i);
					nCells = row.CellCount();
					for(int j=0; j<nCells; j++)
					{
						cell = (CMyCellRecord)row.cells.get(j);
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
					cell = (CMyCellRecord)leafCells.get(h);
					if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;
										
					StringBuffer bufRemark = new StringBuffer("");
					String range = WorkPublicDAO.getPfxMinMaxValue( m_conn, cell.ID, bufRemark );
					
					String title = "";
					String valueRemark = bufRemark.toString();
					if( !valueRemark.equalsIgnoreCase("") )
						title = " title='" + valueRemark + "' ";
					
					sTable += "<td  valign=top align=center class=tdcolour2 rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + cell.nColSpan + "\" >"+

	                    "<input class=tdcolour2 style='width:100%;vertical-align:middle;' size='15' maxlength='5' name=\"pfx_" + cell.ID+ "\" value=" + range+"("+cell.reference_value+")"+" readonly=\"true\" >"+" <br>";

	                    if(scoreList != null && scoreList.size() == leafCells.size()){
	                    	if( bReadOnly )
	                    		sTable += "<input "+ title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h) + " readonly=\"true\" >" ;
	                    	else
	                    		sTable += "<input " + title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h)  +" >";
	                    }else{
	                    	if( bReadOnly )
	                    		sTable += "<input "+ title + " size='10' style='vertical-align:middle;' readonly='true' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\"  value=\"0\"" +" readonly=\"true\" >"+" <br>"; 
	                    	else	
	                    		sTable += "<input "+ title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\" value=\"0\""+" readonly=\"true\" >"+" <br>"; 
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
	
	public String getAdminAgentTableHTML( ArrayList  scoreList, boolean bReadOnly, String gradeChange )

	{
		CMyRowRecord row = null;

		CMyCellRecord cell = null;

		int nCells = 0;
//		
		CMyRowRecord first_row = null;
		CMyCellRecord first_cell = null;
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
			/*if( onetkList.size() == 4 )
			{
				String content = (String)onetkList.get( 0 );
				if( content != null )
					bOneTicket = content.equalsIgnoreCase("1") ? true : false;	
				content = (String)onetkList.get( 1 );
				if( content != null )
					sOnetkremark = content;	
				content = (String)onetkList.get( 2 );
				if( content != null )
					sProperty = content;		
				content = (String)onetkList.get( 3 );
				if( content != null )
					sOneticket = content;						
			}*/
			
			sTable += "<tr align=left >";
			sTable += "<td align=left >";			
			sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += "<tr align=left>";
			sTable += "<td align=left class='tdcolour1'>";
			sTable += sProperty;
			sTable += "</td>";				
			sTable += "<td align=left class='tdcolour2'>";
			
			String readonlyp = "";
			if( bReadOnly )
				readonlyp = " disabled ";
			/*if( bOneTicket )
				sTable += "<input title='"+sOnetkremark+"' type='checkbox' checked name='oneticket' "+readonlyp+" />";
			else
				sTable += "<input title='"+sOnetkremark+"' type='checkbox' name='oneticket' "+readonlyp+" />";*/
			
			sTable += sOneticket;
			sTable += oneTickChange;
			sTable += "</td>";						
			sTable += "</tr>";	   
			sTable += "</table>";
			sTable += "</td>";						
			sTable += "</tr>";	   			
//----------------------------------------------------------------------------------------------------------
			
			
			sTable += "<tr align=middle >";
			sTable += "<td align=middle>";
					
			first_row = (CMyRowRecord)m_tree.rows.get(0);
					
			first_nCells = first_row.CellCount();
			
			ArrayList leafCells = GetLeafCells();
			
			for(int k=0; k<first_nCells; k++)
			{			
				first_cell = (CMyCellRecord)first_row.cells.get(k);
				
				sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
						
			//--
				for(int i=0; i<nRows; i++)
				{
					sTable += "<tr align=middle >";
					row = (CMyRowRecord)m_tree.rows.get(i);
					nCells = row.CellCount();
					for(int j=0; j<nCells; j++)
					{
						cell = (CMyCellRecord)row.cells.get(j);
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
					cell = (CMyCellRecord)leafCells.get(h);
					if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;
										
					StringBuffer bufRemark = new StringBuffer("");
					String range = WorkPublicDAO.getPfxMinMaxValue( m_conn, cell.ID, bufRemark );
					
					String title = "";
					String valueRemark = bufRemark.toString();
					if( !valueRemark.equalsIgnoreCase("") )
						title = " title='" + valueRemark + "' ";
					
					sTable += "<td  valign=top align=center class=tdcolour2 rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + cell.nColSpan + "\" >"+

	                    "<input class=tdcolour2 style='width:100%;vertical-align:middle;' size='15' maxlength='5' name=\"pfx_" + cell.ID+ "\" value=" + range+"("+cell.reference_value+")>"+" <br>";

	                    if(scoreList != null && scoreList.size() == leafCells.size()){
	                    	if( bReadOnly )
	                    		sTable += "<input "+ title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h) + " >" ;
	                    	else
	                    		sTable += "<input " + title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h)  +" >";
	                    }else{
	                    	if( bReadOnly )
	                    		sTable += "<input "+ title + " size='10' style='vertical-align:middle;' readonly='true' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\"  value=" +" >"+" <br>"; 
	                    	else	
	                    		sTable += "<input "+ title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\" value="+" >"+" <br>"; 
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
	
	
	public String getRSTableHTML( ArrayList  scoreList, boolean bReadOnly, String gradeChange )

	{
		CMyRowRecord row = null;

		CMyCellRecord cell = null;

		int nCells = 0;
//		
		CMyRowRecord first_row = null;
		CMyCellRecord first_cell = null;
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
			/*if( onetkList.size() == 4 )
			{
				String content = (String)onetkList.get( 0 );
				if( content != null )
					bOneTicket = content.equalsIgnoreCase("1") ? true : false;	
				content = (String)onetkList.get( 1 );
				if( content != null )
					sOnetkremark = content;	
				content = (String)onetkList.get( 2 );
				if( content != null )
					sProperty = content;		
				content = (String)onetkList.get( 3 );
				if( content != null )
					sOneticket = content;						
			}*/
			
			sTable += "<tr align=left >";
			sTable += "<td align=left >";			
			sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += "<tr align=left>";
			sTable += "<td align=left class='tdcolour1'>";
			sTable += sProperty;
			sTable += "</td>";				
			sTable += "<td align=left class='tdcolour2'>";
			
			String readonlyp = "";
			if( bReadOnly )
				readonlyp = " disabled ";
			/*if( bOneTicket )
				sTable += "<input title='"+sOnetkremark+"' type='checkbox' checked name='oneticket' "+readonlyp+" />";
			else
				sTable += "<input title='"+sOnetkremark+"' type='checkbox' name='oneticket' "+readonlyp+" />";*/
			
			sTable += sOneticket;
			sTable += oneTickChange;
			sTable += "</td>";						
			sTable += "</tr>";	   
			sTable += "</table>";
			sTable += "</td>";						
			sTable += "</tr>";	   			
//----------------------------------------------------------------------------------------------------------
			
			
			sTable += "<tr align=middle >";
			sTable += "<td align=middle>";
					
			first_row = (CMyRowRecord)m_tree.rows.get(0);
					
			first_nCells = first_row.CellCount();
			
			ArrayList leafCells = GetLeafCells();
			
			for(int k=0; k<first_nCells; k++)
			{			
				first_cell = (CMyCellRecord)first_row.cells.get(k);
				
				sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
						
			//--
				for(int i=0; i<nRows; i++)
				{
					sTable += "<tr align=middle >";
					row = (CMyRowRecord)m_tree.rows.get(i);
					nCells = row.CellCount();
					for(int j=0; j<nCells; j++)
					{
						cell = (CMyCellRecord)row.cells.get(j);
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
					cell = (CMyCellRecord)leafCells.get(h);
					if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;
										
					StringBuffer bufRemark = new StringBuffer("");
					String range = WorkPublicDAO.getPfxMinMaxValue( m_conn, cell.ID, bufRemark );
					
					String title = "";
					String valueRemark = bufRemark.toString();
					if( !valueRemark.equalsIgnoreCase("") )
						title = " title='" + valueRemark + "' ";
					
					sTable += "<td  valign=top align=center class=tdcolour2 rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + cell.nColSpan + "\" >"+

	                    "<input class=tdcolour2 style='width:100%;vertical-align:middle;' size='15' maxlength='5' name=\"pfx_" + cell.ID+ "\" value=" + range+"("+cell.reference_value+")>"+" <br>";

	                    if(scoreList != null && scoreList.size() == leafCells.size()){
	                    	if( bReadOnly )
	                    		sTable += "<input "+ title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h) + " >" ;
	                    	else
	                    		sTable += "<input " + title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h + ")\" value=" +scoreList.get(h)  +" >";
	                    }else{
	                    	if( bReadOnly )
	                    		sTable += "<input "+ title + " size='10' style='vertical-align:middle;'  maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\"  value=" +" >"+" <br>"; 
	                    	else	
	                    		sTable += "<input "+ title +" size='10' style='vertical-align:middle;' maxlength='5' name=\"value(" + cell.ID + "_" + h +  ")\" value="+" >"+" <br>"; 
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
	
	//���µ�
	public String getTableDictHTML(  boolean bReadOnly )

	{
		CMyRowRecord row = null;

		CMyCellRecord cell = null;

		int nCells = 0;
//		
		CMyRowRecord first_row = null;
		CMyCellRecord first_cell = null;
		int first_nCells = 0;

		int nRows = m_tree.RowCount();

		String sTable = "";

//-------------------------------------------------------------------
		if( nRows > 0 )
		{
			sTable += "<tr align=middle >";
			sTable += "<td align=middle>";
					
			first_row = (CMyRowRecord)m_tree.rows.get(0);
					
			first_nCells = first_row.CellCount();
			
			ArrayList leafCells = GetLeafCells();
			
			for(int k=0; k<first_nCells; k++)
			{			
				first_cell = (CMyCellRecord)first_row.cells.get(k);
				
				sTable += "<table   border=0 cellspacing=1 cellpadding=2>";
						
			//--
				for(int i=0; i<nRows; i++)
				{
					sTable += "<tr align=middle >";
					row = (CMyRowRecord)m_tree.rows.get(i);
					nCells = row.CellCount();
					for(int j=0; j<nCells; j++)
					{
						cell = (CMyCellRecord)row.cells.get(j);
						if( first_cell.TOPPARENTID != cell.TOPPARENTID ) continue;

						sTable += "<td   align=middle class='tdcolour1' rowSpan=\"" + cell.nRowSpan + "\" colSpan=\"" + 
						cell.nColSpan + "\" " + sTdProp + " >" + cell.DESCRIPTION + "("+cell.perent+")"+ "</td>";			
					}
					sTable += "</tr>";
				}	
			//--	
				
				sTable += "<tr align=middle >";
				for(int h=0; h<leafCells.size(); h++)
				{
					cell = (CMyCellRecord)leafCells.get(h);
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

public class MyTableRecord 
{
	public String MyTestFuncByMap(String grade_index, Map errorValueMap )
	 throws SQLException
	{
		Connection conn = null;
		try
		{ 
		 	conn = Utility.getConnection();
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
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
			return "�����";
		} 
		finally
		{	
			if( conn != null )
				conn.close();
		}		
    }
	
	public String AdminAgentMyTestFuncByMap(String grade_index, Map errorValueMap )
	 throws SQLException
	{
		Connection conn = null;
		try
		{ 
		 	conn = Utility.getConnection();
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
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
			return "�����";
		} 
		finally
		{	
			if( conn != null )
				conn.close();
		}		
   }
	
	
	public String RSMyTestFuncByMap(String grade_index, Map errorValueMap )
	 throws SQLException
	{
		Connection conn = null;
		try
		{ 
		 	conn = Utility.getConnection();
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
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
			return "�����";
		} 
		finally
		{	
			if( conn != null )
				conn.close();
		}		
  }
	
	public String MyTestFuncByMapDict(String grade_index, Map errorValueMap )throws SQLException
	{
		Connection conn = null;
		try
		{ 
			conn = Utility.getConnection();
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
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
			return "�����";
		} 
		finally
		{	
			if( conn != null )
				conn.close();
		}
    }
	public ArrayList getLeafIds( String grade_index )
	 throws SQLException
	{
		Connection conn = null;
		ArrayList leafList = new ArrayList();
		try
		{ 
		 	conn = Utility.getConnection();
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
			table.PrcessTree();
			ArrayList leafCells = table.GetLeafCells();
			for(int h=0; h<leafCells.size(); h++)
			{
				CMyCellRecord cell = (CMyCellRecord)leafCells.get(h);
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
				conn.close();
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
			conn = Utility.getConnection();
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
			table.PrcessTree();            
			ArrayList tmplist = table.GetLeafCells();
			for( int i = 0; i < tmplist.size(); i++ )
			{
				CMyCellRecord cell = (CMyCellRecord)tmplist.get(i);
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
				conn.close();
		}		
		return list;
	}
	
    public  String MyTestFunc(String grade_index, ArrayList scoreList )//String grade_id, String qcUser, int planid, int gradestep)
    throws SQLException
    {
    	Connection conn = null;
		try
		{ 
			conn = Utility.getConnection();
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
			table.PrcessTree();            
			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getRSTableHTML( scoreList, true, "" );
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "�����";
		}
		finally
		{	
			if( conn != null )
				conn.close();
		}
    }
    
    public  String AdminAgentMyTestFunc(String grade_index, ArrayList scoreList )//String grade_id, String qcUser, int planid, int gradestep)
    throws SQLException
    {
    	Connection conn = null;
		try
		{ 
			conn = Utility.getConnection();
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
			table.PrcessTree();            
			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getAdminAgentTableHTML( scoreList, false, "" );
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "�����";
		}
		finally
		{	
			if( conn != null )
				conn.close();
		}
    }
    
    public  String MyTestFuncView(String grade_index, ArrayList scoreList )//String grade_id,String qcUser, int planid, int gradestep)
    throws SQLException
    {
    	Connection conn = null;
		try
		{ 
		 	conn = Utility.getConnection(); 
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
			table.PrcessTree();            
			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getTableHTML( scoreList, true, "" );
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "�����";
		} 
		finally
		{	
			if( conn != null )
				conn.close();
		}		
    }	  

    public  String MyTestFuncView2(String grade_index, ArrayList scoreList, String gradeChange )//String grade_id,String qcUser, int planid, int gradestep)
    throws SQLException
    {
    	Connection conn = null;
		try
		{ 
		 	conn = Utility.getConnection(); 
			CMyTableRecord table = new CMyTableRecord(conn,grade_index);
			table.PrcessTree();            
			table.sTdProp = "bgcolor=\"buttonface\" align=center";
			String sTable = "<table   border=0 cellspacing=1 cellpadding=2>";
			sTable += table.getTableHTML( scoreList, true, gradeChange );
			sTable += "</table>";
			return sTable;
		} 
		catch(Exception ex)
		{ 
			return "�����";
		} 
		finally
		{	
			if( conn != null )
				conn.close();
		}		
    }	 
    
	 public  String MyTestDictView(String grade_index,String qcUser, String gradestep)throws SQLException
	    {
		 Connection conn = null;
			try
			{ 
			 	conn = Utility.getConnection(); 
				CMyTableRecord table = new CMyTableRecord(conn,grade_index);
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
				return "�����";			
			} 
			finally
			{	
				if( conn != null )
					conn.close();
			}
	    }	 
}
/* End of class HelloWorld */

