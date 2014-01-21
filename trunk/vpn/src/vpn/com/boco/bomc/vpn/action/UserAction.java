package com.boco.bomc.vpn.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.expressme.webwind.ActionContext;
import org.expressme.webwind.Mapping;
import org.expressme.webwind.renderer.Renderer;
import org.expressme.webwind.renderer.TextRenderer;

import com.boco.bomc.vpn.db.bean.Page;
import com.boco.bomc.vpn.domain.MainUser;
import com.boco.bomc.vpn.domain.UserContext;
import com.boco.bomc.vpn.service.MainUserService;
import com.boco.bomc.vpn.service.MainUserServiceImpl;
import com.boco.bomc.vpn.service.ServiceException;

public class UserAction {
	
	private static final Logger logger = Logger.getLogger(UserAction.class);
	
	@Mapping("/u/mainusers")
	public Renderer getMainUsersByPage(){
		
		HttpServletRequest reuqest = ActionContext.getActionContext().getHttpServletRequest();
		
		Page page = new Page();
		String pageNum = reuqest.getParameter("pageNum");
		String pageSize = reuqest.getParameter("pageSize");
		if(pageNum!=null&&!pageNum.equals(""))
			page.setPageNum(Integer.valueOf(pageNum));
		else
			page.setPageNum(1);
		if(pageSize!=null&&!pageSize.equals("")){
			page.setPageSize(Integer.valueOf(pageSize));
		}
		MainUserService service = new MainUserServiceImpl();
		List<MainUser> mainUsers = null;
		try {
			page = service.getMainUsers(page,null);
		} catch (ServiceException e) {
			logger.error("�޷���ȡ�������û��б�", e);
			String json ="{\"status\":\"0\",\"message\":\"�޷���ȡ�������û��б�!\",\"result\":{}}";
			return new TextRenderer(json);
		}
		
		reuqest.setAttribute("page",page);
		
		
		String json ="{\"status\":\"1\",\"page\":"+JSONObject.fromObject(page).toString()+",\"message\":\"��ȡ�������û��б�!\",\"result\":"+JSONArray.fromObject(page.getObjects()).toString()+"}";
		System.out.println(json);
		return new TextRenderer(json);
	}
	
	/**
	 * ����4A�û��ʺ�״̬��$1Ϊ�û��ʺţ�$2Ϊ1ʱ��ʶ������Ϊ0ʱ��ʶ���á�
	 * @param loginname
	 * @param approve
	 * @return
	 */
	@Mapping("/u/approve/$1/$2")
	public Renderer enable4AUser(String loginname,String approve){
		
		HttpServletRequest request = ActionContext.getActionContext().getHttpServletRequest();
		HttpSession session = ActionContext.getActionContext().getHttpSession();
		UserContext userContext = (UserContext)session.getAttribute("SESSION_USER");
		
		if(userContext==null){
			String json = "{\"status\":\"0\",\"message\":\"���¼����!\",\"result\":\"{��ǰ�û�δ��¼}\"}";
			return new TextRenderer(json);
		}
		
		if(loginname==null||"".equals(loginname.trim())){
			String json = "{\"status\":\"0\",\"message\":\"��ֹ�������޶�Ӧ�����ʺ�!\",\"result\":\"{��ֹ�������޶�Ӧ�����ʺ�!}\"}";
			return new TextRenderer(json);
		}
		
		MainUserService service = new MainUserServiceImpl();
		int ret= 0;
		try {
			if (approve.equals("1")) {
				ret = service.enableMainUser(loginname);
			} else if (approve.equals("0")) {
				ret = service.disabledMainUser(loginname);
			}
		} catch (ServiceException e) {
			String json = "{\"status\":\"0\",\"message\":\"�����û�ʧ��!\",\"result\":\""+e.getMessage()+"\"}";
			return new TextRenderer(json);
		}
		if(ret<=0){
			String json = "{\"status\":\"0\",\"message\":\"��Ч�������޶�Ӧ�����ʺ�!\",\"result\":\"{��Ч�������޶�Ӧ�����ʺ�!}\"}";
			return new TextRenderer(json);
		}
		
		String json = "{\"status\":\"1\",\"message\":\"�����ʺųɹ�!\",\"result\":\"{�����ʺųɹ�!}\"}";
		return new TextRenderer(json);
	}
	
}