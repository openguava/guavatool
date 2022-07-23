package io.github.openguava.guavatool.mybatis.mybatisplus;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.openguava.guavatool.mybatis.common.AbstractMapperUtils;
import io.github.openguava.guavatool.core.constant.SqlConstants;
import io.github.openguava.guavatool.core.lang.PageResult;
import io.github.openguava.guavatool.core.util.CollectionUtils;
import io.github.openguava.guavatool.core.util.StringUtils;
import io.github.openguava.guavatool.mybatis.QueryCondition;
import io.github.openguava.guavatool.mybatis.QueryPage;

public class MapperUtils<T> extends AbstractMapperUtils<T> {

	private BaseMapper<T> mapper;
	
	public BaseMapper<T> getMapper() {
		return this.mapper;
	}
	
	public MapperUtils(BaseMapper<T> mapper) {
		this.mapper = mapper;
	}
	
	@Override
	public int saveEntity(T entity) {
		return this.mapper.insert(entity);
	}

	@Override
	public int updateEntity(T entity) {
		return this.mapper.updateById(entity);
	}

	@Override
	public int deleteById(Serializable id) {
		return this.mapper.deleteById(id);
	}

	@Override
	public int deleteByIds(Collection<? extends Serializable> ids) {
		return this.mapper.deleteBatchIds(ids);
	}
	
	@Override
	public T getEntityById(Serializable id) {
		return this.mapper.selectById(id);
	}
	
	@Override
	public PageResult<T> getEntityByPage(QueryPage queryPage) {
		PageResult<T> resultPage = new PageResult<>();
		List<T> list = null;
		QueryWrapper<T> queryWrapper = getQueryWrapper(queryPage);
		if(queryPage.getPageNo() == null || queryPage.getPageSize() == null) {
			list = this.mapper.selectList(queryWrapper);
		} else {
			Page<T> p = new Page<>(queryPage.getPageNo(), queryPage.getPageSize());
			Page<T> page = this.mapper.selectPage(p, queryWrapper);
			resultPage.setPageNo(queryPage.getPageNo().longValue());
			resultPage.setPageSize(queryPage.getPageSize().longValue());
			resultPage.setDataCount(page.getTotal());
			list = page.getRecords();
		}
		resultPage.setData(list);
		return resultPage;
	}
	
	@Override
	public List<T> getList() {
		return this.mapper.selectList(new QueryWrapper<T>());
	}
	
	@Override
	public Long getCountByMap(Map<String, Object> map) {
		return this.mapper.selectCount(this.getQueryWrapper(map));
	}
	
	@Override
	public List<T> getListByMap(Map<String, Object> map) {
		return this.mapper.selectByMap(map);
	}
	
	@Override
	public Long getCountByCondition(Collection<QueryCondition> conditions) {
		QueryWrapper<T> queryWrapper = this.getQueryWrapper(conditions, null);
		return this.mapper.selectCount(queryWrapper);
	}
	
	@Override
	public List<T> getListByCondition(Collection<QueryCondition> conditions, String orderBy, String... columns) {
		QueryWrapper<T> queryWrapper = this.getQueryWrapper(conditions, orderBy, columns);
		return this.mapper.selectList(queryWrapper);
	}
	
	private TableInfo entityTable;

	/**
	 * 获取实体表对象信息
	 * 
	 * @return
	 */
	public TableInfo getEntityTable() {
		if (this.entityTable == null) {
			Class<?> clazz = this.getEntityClass();
			this.entityTable = SqlHelper.table(clazz);
		}
		return this.entityTable;
	}

	@Override
	public String getTableName() {
		TableInfo tableInfo = this.getEntityTable();
		if (tableInfo != null) {
			return tableInfo.getTableName();
		}
		Class<T> clazz = this.getEntityClass();
		TableName tableName = clazz.getAnnotation(TableName.class);
		if (tableName != null) {
			return tableName.value();
		}
		return clazz.getSimpleName();
	}

	@Override
	public String getIdField() {
		TableInfo tableInfo = this.getEntityTable();
		if (tableInfo != null && tableInfo.havePK()) {
			return tableInfo.getKeyColumn();
		}
		Class<T> clazz = this.getEntityClass();
		for (Field field : clazz.getFields()) {
			TableId tableId = field.getAnnotation(TableId.class);
			if (tableId != null) {
				TableField tableField = field.getAnnotation(TableField.class);
				return tableField != null ? tableField.value() : field.getName();
			}
		}
		return "id";
	}
	
	/**
	 * 创建  QueryWrapper
	 * @param queryMap 查询参数
	 * @return
	 */
	public QueryWrapper<T> getQueryWrapper(Map<String, Object> queryMap) {
		QueryWrapper<T> queryWrapper = this.getQueryWrapper();
		if (queryMap != null && queryMap.size() > 0) {
			for (Entry<String, Object> kv : queryMap.entrySet()) {
				if (kv.getValue() == null) {
					queryWrapper.isNull(true, kv.getKey());
				} else {
					queryWrapper.eq(true, kv.getKey(), kv.getValue());
				}
			}
		}
		return queryWrapper;
	}
	
	/**
	 * 转化为 QueryWrapper
	 * @param <T>
	 * @param queryPage
	 * @return
	 */
	public QueryWrapper<T> getQueryWrapper(QueryPage queryPage) {
		return this.getQueryWrapper(queryPage.getConditions(), queryPage.getOrderByClause(), queryPage.getProperties());
	}
	
	/**
	 * 创建  QueryWrapper
	 * @param conditions 查询条件
	 * @param orderBy 排序
	 * @param columns 过滤字段
	 * @return
	 */
	public QueryWrapper<T> getQueryWrapper(Collection<QueryCondition> conditions, String orderBy, String... columns) {
		QueryWrapper<T> queryWrapper = this.getQueryWrapper();
		// conditions
		if(CollectionUtils.isNotEmpty(conditions)) {
			for(QueryCondition condition : conditions) {
				loadQueryWrapper(condition, queryWrapper);
			}
		}
		// orderBy
		if(StringUtils.isNotEmpty(orderBy)) {
			String[] strs = orderBy.split(",");
			for(String str : strs) {
				if(str.contains("desc") || str.contains("DESC")) {
					queryWrapper.orderByDesc(str.replace("desc", "").replace("DESC", "").trim());
				} else if(str.contains("asc") || str.contains("ASC")) {
					queryWrapper.orderByAsc(str.replace("asc", "").replace("ASC", "").trim());
				} else {
					queryWrapper.orderByDesc(str.trim());
				}
			}
		}
		// columns
		if(CollectionUtils.isNotEmpty(columns)) {
			queryWrapper.select(columns);
		}
		return queryWrapper;
	}
	
	/**
	 * 创建 QueryWrapper 
	 * @return
	 */
	public QueryWrapper<T> getQueryWrapper() {
		return new QueryWrapper<T>();
	}
	
	/**
	 * example 条件操作递归处理
	 * @param condition
	 * @param example
	 * @param criteriaNew
	 * @return
	 */
	private static <T> boolean loadQueryWrapper(QueryCondition condition, QueryWrapper<T> queryWrapper) {
		//是否为空条件
		if(condition.isEmpty()) {
			return false;
		}
		//operator
		String operator = !StringUtils.isEmpty(condition.getOperator()) ? condition.getOperator().toLowerCase() : "";
		
		//trim
		operator = operator.trim();
		
		//and or
		boolean isAnd = SqlConstants.KEYWORD_AND.equalsIgnoreCase(condition.getAndOr());
		
		//字段是否效
		if(!StringUtils.isEmpty(condition.getField())) {
			QueryWrapper<T> wrapper = isAnd ? queryWrapper : queryWrapper.or();
			if(SqlConstants.isOpEq(operator)){
				wrapper.eq(condition.getValue() != null, condition.getField(), condition.getValue());
			}else if(SqlConstants.isOpNe(operator)){
				wrapper.ne(condition.getValue() != null, condition.getField(), condition.getValue());
			}else if(SqlConstants.isOpLike(operator)){
				wrapper.like(condition.getValue() != null, condition.getField(), condition.getValue());
			}else if(SqlConstants.isOpNotLike(operator)){
				wrapper.notLike(condition.getValue() != null, condition.getField(), condition.getValue());
			}else if(SqlConstants.isOpGt(operator)){
				wrapper.gt(condition.getValue() != null, condition.getField(), condition.getValue());
			}else if(SqlConstants.isOpGe(operator)){
				wrapper.ge(condition.getValue() != null, condition.getField(), condition.getValue());
			}else if(SqlConstants.isOpLt(operator)){
				wrapper.lt(condition.getValue() != null, condition.getField(), condition.getValue());
			}else if(SqlConstants.isOpLe(operator)){
				wrapper.le(condition.getValue() != null, condition.getField(), condition.getValue());
			}else if(SqlConstants.isOpIn(operator)){
				wrapper.in(condition.getValue() != null, condition.getField(), (List<?>)condition.getValue());
			}else if(SqlConstants.isOpNotIn(operator)){
				wrapper.notIn(condition.getValue() != null, condition.getField(), (List<?>)condition.getValue());
			}else if(SqlConstants.isOpIsNull(operator)){
				wrapper.isNull(condition.getField());
			}else if(SqlConstants.isOpIsNotNull(operator)){
				wrapper.isNotNull(condition.getField());
			}else if(SqlConstants.isOpBetween(operator)){
				wrapper.between(condition.getValue() != null && condition.getSecondValue() != null,condition.getField(), condition.getValue(), condition.getSecondValue());
			} else if(SqlConstants.isOpNotBetween(operator)){
				wrapper.notBetween(condition.getValue() != null && condition.getSecondValue() != null,condition.getField(), condition.getValue(), condition.getSecondValue());
			} else{
				if(condition.getValue() != null){
					wrapper.apply(condition.getField(), condition.getValue());
				}else{
					wrapper.apply(condition.getField());
				}
			}
		}
		return true;
	}
}
