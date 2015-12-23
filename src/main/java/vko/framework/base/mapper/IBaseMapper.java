package vko.framework.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import vko.framework.base.entity.SuperBaseEntity;

/**
 * BaseMapper
 * @author caoqingguang
 *
 * @param <T>
 */
public interface IBaseMapper<T extends SuperBaseEntity>{
	void insertObj(T obj);
	void deleteObjById(Long id);
	void updateObjById(T obj);
	T selectObjById(Long id);
	List<T> selectObjList(@Param("obj")T obj,@Param("other")Object other);
}
