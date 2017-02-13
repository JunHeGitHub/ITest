

package com.zinglabs.index.tbl.sys;



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
	
	private int id;

	private int parentId;
	
	private String idPath;

	private String grade_index;

	private String description;
	
	private String min_value;
	
	private String max_value;
	
	private String reference_value;
	
	private String reference2_value;
	
	private String reference3_value;
	
	private String reference4_value;
//增加百分比的功能-----2009-02-09	
	private int percent ;
//等级划分	
	private String value_remark;
//是否显示删除按钮	
	private int test;
	
	public int getTest() {
		return test;
	}

	public void setTest(int test) {
		this.test = test;
	}

	public String getValue_remark() {
		return value_remark;
	}

	public void setValue_remark(String value_remark) {
		this.value_remark = value_remark;
	}

	/**
	 * @return Returns the max_value.
	 */
	public String getMax_value() {
		return max_value;
	}

	/**
	 * @param max_value The max_value to set.
	 */
	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}

	/**
	 * @return Returns the min_value.
	 */
	public String getMin_value() {
		return min_value;
	}

	/**
	 * @param min_value The min_value to set.
	 */
	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}

	/**
	 * @return Returns the reference_value.
	 */
	public String getReference_value() {
		return reference_value;
	}

	/**
	 * @param reference The reference to set.
	 */
	public void setReference_value(String reference_value) {
		this.reference_value = reference_value;
	}

	/**
	 * @return Returns the idPath.
	 */
	public String getIdPath() {
		return idPath;
	}

	/**
	 * @param idPath The idPath to set.
	 */
	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	
	

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the parentId.
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            The parentId to set.
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Dict)) {
			return false;
		}

		Dict tmp = (Dict) obj;

		return (id == tmp.id);
	}

	public String getGrade_index() {
		return grade_index;
	}

	public void setGrade_index(String grade_index) {
		this.grade_index = grade_index;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getReference2_value() {
		return reference2_value;
	}

	public void setReference2_value(String reference2_value) {
		this.reference2_value = reference2_value;
	}

	public String getReference3_value() {
		return reference3_value;
	}

	public void setReference3_value(String reference3_value) {
		this.reference3_value = reference3_value;
	}

	public String getReference4_value() {
		return reference4_value;
	}

	public void setReference4_value(String reference4_value) {
		this.reference4_value = reference4_value;
	}
}
