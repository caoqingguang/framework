package vko.framework.base.jsonp;



import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import vko.framework.base.json.JsonHelper;

public class FastJsonHttpMessageConverter extends com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter {
	
	private JsonpStore jsonpStore;
	
	public JsonpStore getJsonpStore() {
		return jsonpStore;
	}

	public void setJsonpStore(JsonpStore jsonpStore) {
		this.jsonpStore = jsonpStore;
	}


	@Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		if (obj instanceof JsonpObject) {
			JsonpObject jsonp = (JsonpObject) obj;
		      this.doJsonp(jsonp.getFunction(), jsonp.getJson(), outputMessage);
		} else if(this.jsonpStore.isJsonp()) {
			this.doJsonp(this.jsonpStore.getFunction(),obj, outputMessage);
		} else {
			this.doJson(obj, outputMessage);
		}
    }
    
    private void doJsonp(String function,Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException{
    	OutputStream out = outputMessage.getBody();
	      String text = function + "(" + JsonHelper.toJSONString(obj,getFeatures()) + ")";
	      System.out.println(text);
	      byte[] bytes = text.getBytes(getCharset());
	      out.write(bytes);
    }
    
    private void doJson(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException{
    	OutputStream out = outputMessage.getBody();
	      String text = JsonHelper.toJSONString(obj,getFeatures());
	      System.out.println(text);
	      byte[] bytes = text.getBytes(getCharset());
	      out.write(bytes);
    }
    

}
