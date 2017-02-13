package com.zinglabs.apps.validateCode.action;



import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zinglabs.apps.validateCode.RandomValidateCode;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.CookieUtils;
import com.zinglabs.util.DESUtil;
import com.zinglabs.util.JsonUtils;


@Controller
@RequestMapping("/ZKM/validateCode")
public class ValidateCodeAction extends BaseAction {

	/**
	 * 
	 * 获取验证码请求
	 * 
	 * @throws IOException
	 */

	@RequestMapping(value = "/randomCode")
	public void randomCode(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		
		RandomValidateCode randomValidateCode=RandomValidateCode.getInstance();
		try {

			randomValidateCode.getRandcode(request, response);// 输出图片方法
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/***
	 * 验证验证码 正确返回true 反 false
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/verificationCode")
	public void verificationCode(HttpServletRequest request,HttpServletResponse response) {

		HashMap map=CookieUtils.cookie2Map(request);
		String code="";
		String codeValue=request.getParameter("code");
		try {
			code=DESUtil.decodeUTF2(map.get("cookietest").toString());
			code=(String) code.subSequence(0,4);
			if(code.equals(codeValue.toUpperCase())){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,""), response);
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,""), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	

//	@Override
//	public void setApplicationContext(ApplicationContext context)
//			throws BeansException {
//		actionApplicationContext = context;
//	}

}
