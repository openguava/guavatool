package io.github.openguava.guavatool.mybatis.fluentmybatis;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.openguava.guavatool.mybatis.common.AbstractMapperUtils;
import io.github.openguava.guavatool.mybatis.common.AbstractServiceImpl;

import cn.org.atool.fluent.mybatis.base.IEntity;

public class BaseServiceImpl<M extends BaseMapper<T>, T extends IEntity> extends AbstractServiceImpl<T> implements BaseService<T> {

	@Autowired(required = false)
	private M mapper;
	
	public M getMapper() {
		return this.mapper;
	}
	
	private AbstractMapperUtils<T> mapperUtils;
	
	@Override
	public AbstractMapperUtils<T> getMapperUtils() {
		if(this.mapperUtils == null) {
			this.mapperUtils = new MapperUtils<>(this.getMapper());
		}
		return this.mapperUtils;
	}
}
