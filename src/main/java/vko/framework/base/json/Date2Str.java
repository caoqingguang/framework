package vko.framework.base.json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class Date2Str implements ObjectSerializer{
	public static Date2Str  instance=new Date2Str();
	private SimpleDateFormat sdf=new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss\"");
	private Date2Str(){}
	public static Date2Str getInstance(){
		return instance;
	}
	@Override
	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException {
		SerializeWriter out = serializer.getWriter();
		Date value=(Date)object;
		if(value==null){
			return ;
		}
		out.write(sdf.format(value));
	}
}
