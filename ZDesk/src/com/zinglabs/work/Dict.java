
package com.zinglabs.work;



public class Dict extends BaseDto {
	private static final long serialVersionUID = -1206966474340168742L;
///////////////////////////////////////////////
	public static final int ROOT_NODE_ID = 0;
	public static final int ROOT_NODE_PARENTID = -1;
	public static final String ROOT_NODE_CODE = "DICT(" + ROOT_NODE_ID + ","
			+ ROOT_NODE_PARENTID + ")";
	public static final String ROOT_NODE_DESC = "所有评分项";
	public static final String ROOT_DESCRIPTION = "专家信息字典根结点，不可以修改和删除。";
	public static final String CTX_DICT = "dict";
	public static final String CTX_PARENT_DICT = "parentDict";
	public static final String CTX_DICTMAP = "CtxDictMap";
	////////////////////////////////////////////////
	
	
	private int teach_dir_id;
	
	private int teach_dir_parentid;
	
	private String teach_dir_idparth;
	
	private String teach_id;
	
	private String teach_dir_description;
	
	private int teach_dir_state;
	

	public int getTeach_dir_state() {
		return teach_dir_state;
	}
	public void setTeach_dir_state(int teach_dir_state) {
		this.teach_dir_state = teach_dir_state;
	}
	public int getTeach_dir_id() {
		return teach_dir_id;
	}
	public void setTeach_dir_id(int teach_dir_id) {
		this.teach_dir_id = teach_dir_id;
	}
	public int getTeach_dir_parentid() {
		return teach_dir_parentid;
	}
	public void setTeach_dir_parentid(int teach_dir_parentid) {
		this.teach_dir_parentid = teach_dir_parentid;
	}
	
	public String getTeach_dir_idparth() {
		return teach_dir_idparth;
	}
	public void setTeach_dir_idparth(String teach_dir_idparth) {
		this.teach_dir_idparth = teach_dir_idparth;
	}
	public String getTeach_id() {
		return teach_id;
	}
	public void setTeach_id(String teach_id) {
		this.teach_id = teach_id;
	}
	public String getTeach_dir_description() {
		return teach_dir_description;
	}
	public void setTeach_dir_description(String teach_dir_description) {
		this.teach_dir_description = teach_dir_description;
	}
	
}
