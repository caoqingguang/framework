package vko.framework.base.json;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonHelper {
	private final static SerializeConfig mapping=new SerializeConfig();
    static{
    	mapping.put(Long.class, Long2Str.getInstance());
    	mapping.put(Date.class, Date2Str.getInstance());
    }
    public static String toJSONString(Object object, SerializerFeature... features){
    	return JSON.toJSONString(object, mapping, features);
    }
    public static String toJSONString(Object object){
    	return JSON.toJSONString(object, mapping);
    }
}
