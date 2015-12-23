package vko.framework.base.service;

import java.util.List;

import vko.framework.base.entity.SuperBaseEntity;
import vko.framework.mybatis.pager.PagerRequest;
import vko.framework.mybatis.pager.PagerResponse;

/**
 * 
 * @author caoqingguang
 *
 * @param <T>
 */
public interface IBaseService<T extends SuperBaseEntity>{
	void insertObj(T obj);
	void deleteObjById(Long id);
	void updateObjById(T obj);
	T selectObjById(Long id);
	List<T> selectObjList(T obj,Object other);
	T selectObj(T obj,Object other);
	PagerResponse<T> queryPager(PagerRequest request,T entity,Object anyObj);
}
