/**
 * 
 */
package com.zinglabs.work;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


public class XTreeNode implements Serializable {
	private static final long serialVersionUID = 3461339809754559202L;

	
	private String id;

	
	private String text;

	
	private String parentId;

	
	private String action;

	
	private String icon;

	
	private String openIcon;

	
	private int level;

	
	private String target;
	
	public XTreeNode(String id,String text,String parentId,String action,String target){
		this(id,text,parentId,action,"","",target);
	}
	
	public XTreeNode(String id, String text, String parentId, String action,
			String icon, String openIcon,String target) {
		this(id, text, parentId, action, icon, openIcon, 2, target);
	}

	public XTreeNode(String id, String text, String parentId, String action,
			String icon, String openIcon, int level, String target) {
		this.id = id;
		this.text = text;
		this.parentId = parentId;
		this.action = action;
		this.icon = icon;
		this.openIcon = openIcon;
		this.level = level;
		this.target = target;
	}

	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return Returns the flag.
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param flag
	 *            The flag to set.
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return Returns the icon.
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            The icon to set.
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the openIcon.
	 */
	public String getOpenIcon() {
		return openIcon;
	}

	/**
	 * @param openIcon
	 *            The openIcon to set.
	 */
	public void setOpenIcon(String openIcon) {
		this.openIcon = openIcon;
	}

	/**
	 * @return Returns the parentId.
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            The parentId to set.
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            The text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return Returns the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            The level to set.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof XTreeNode)) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		XTreeNode tmp = (XTreeNode) obj;

		return new EqualsBuilder().append(id, tmp.id).append(text, tmp.text)
				.append(parentId, tmp.parentId).append(icon, tmp.icon).append(
						openIcon, tmp.openIcon).append(action, tmp.action)
				.append(target, tmp.target).append(level, tmp.level).isEquals();
	}

	
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(text).append(parentId)
				.append(icon).append(openIcon).append(action).append(target)
				.append(level).toHashCode();
	}

	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[id = " + id + ",text = " + text + ",parentId = ");
		buffer.append(parentId + ",icon = " + icon + ",openIcon = " + openIcon);
		buffer.append(",action = " + action + ",target = " + target + ",level = ");
		buffer.append(level + "]");

		return buffer.toString();
	}

}
