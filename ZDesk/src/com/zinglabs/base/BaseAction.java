package com.zinglabs.base;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;



public abstract class BaseAction extends Action {
	protected HttpServletRequest cRequest = null;

	protected ActionMapping cMapping = null;

	protected HttpSession cSession = null;

	private Properties msgProperties = null;

	public BaseAction() {
	}

	// call back user action
	abstract public ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception;

	// call back user session wraper id
	public String getSessionWrapperId() {
		return BaseConstants.SESSION_USERDATA;
	}

	protected boolean checkSession() {
		// login action do not need check
		String url = cMapping.getPath();
		//System.out.println(url);
		if ("/user/logon".equals(url) || "/user/logout".equals(url)) {
			cSession.removeAttribute("SelectedModule");
			cSession.removeAttribute("SelectedSubModule");
			cSession.removeAttribute("SelectedSubSubModule");

			//System.out.println("check session");
			cSession = cRequest.getSession(false);
			return true;
		}
		HttpSession session = cRequest.getSession(false);

		if (session == null) {
			System.out.println("checkSession: session is invalidated");
			return false;
		} else {
			//System.out.println("checkSession:session is active");
			cSession = session;
			checkSessionUserData();
		}
		return true;
	}

	public static String convert(String str) {

		try {
			if (str == null)
				return str;
			return new String(str.getBytes("ISO8859-1"), "UTF-8");
		} catch (Exception ex) {
			System.out.println("Convert Error:" + str);
			return str;
		}
	}

	protected boolean checkSessionUserData() {
		String userId = null;
		String userName = null;
		String phoneNumber = null;
		String roleName = null;
		String role = null;
		if (cSession.getAttribute("QCUSER") == null) {
			phoneNumber = (String)cRequest.getParameter("QCUSER");
			cSession.setAttribute("QCUSER",phoneNumber);
		}

		return true;
	}

	protected boolean checkPermission() {
		/* 2007-10-23 {begin for no use
		int role = -1;
		String userName = null;
		String roleName = null;
		String subMenuID = null;
		if (cSession.getAttribute("SSCUSER") != null) {
			userName = cSession.getAttribute("SSCUSER").toString();
		}
		if (cSession.getAttribute("ROLENAME") != null){
			roleName =cSession.getAttribute("ROLENAME").toString();
		}
		if (cSession.getAttribute("ROLE") != null) {
			role = Integer.valueOf(cSession.getAttribute("ROLE").toString())
					.intValue();
		}
		if (cRequest.getParameter("SubMenu") != null) {
			subMenuID = cRequest.getParameter("SubMenu");
		}
		//if (!Functions.validityFunction(subMenuID, userName)) return false;
		
		Vector funIDMenu = null;
		if(cSession.getAttribute("UserMenu") != null){
			funIDMenu = (Vector)cSession.getAttribute("UserMenu");
		}
		end}*/

		/*
		else{
			funIDMenu = Functions.getFunctions(userName,roleName);
			cSession.setAttribute("UserMenu",funIDMenu);
		}
		*/
		//if ((funIDMenu == null) ||(funIDMenu.indexOf(subMenuID)  == -1)) return false;
		return true;
	}

	public void setErrorMSG(HttpServletRequest request, String errMSG) {
		request.setAttribute("Info", getMSGText(errMSG));
	}

	public void setErrorMSG(HttpServletRequest request, String msgTitle, String errMSG ) {
		request.setAttribute("Info", msgTitle+" "+getMSGText(errMSG) );
	}
	
	public void setErrorMSG(HttpServletRequest request, String errMSG1, String errMSG2, int msgPara ) {
		request.setAttribute("Info", getMSGText(errMSG1)+msgPara+getMSGText(errMSG2) );
	}

	public void setErrorMSG(HttpServletRequest request, String errMSG1, String errMSG2, String msgPara ) {
		request.setAttribute("Info", getMSGText(errMSG1)+msgPara+getMSGText(errMSG2) );
	}
	
	public void setErrorMSG2(HttpServletRequest request, String errMSG, String msgPara ) {
		request.setAttribute("Info", getMSGText(errMSG)+msgPara );
	}
	
	public void handleMenu() {
		String ltmp = cRequest.getParameter("Menu");
		if (ltmp != null) {
			cSession.setAttribute("SelectedModule", cRequest
					.getParameter("Menu"));
		}
		ltmp = cRequest.getParameter("SubMenu");
		if (ltmp != null) {
			cSession.setAttribute("SelectedSubModule", cRequest
					.getParameter("SubMenu"));
		}
		ltmp = cRequest.getParameter("SubSubMenu");
		if (ltmp != null) {
			cSession.setAttribute("SelectedSubSubModule", cRequest
					.getParameter("SubSubMenu"));
		}
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		//System.out.println("base action");
		this.cRequest = request;
		this.cMapping = mapping;
		this.cSession = cRequest.getSession(false);
		cSession.setAttribute("Locale", cRequest.getCharacterEncoding());
		ActionForward forwardPage = null;
		try {
			if (!checkSession()){
				return mapping.findForward("logon");
			}
			handleMenu();
			forwardPage = executeAction(mapping, form, request, response);
		} catch (BaseException e) {
			e.printStackTrace();
			if (e.getRootCause() == null) {
				forwardPage = processBaseException(request, mapping, e);
			} else {
				forwardPage = processSystemException(request, mapping, e);
			}
		} catch (Throwable ex) {
			// Debug.logError(ex);
			ex.printStackTrace();
			forwardPage = processSystemException(request, mapping, ex);
		} finally {
			clear();
		}

		return forwardPage;
	}

	public void clear() {
	}

	protected ActionForward processException(HttpServletRequest request,
			ActionMapping mapping, Exception e) {
		ActionForward rtn = null;
		if (e instanceof BaseException) {
			BaseException ie = (BaseException) e;
			if (ie.getRootCause() == null) {
				rtn = processBaseException(request, mapping, ie);
			} else {
				rtn = processSystemException(request, mapping, ie);
			}
		} else {
			rtn = processSystemException(request, mapping, e);
		}
		return rtn;
	}

	protected ActionForward processBaseException(HttpServletRequest request,
			ActionMapping mapping, BaseException ex) {
		ActionMessages messages = new ActionMessages();
		ActionForward forward = null;

		processBaseException(messages, ex);

		// Either return to the input resource or a configured failure forward
		if (mapping.findForward(BaseConstants.FORWARD_FAILUER) != null) {
			forward = mapping.findForward(BaseConstants.FORWARD_FAILUER);
		} else if (mapping.getInput() != null) {
			forward = new ActionForward(mapping.getInput());
		}

		// See if this exception contains a collection of sub exceptions
		List exceptionList = ex.getExcepionsList();
		if (exceptionList != null && !exceptionList.isEmpty()) {
			for (int i = 0; i < exceptionList.size(); i++) {
				BaseException subException = (BaseException) exceptionList.get(i);
				// processBaseException(errors, subException);
			}
		}

		// Tell the Struts framework to save the errors into the request
		// saveErrors(request, errors);

		// Return the ActionForward
		return forward;
	}

	protected void processBaseException(ActionMessages messages,
			BaseException ex) {
		/*
		 * String lsMsg = messages.getResourceKey(); if (lsErrMsg!=null &&
		 * lsErrMsg.length()>0) messages.add(Globals.GLOBAL_ERROR, new
		 * ActionMessages(lsErrMsg));
		 */
	}

	protected ActionForward processSystemException(HttpServletRequest request,
			ActionMapping mapping, Throwable ex) {
		/*
		 * ActionErrors errors = new ActionErrors();
		 * errors.add(ActionErrors.GLOBAL_ERROR, new
		 * ActionError(ex.getMessage())); saveErrors( request, errors );
		 */
		// request.setAttribute(Action.EXCEPTION_KEY, ex);
		// forward to system error page
		return mapping.findForward(BaseConstants.FORWARD_SYSTEM_FAILUER);
	}

	protected void displayInfoOnJSP(HttpServletRequest request, String msg) {
		ActionMessages msgs = new ActionMessages();
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(msg));
		this.saveMessages(request, msgs);
	}

	protected String getMSGText(String key) {
		String res = "";
		msgProperties = (Properties) this.getServlet().getServletContext()
				.getAttribute("MSGProerties");
		if (msgProperties == null) {
			res = "";
		} else {
			res = msgProperties.getProperty(key);
			if (res == null) {
				res = "";
			}
		}
		return res;
	}

	

	protected void setStoreList(HttpServletRequest request,
			String storedListName, Object listName) {
		request.setAttribute(storedListName, listName);
	}

	public boolean isValidIp(String ip) {
		if ((ip == null) || (ip.length() == 0))
			return false;

		String ipReg = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
		if (!Pattern.matches(ipReg, ip))
			return false;

		StringTokenizer strToken = new StringTokenizer(ip, ".");
		int addr;
		for (int i = 0; i < 4; i++) {
			addr = Integer.parseInt(strToken.nextToken().toString());
			if ((addr < 0) || (addr > 255))
				return false;
		}
		return true;
	}



	
	protected void closeDBConnection(Connection conn){
		try{
			if(conn != null){
				conn.close();
			}
		}catch(SQLException e){
			System.out.println("DB Connection close failed !");
			System.out.println(e);		
		}
	}
}
