package vko.framework.base.jsonp;

public class JsonpStore {
	private String callbackFlag="callback";
	
	private ThreadLocal<String> functions=new ThreadLocal<String>();

	public String getCallbackFlag() {
		return callbackFlag;
	}

	public void setCallbackFlag(String callbackFlag) {
		this.callbackFlag = callbackFlag;
	}
	
	public void clear(){
		this.functions.remove();
	}
	
	public boolean isJsonp(){
		String callbackValue=functions.get();
		if(callbackValue==null){
			return false;
		}
		if("".equals(callbackValue)){
			return false;
		}
		return true;
	}
	
	public String getFunction(){
		return functions.get();
	}
	public void setFunction(String function){
		functions.set(function);
	}
	

}
