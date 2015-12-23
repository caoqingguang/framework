package vko.framework.mybatis.pager;

import java.io.Serializable;

/**
 * 整合 分页请求和分页响应
 * @author caoqingguang
 *
 * @param <E>
 */
@SuppressWarnings("serial")
public class PagerModel<E> implements Serializable{
	private final PagerResponse<E> response;
	private final PagerRequest request;
	private final int rowFrom;
	private final int rowNum;
	private final String orderStr;
	public PagerModel(PagerRequest pager) {
		super();
		this.request = pager;
		this.rowFrom = (pager.getPageIndex()-1) * pager.getRowNum();
		this.rowNum = pager.getRowNum();
		String orderStr=null;
		if(pager.getOrderCol()!=null && !"".equals(pager.getOrderCol())){
			orderStr = pager.getOrderCol();
			if(pager.getSort()!=null && !"".equals(pager.getSort())){
				orderStr+=" "+ pager.getSort();
			}
		} 
		this.orderStr=orderStr;
		this.response=new PagerResponse<E>(request);
	}
	public int getRowFrom() {
		return rowFrom;
	}
	public int getRowNum() {
		return rowNum;
	}
	public String getOrderStr() {
		return orderStr;
	}
	
	public PagerResponse<E> getResponse(){
		return this.response;
	}
	
	@Override
	public String toString() {
		return "PagerModel [response=" + response + ", request=" + request
				+ ", rowFrom=" + rowFrom + ", rowNum=" + rowNum + ", orderStr="
				+ orderStr + "]";
	}
	
	
	

}
