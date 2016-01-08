package vko.framework.base.json;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public  class Long2Str implements ObjectSerializer{
		
		private static Long2Str instance=new Long2Str();
		private Long2Str(){}
		public static Long2Str getInstance(){
			return instance;
		}
		@Override
		public void write(JSONSerializer serializer, Object object,
				Object fieldName, Type fieldType) throws IOException {
			SerializeWriter out = serializer.getWriter();
			Number value=(Number)object;
			if(value==null){
				return;
			}
			out.write("\""+value.longValue()+"\"");
			
		}
		
}