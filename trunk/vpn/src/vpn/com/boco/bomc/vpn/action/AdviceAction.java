package com.boco.bomc.vpn.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.expressme.webwind.ActionContext;
import org.expressme.webwind.Mapping;
import org.expressme.webwind.renderer.Renderer;
import org.expressme.webwind.renderer.TextRenderer;

import com.boco.bomc.vpn.db.bean.Page;
import com.boco.bomc.vpn.domain.Advice;
import com.boco.bomc.vpn.domain.UserContext;
import com.boco.bomc.vpn.service.AdviceService;
import com.boco.bomc.vpn.service.AdviceServiceImpl;
import com.boco.bomc.vpn.service.ServiceException;

public class AdviceAction extends BaseAction{
	@Mapping("/advice")
	public Renderer saveAdvice() {
		
		HttpServletRequest request = ActionContext.getActionContext().getHttpServletRequest();
		HttpSession session = ActionContext.getActionContext().getHttpSession();
		UserContext userContext = (UserContext)session.getAttribute("SESSION_USER");
		
		if(userContext==null){
			String json = "{\"status\":\"0\",\"message\":\"请登录后留言!\",\"result\":\"{当前用户未登录}\"}";
			return new TextRenderer(json);
		}
		String loginname = userContext.getUsername();
		String adviceStr = request.getParameter("content");
		String parentId = request.getParameter("parentId");
		
		Advice advice = new Advice();
		advice.setCreate_time(new Date());
		advice.setLoginname(loginname);
		advice.setName(loginname);
		advice.setAdvice(adviceStr);
		advice.setParent_id(parentId==null?new Long(0):Long.valueOf(parentId));
		
		AdviceService service = new AdviceServiceImpl();
		
		if(!service.saveAdvice(advice)){
			String json = "{\"status\":\"0\",\"message\":\"留言保存失败!\",\"result\":\"{留言保存失败}\"}";
			return new TextRenderer(json);
		}
		
		String json = "{\"status\":\"1\",\"message\":\"留言保存成功!\",\"result\":\"{留言保存成功}\"}";
		return new TextRenderer(json);
	}
	
	@Mapping("/advices")
	public Renderer getAdvices(){
		
		HttpServletRequest request = ActionContext.getActionContext().getHttpServletRequest();
		HttpSession session = ActionContext.getActionContext().getHttpSession();
		UserContext userContext = (UserContext)session.getAttribute("SESSION_USER");
		
		if(userContext==null){
			String json = "{\"status\":\"0\",\"message\":\"请登录操作!\",\"result\":\"{当前用户未登录}\"}";
			return new TextRenderer(json);
		}
		
		Page page = new Page();
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		if(pageNum!=null&&!pageNum.equals(""))
			page.setPageNum(Integer.valueOf(pageNum));
		else
			page.setPageNum(1);
		if(pageSize!=null&&!pageSize.equals("")){
			page.setPageSize(Integer.valueOf(pageSize));
		}
		
		AdviceService service = new AdviceServiceImpl();
		try {
			page = service.getAdvices(page);
		} catch (ServiceException e) {
			String json = "{\"status\":\"0\",\"message\":\"留言获取失败!\",\"result\":\"{"+e.getMessage()+"}\"}";
			return new TextRenderer(json);
		}
		
		String json ="{\"status\":\"1\",\"page\":"+JSONObject.fromObject(page).toString()+",\"message\":\"获取留言成功!\",\"result\":"+JSONArray.fromObject(page.getObjects()).toString()+"}";
		return new TextRenderer(json);
	}
}