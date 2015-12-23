package vko.framework.mybatis.primarykey;

/**
 * 单表主键 临时存储
 * @author caoqingguang
 *
 */
public class IdsApply {
	private Long id;
	private Long cur;
	
	public IdsApply(Long id, Long cur) {
		super();
		this.id = id;
		this.cur = cur;
	}
	
	public boolean hasNext(){
		return cur>0;
	}
	
	public Long getNext(){
		if(cur<1){
			throw new RuntimeException("id申请出错,资源用尽,请重新申请");
		}
		cur--;
		return id++;
	}
	
}
