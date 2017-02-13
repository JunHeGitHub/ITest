package com.zinglabs.servlet.zkmDocTemplate.templateImp;

import com.zinglabs.servlet.zkmDocTemplate.ZkmDocGenerator;

public class ZkmDocTemplateDefault implements ZkmDocGenerator {

	public static final String DOC_HEAD="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title></title><style>table{border: 1px solid #000000;border-collapse: collapse;}td{border: 1px solid #000000;border-collapse: collapse;}</style></head><body>";
	
	public static final String DOC_BOTTOM="</body></html>";
	
	public String getZkmContent(String docBody) {
		if(docBody==null){
			docBody="";
		}
		StringBuffer sb=new StringBuffer();
		sb.append(DOC_HEAD);
		sb.append(docBody);
		sb.append(DOC_BOTTOM);
		return sb.toString();
	}
}
