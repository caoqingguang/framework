package vko.framework.base.msg;

import java.io.Serializable;

/**
 * 
 * @author caoqingguang
 *
 */
@SuppressWarnings("serial")
public final class JsonMsg implements Serializable{
	
	private String code;
	private String message;
	private Object data;
	private Object dataEx;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public JsonMsg setMessage(String message) {
		this.message = message;
		return this;
	}
	public Object getData() {
		return data;
	}
	public JsonMsg setData(Object data) {
		this.data = data;
		return this;
	}
	
	
	public Object getDataEx() {
		return dataEx;
	}
	public JsonMsg setDataEx(Object dataEx) {
		this.dataEx = dataEx;
		return this;
	}
	public JsonMsg setSuccess(){
		this.code=SUCCESS;
		return this;
	}
	
	public JsonMsg setSuccess(String msg){
		return this.setSuccess().setMessage(msg);
	}
	public JsonMsg setError(){
		this.code=ERROR;
		return this;
	}
	public JsonMsg setError(String msg){
		return this.setError().setMessage(msg);
	}
	
	
	public static final String SUCCESS="success";
	public static final String ERROR="error";
	public static JsonMsg newSuccess(String msg){
		return new JsonMsg().setSuccess().setMessage(msg);
	}
	public static JsonMsg newError(String msg){
		return new JsonMsg().setError().setMessage(msg);
	}
}
