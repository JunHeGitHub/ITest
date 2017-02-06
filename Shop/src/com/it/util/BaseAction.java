package com.it.util;

import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class BaseAction {

	
	public void postStrToClient(String json, HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(json);
			pw.flush();
			pw.close();
		} catch (Exception x) {
			System.out.println("转发");
		}
	}
	
	public <T> void postStrToClient(T data, HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=UTF-8");
			OutputStream os = response.getOutputStream();
			JsonUtils.genUpdateDataReturnJsonStr(data, os);
		} catch (Exception x) {
			System.out.println("转发");
		}
	}
	
	
}
