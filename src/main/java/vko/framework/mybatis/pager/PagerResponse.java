package vko.framework.mybatis.pager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 分页响应对象
 * @author caoqingguang
 *
 * @param <E>
 */
@SuppressWarnings("serial")
public class PagerResponse<E> implements Serializable{
	@SuppressWarnings("rawtypes")
	private static final List nullList=new ArrayList(0);
	
	private String sqlMsg;
	private int total;
	private int totalPages;
	private final int pageIndex;
	private final int rowNum;
	private List<E> rows;
	
	
	@SuppressWarnings("unchecked")
	public PagerResponse(PagerRequest request) {
		super();
		this.pageIndex=request.getPageIndex();
		this.rowNum=request.getRowNum();
		this.rows=(List<E>)nullList;
	}

	
	public String getSqlMsg() {
		return sqlMsg;
	}


	public void setSqlMsg(String sqlMsg) {
		this.sqlMsg = sqlMsg;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int totalRows) {
		this.total = totalRows;
		this.totalPages=this.total/this.rowNum;
		if(this.total%this.rowNum>0){
			this.totalPages++;
		}
	}


	public int getTotalPages() {
		return totalPages;
	}



	


	public List<E> getRows() {
		return rows;
	}


	public void setRows(List<E> rows) {
		this.rows = rows;
	}


	public int getPageIndex() {
		return pageIndex;
	}


	public int getRowNum() {
		return rowNum;
	}


	@Override
	public String toString() {
		return "PagerResponse [totalRows=" + total + ", totalPages="
				+ totalPages + ", pageIndex=" + pageIndex + ", rowNum="
				+ rowNum + ", data=" + rows + "]";
	}
	
	
}
