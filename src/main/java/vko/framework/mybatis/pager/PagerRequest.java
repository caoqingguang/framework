package vko.framework.mybatis.pager;

import java.io.Serializable;
/**
 * 分页请求对象
 * @author caoqingguang
 *
 */
@SuppressWarnings("serial")
public class PagerRequest implements Serializable{
	private static final int pageIndex_def=1;
	private static final int rowNum_def=15;
	private int pageIndex=pageIndex_def;
	private int rowNum=rowNum_def;
	private String orderCol;
	private String sort;
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getOrderCol() {
		return orderCol;
	}
	public void setOrderCol(String orderCol) {
		this.orderCol = orderCol;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public <E> PagerModel<E> genModel(Class<E> clazz){
		PagerModel<E> model=new PagerModel<E>(this);
		return model;
	}
	
	@Override
	public String toString() {
		return "PagerRequest [pageIndex=" + pageIndex + ", rowNum=" + rowNum
				+ ", orderCol=" + orderCol + ", sort=" + sort + "]";
	}
	
	
	
}
