package com.zinglabs.index.tbl.sys;

public class Module extends BaseDto {
	private static final long serialVersionUID = -1206966474340168742L;
///////////////////////////////////////////////
	public static final int ROOT_NODE_ID = 0;
	public static final int ROOT_NODE_PARENTID = -1;
	public static final String ROOT_NODE_CODE = "MODULE(" + ROOT_NODE_ID + ","
			+ ROOT_NODE_PARENTID + ")";
	public static final String ROOT_NODE_DESC = "root";
	public static final String ROOT_NAME = "gen mu lu ";
	public static final String CTX_MODULE = "module";
	public static final String CTX_PARENT_MODULE = "parentModule";
	public static final String CTX_MODULE_TREE = "moduleTree";	
	public static final String CTX_MODULE_LIST = "moduleList";
	////////////////////////////////////////////////
	
	private int id;

	private int parentId;
	
	private String idPath;

	private String code;

	private String name;
	
	private String linkedUrl;
	
	private int listIndex;
	
	private int enabled;
	
	private int Type;
	

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	public int getListIndex() {
		return listIndex;
	}

	public void setListIndex(int lastIndex) {
		this.listIndex = lastIndex;
	}

	public String getLinkedUrl() {
		return linkedUrl;
	}

	public void setLinkedUrl(String linkedUrl) {
		this.linkedUrl = linkedUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	
}
