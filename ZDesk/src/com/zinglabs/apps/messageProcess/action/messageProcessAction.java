package com.zinglabs.apps.messageProcess.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.messageProcess.messageProcessService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping(value = "/*/messageProcess")
public class messageProcessAction extends BaseAction {
	@RequestMapping(value = "/randomData")
	public void SelMessOneData(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			Map selectone = getService().randomAccessToData(map);
			if (selectone != null) {
				Map mapup = new HashMap();
				mapup.put("id", selectone.get("id"));
				mapup.put("ZT", "处理中");
				try {
					int upcount = getService().UpdateMess(mapup); // 修改留言状态
					if (upcount != 0) {
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "查询并修改状态成功", selectone), response);
					} else {
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "修改状态失败"), response);
					}
				} catch (Exception e) {
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "修改状态失败"), response);
				}
			}

		} catch (Exception e) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "查询失败"),response);
		}
	}

	public messageProcessService getService() {
		return (messageProcessService) getBean("messageProcessService");
	}
}
