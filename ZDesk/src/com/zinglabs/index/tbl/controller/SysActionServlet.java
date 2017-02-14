package com.zinglabs.index.tbl.controller;

import javax.servlet.ServletException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.action.ActionServlet;

import com.zinglabs.index.tbl.util.DateConvert;


public class SysActionServlet extends ActionServlet {
	private static final long serialVersionUID = -5591776352406702997L;

	public void init() throws ServletException {
		super.init();
		ConvertUtils.register(new DateConvert(), java.util.Date.class);
	}
}
