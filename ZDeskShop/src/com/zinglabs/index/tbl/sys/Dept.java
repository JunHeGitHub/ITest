package com.zinglabs.index.tbl.sys;


public class Dept extends BaseDto {
	private static final long serialVersionUID = -5544958290959367531L;

	public static final int IS_ORGANIZATION = 1;
	public static final int NOT_ORGANIZATION = 0;

	public static final int ROOT_ID = 0;
	public static final int ROOT_PARENTID = -1;
	public static final String ROOT_NAME = "组织结构";
	public static final String ROOT_DESCRIPTION = "zu zhi jie gou gen jie dian  bu neng xiu gai shan chu";

	public static final String CTX_DEPT_TREE = "deptTree";
	public static final String CTX_DEPT = "dept";
	public static final String CTX_DEPT_LIST = "deptList";
	public static final String CTX_PARENT_DEPT = "parentDept";
	
	private int id;
	private int parentId;
	private String code;
	private String name;
	private String idPath;
	private int organization;
	private int listIndex;
	private String description;

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return Returns the idPath.
	 */
	public String getIdPath() {
		return idPath;
	}

	/**
	 * @param idPath
	 *            The idPath to set.
	 */
	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	/**
	 * @return Returns the listIndex.
	 */
	public int getListIndex() {
		return listIndex;
	}

	/**
	 * @param listIndex
	 *            The listIndex to set.
	 */
	public void setListIndex(int listIndex) {
		this.listIndex = listIndex;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the organization.
	 */
	public int getOrganization() {
		return organization;
	}

	/**
	 * @param organization
	 *            The organization to set.
	 */
	public void setOrganization(int organization) {
		this.organization = organization;
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
		if (obj == null || !(obj instanceof Dept)) {
			return false;
		}

		Dept tmp = (Dept) obj;

		return (id == tmp.id);
	}
}
