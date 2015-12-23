package vko.framework.base.entity;

import java.io.Serializable;
/**
 * entity 
 * @author caoqingguang
 *
 */

@SuppressWarnings("serial")
public class SuperBaseEntity implements Serializable {
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

}
