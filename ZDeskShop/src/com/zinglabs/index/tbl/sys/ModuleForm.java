package com.zinglabs.index.tbl.sys;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.zinglabs.index.tbl.sys.*;



public class ModuleForm extends ActionForm {
	private static final long serialVersionUID = -8415304018596857832L;

	private Module module;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		module = new Module();
	}

	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		return super.validate(mapping, request);
	}

	/**
	 * @return Returns the dept.
	 */
	public Module getModule() {
		return module;
	}
}
