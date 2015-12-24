package vko.framework.timer;

import java.lang.reflect.Method;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

class TimerObject {
	
	private final Class<?> clazz;
	private final Method method;
	private final String rule;
	private final Boolean sock;
	public TimerObject(Class<?> clazz, Method method,String rule,boolean sock) {
		super();
		this.clazz = clazz;
		this.method = method;
		this.rule = rule;
		this.sock=sock;
	}

	public String getRule() {
		return rule;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public Method getMethod() {
		return method;
	}
	
	
	
	public Boolean getSock() {
		return sock;
	}

	public JobKey genJobKey(){
		return new JobKey(this.genId(),"plan");
	}
	
	public TriggerKey genTriggerKey(){
		return new TriggerKey(this.genId(),"planT");
	}
	
	public String genId(){
		return "timerObject:"+this.clazz.getName()+":"+this.method.getName();
	}

	@Override
	public String toString() {
		return "TimerObject [clazz=" + clazz + ", method=" + method + ", rule="
				+ rule + ", sock=" + sock + "]";
	}

	
	
	
	
}
