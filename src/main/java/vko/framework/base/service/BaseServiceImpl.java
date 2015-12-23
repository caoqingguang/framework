package vko.framework.base.service;

import java.util.List;

import vko.framework.base.entity.SuperBaseEntity;
import vko.framework.base.mapper.IBaseMapper;
import vko.framework.mybatis.pager.PagerHelper;
import vko.framework.mybatis.pager.PagerRequest;
import vko.framework.mybatis.pager.PagerResponse;

/**
 * 
 * @author caoqingguang
 *
 * @param <T>
 */
public abstract class BaseServiceImpl<T extends SuperBaseEntity> implements IBaseService<T>{
	
	protected abstract IBaseMapper<T> getMapper();
	@Override
	public void insertObj(T obj) {
		this.getMapper().insertObj(obj);
	}

	@Override
	public void deleteObjById(Long id) {
		this.getMapper().deleteObjById(id);
	}

	@Override
	public void updateObjById(T obj) {
		this.getMapper().updateObjById(obj);
	}

	@Override
	public T selectObjById(Long id) {
		return this.getMapper().selectObjById(id);
	}
	

	@Override
	public List<T> selectObjList(T obj,Object anyObj) {
		return this.getMapper().selectObjList(obj,anyObj);
	}

	@Override
	public T selectObj(T obj,Object other) {
		List<T> result=this.selectObjList(obj, other);
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public PagerResponse<T> queryPager(PagerRequest request, T entity,Object anyObj) {
		PagerHelper.setPagerRequest(request);
		this.getMapper().selectObjList(entity,anyObj);
		return (PagerResponse<T>) PagerHelper.getPagerResponse(entity.getClass());
	}
	
}
