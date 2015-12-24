package vko.framework.base.jsonp;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class JsonpInterceptor implements HandlerInterceptor{
	
	private String callback="callback";
	
	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	ThreadLocal<Boolean> jsonp=new ThreadLocal<Boolean>();
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		jsonp.remove();
		String callback=request.getParameter(this.callback);
		if(callback==null||"".equals(callback)){
			return true;
		}
		if(handler instanceof HandlerMethod){  
            HandlerMethod method = (HandlerMethod)handler; 
            if(method.getMethodAnnotation(JsonpMethod.class)!=null){
            	BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
            	bw.write(callback+'(');
            	bw.flush();
            	jsonp.set(true);
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
		if(jsonp.get()!=null&&jsonp.get()){
			response.getOutputStream().write(')');
			jsonp.remove();
		};
	}
}
