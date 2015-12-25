package vko.framework.timer;

import java.lang.reflect.Method;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class TimerResolve implements Job{
	private static ApplicationContext ac;
	public static final String flag="id";
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	public static void setAc(ApplicationContext ac){
		TimerResolve.ac=ac;
	}
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		Date now=new Date();
		if(ac==null){
			return;
		}
		JobDataMap data=context.getMergedJobDataMap();
		Object obj=data.get(flag);
		if(obj==null||!(obj instanceof TimerObject)){
			return ;
		}
		TimerObject timerObj=(TimerObject)obj;
		if(timerObj.getSock()){
			TimerSock sock=ac.getBean(TimerSock.class);
			if(!sock.weCan(timerObj, now)){
				this.logger.debug("定时任务被更勤劳的小伙伴抢去了,下次我会努力的,{}",timerObj);
				return ;
			}
			this.logger.debug("运气不错,抢到一个定时任务任务,现在执行,{}",timerObj);
		}
		Class<?> clazz=timerObj.getClazz();
		Object target=ac.getBean(clazz);
		Method method=timerObj.getMethod();
		int argsLen=method.getParameterTypes().length;
		Object[] args=new Object[argsLen];
		try {
			this.logger.debug("开始执行一个定时任务:\n{}",timerObj);
			method.invoke(target,args);
			this.logger.debug("定时任务执行结束:\n{}",timerObj);
		} catch (Exception e) {
			this.logger.error("定时任务执行出错:\n{}",timerObj,e);
		} 
	}
}
