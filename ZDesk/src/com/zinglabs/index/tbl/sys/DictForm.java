package com.zinglabs.index.tbl.sys;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.zinglabs.index.tbl.sys.*;



public class DictForm extends ActionForm {
	private static final long serialVersionUID = -8415304018596857832L;

	private Dict dict;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		dict = new Dict();
	}

	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		return super.validate(mapping, request);
	}

	/**
	 * @return Returns the dept.
	 */
	public Dict getDict() {
		return dict;
	}
}
