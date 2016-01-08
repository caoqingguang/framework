package vko.framework;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class TestMain {
	public static void main(String[] args) {
		SerializeConfig mapping=new SerializeConfig();
		List<Person> list=new ArrayList<Person>();
		Person p1=new Person(1l, "cao");
		p1.setTime(new Date());
		list.add(p1);
		Person p2=new Person();
		p2.setName("qing");
		list.add(p2);
		//list.add("abdcd");
//		mapping.put(long.class, MyLong2Str.instance);
		mapping.put(Long.class, MyLong2Str.instance);
		mapping.put(Date.class, Date2Str.instance);
		Object ooo=new Long[]{1l,2l};
		String str=JSON.toJSONString(list,mapping);
		System.out.println(str);
	}
	
	private  static class MyLong2Str implements ObjectSerializer{
		
		private static MyLong2Str instance=new MyLong2Str();
		
		private MyLong2Str(){}

		@Override
		public void write(JSONSerializer serializer, Object object,
				Object fieldName, Type fieldType) throws IOException {
			if(object==null){
				return;
			}
			SerializeWriter out = serializer.getWriter();
			Number value=(Number)object;
			out.write("\""+value.longValue()+"\"");
			
		}
		
	}
	
	private static class Date2Str implements ObjectSerializer{
		public static Date2Str  instance=new Date2Str();
		private SimpleDateFormat sdf=new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss\"");
		private Date2Str(){}
		@Override
		public void write(JSONSerializer serializer, Object object,
				Object fieldName, Type fieldType) throws IOException {
			if(object==null){
				return;
			}
			SerializeWriter out = serializer.getWriter();
			Date value=(Date)object;
			out.write(sdf.format(value));
		}
	}

}
