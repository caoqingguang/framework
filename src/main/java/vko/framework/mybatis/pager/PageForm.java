package vko.framework.mybatis.pager;

/**
 * 分页适配  适配老的分页请求表单
 * @author caoqingguang
 *
 */
public class PageForm {
	private Integer page;
	private Integer rows;
	private String sort;
	private String order;
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	public PagerRequest genRequest(){
		PagerRequest request=new PagerRequest();
		if(this.page!=null&&this.page>0){
			request.setPageIndex(this.page);
		}
		if(this.rows!=null&&this.rows>0){
			request.setRowNum(this.rows);
		}
		if(this.order!=null&&this.order!=""){
			request.setSort(this.order);
		}
		if(this.sort!=null&&this.sort!=""){
			request.setOrderCol(this.sort);
		}
		return request;
	}

}
