package vko.framework.timer;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import vko.framework.base.scanner.ScanUtil;

public class TimerCenter implements ApplicationListener<ContextRefreshedEvent>{
	private boolean startFirst=true;
	
	private String path;
	
	private Long delay=5000l;
	

	public Long getDelay() {
		return delay;
	}

	public void setDelay(Long delay) {
		this.delay = delay;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private static void startTimer(TimerObject obj) throws SchedulerException, ParseException{
		JobDataMap datamap=new JobDataMap();
		datamap.put(TimerResolve.flag,obj);
		JobDetail jobDetail=JobBuilder.newJob().ofType(TimerResolve.class).usingJobData(datamap).withIdentity(obj.genId()).build();
		
		// 定义调度触发规则
		CronTriggerImpl trigger=new CronTriggerImpl();
		trigger.setKey(obj.genTriggerKey());
		trigger.setCronExpression(obj.getRule());
		
		// 把作业和触发器注册到任务调度中  
		SchedulerFactory schedulerfactory=new StdSchedulerFactory();  
		Scheduler scheduler=schedulerfactory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();	
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		TimerResolve.setAc(event.getApplicationContext());
		if(!this.startFirst){
			return;
		}
		this.startFirst=false;
		final List<TimerObject> list=this.getListTimer();
		final Long delay=this.delay;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				for(TimerObject obj:list){
					try {
						TimerCenter.startTimer(obj);
					} catch (SchedulerException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}
	
	private List<TimerObject> getListTimer(){
		Set<Class<?>> cs=ScanUtil.scannerClass(this.path, TimerClass.class);
		List<TimerObject> list=new ArrayList<TimerObject>();
		for(Class<?> clazz:cs){
			Set<Method> ms=ScanUtil.scannerMethod(clazz, TimerMethod.class);
			for(Method m:ms){
				TimerMethod tm=m.getAnnotation(TimerMethod.class);
				TimerObject obj=new TimerObject(clazz, m, tm.rule(),tm.sock());
				list.add(obj);
			}
			
		}
		return list;
	}
}
