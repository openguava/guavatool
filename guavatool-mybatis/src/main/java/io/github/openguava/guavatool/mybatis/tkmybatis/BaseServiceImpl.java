package io.github.openguava.guavatool.mybatis.tkmybatis;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.openguava.guavatool.mybatis.common.AbstractMapperUtils;
import io.github.openguava.guavatool.mybatis.common.AbstractServiceImpl;

public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends AbstractServiceImpl<T> implements BaseService<T> {

	@Autowired(required = false)
	private M mapper;
	
	public M getMapper() {
		return this.mapper;
	}
	
	private AbstractMapperUtils<T> mapperUtils;
	
	/**
	 *  获取 mapper 工具类
	 * @return
	 */
	public AbstractMapperUtils<T> getMapperUtils() {
		if(this.mapperUtils == null) {
			this.mapperUtils = new MapperUtils<T>(this.getMapper());
		}
		return this.mapperUtils;
	}
}
