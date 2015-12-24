package vko.framework.timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

public class TimerSock {
	
	private final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private RedisTemplate<String, String> redis;
	private long timeout=60l;
	public RedisTemplate<String, String> getRedis() {
		return redis;
	}
	public void setRedis(RedisTemplate<String, String> redis) {
		this.redis = redis;
	}
	
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public boolean weCan(TimerObject timerObj,Date date){
		if(!timerObj.getSock()){
			return true;
		}
		String key=timerObj.genId()+"="+sdf.format(date);
		String value=redis.opsForValue().getAndSet(key,"ok");
		redis.expire(key,timeout,TimeUnit.SECONDS);
		return value==null;
	}
	

}
