package vko.framework.base.jsonp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class JsonpInterceptor implements HandlerInterceptor{
	
	private JsonpStore jsonpStore;
	
	
	public JsonpStore getJsonpStore() {
		return jsonpStore;
	}

	public void setJsonpStore(JsonpStore jsonpStore) {
		this.jsonpStore = jsonpStore;
	}
	
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		this.jsonpStore.clear();
		String function=request.getParameter(this.jsonpStore.getCallbackFlag());
		if(function==null||"".equals(function)){
			return true;
		}
		if(handler instanceof HandlerMethod){  
            HandlerMethod method = (HandlerMethod)handler; 
            if(method.getMethodAnnotation(JsonpMethod.class)!=null){
            	this.jsonpStore.setFunction(function);
            }
		}
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
