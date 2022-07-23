package io.github.openguava.guavatool.mybatis.beetlsql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.sql.annotation.entity.AssignID;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Column;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.query.Query;

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
		this.mapper.insert(entity);
		return 1;
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
		int count = 0;
		for(Object id : ids) {
			count += this.mapper.deleteById(id);
		}
		return count;
	}

	@Override
	public T getEntityById(Serializable id) {
		return this.mapper.single(id);
	}

	@Override
	public PageResult<T> getEntityByPage(QueryPage queryPage) {
		PageResult<T> resultPage = new PageResult<>();
		List<T> list = null;
		Query<T> query = this.getQuery(queryPage.getConditions(), queryPage.getOrderByClause(), queryPage.getProperties());
		if(queryPage.getPageNo() == null || queryPage.getPageSize() == null) {
			list = query.select();
		} else {
			org.beetl.sql.core.page.PageResult<T> result =  query.page(queryPage.getPageNo(), queryPage.getPageSize(), queryPage.getProperties());
			resultPage.setPageNo(queryPage.getPageNo().longValue());
			resultPage.setPageSize(queryPage.getPageSize().longValue());
			resultPage.setDataCount(result.getTotalRow());
			list = result.getList();
		}
		resultPage.setData(list);
		return resultPage;
	}

	@Override
	public List<T> getList() {
		return this.mapper.all();
	}

	@Override
	public Long getCountByMap(Map<String, Object> map) {
		return this.getQuery(map).count();
	}

	@Override
	public List<T> getListByMap(Map<String, Object> map) {
		return this.getQuery(map).select();
	}

	@Override
	public Long getCountByCondition(Collection<QueryCondition> conditions) {
		return this.getQuery(conditions, null).count();
	}

	@Override
	public List<T> getListByCondition(Collection<QueryCondition> conditions, String orderBy, String... columns) {
		return this.getQuery(conditions, orderBy, columns).select();
	}

	@Override
	public String getTableName() {
		Class<T> clazz = this.getEntityClass();
		// Table
		Table tableName = clazz.getAnnotation(Table.class);
		if (tableName != null) {
			return tableName.name();
		}
		return clazz.getSimpleName();
	}

	@Override
	public String getIdField() {
		Class<T> clazz = this.getEntityClass();
		for (Field field : clazz.getFields()) {
			// column
			Column column = field.getAnnotation(Column.class);
			// assignID
			AssignID assignID = field.getAnnotation(AssignID.class);
			if (assignID != null) {
				return column != null ? column.value() : field.getName();
			}
			// autoID
			AutoID id = field.getAnnotation(AutoID.class);
			if (id != null) {
				return column != null ? column.value() : field.getName();
			}
		}
		return "id";
	}

	/**
	 * 获取 Query
	 * 
	 * @param queryMap
	 * @return
	 */
	public Query<T> getQuery(Map<String, Object> queryMap) {
		Query<T> query = this.getQuery();
		if (queryMap != null && queryMap.size() > 0) {
			for (Entry<String, Object> kv : queryMap.entrySet()) {
				if (kv.getValue() == null) {
					query.andIsNull(kv.getKey());
				} else {
					query.andEq(kv.getKey(), kv.getValue());
				}
			}
		}
		return query;
	}

	/**
	 * 获取 Query
	 * 
	 * @param conditions
	 * @param orderBy
	 * @param columns
	 * @return
	 */
	public Query<T> getQuery(Collection<QueryCondition> conditions, String orderBy, String... columns) {
		Query<T> query = this.getQuery();
		// conditions
		if (CollectionUtils.isNotEmpty(conditions)) {
			for (QueryCondition condition : conditions) {
				loadQuery(condition, query);
			}
		}
		// orderBy
		if (StringUtils.isNotEmpty(orderBy)) {
			query.orderBy(orderBy);
		}
		// columns
		if (CollectionUtils.isNotEmpty(columns)) {
			query.select(columns);
		}
		return query;
	}

	/**
	 * 获取 Query
	 * 
	 * @return
	 */
	public Query<T> getQuery() {
		return this.mapper.getSQLManager().query(this.getEntityClass());
	}

	/**
	 * example 条件操作递归处理
	 * 
	 * @param condition
	 * @param example
	 * @param criteriaNew
	 * @return
	 */
	private static <T> boolean loadQuery(QueryCondition condition, Query<T> query) {
		// 是否为空条件
		if (condition.isEmpty()) {
			return false;
		}
		// operator
		String operator = !StringUtils.isEmpty(condition.getOperator()) ? condition.getOperator().toLowerCase() : "";

		// trim
		operator = operator.trim();

		// prefix
		boolean hasPrefix = !StringUtils.isEmpty(condition.getField()) && condition.getField().contains(".");
				
		// and or
		boolean isAnd = SqlConstants.KEYWORD_AND.equalsIgnoreCase(condition.getAndOr());

		// 字段是否效
		if (!StringUtils.isEmpty(condition.getField())) {

			// 字段是否包含前缀
			if (hasPrefix) {
				if (QueryCondition.OPERATOR_MAP.containsKey(operator)) {
					String opt = QueryCondition.OPERATOR_MAP.get(operator);
					if (isAnd) {
						if (condition.getValue() == null) {
							//criteriaNew.andCondition(condition.getField() + " " + opt);
						} else {
							//criteriaNew.andCondition(condition.getField() + " " + opt, condition.getValue());
						}
					} else {
						if (condition.getValue() == null) {
							//criteriaNew.orCondition(condition.getField() + " " + opt);
						} else {
							//criteriaNew.orCondition(condition.getField() + " " + opt, condition.getValue());
						}
					}
				} else {
					if (isAnd) {
						if (condition.getValue() == null) {
							//criteriaNew.andCondition(condition.getField());
						} else {
							//criteriaNew.andCondition(condition.getField(), condition.getValue());
						}
					} else {
						if (condition.getValue() == null) {
							//criteriaNew.orCondition(condition.getField());
						} else {
							//criteriaNew.orCondition(condition.getField(), condition.getValue());
						}
					}
				}
			} else {
				if (SqlConstants.isOpEq(operator)) {
					if (isAnd) {
						query.andEq(condition.getField(), condition.getValue());
					} else {
						query.orEq(condition.getField(), condition.getValue());
					}
				} else if (SqlConstants.isOpNe(operator)) {
					if (isAnd) {
						query.andNotEq(condition.getField(), condition.getValue());
					} else {
						query.orNotEq(condition.getField(), condition.getValue());
					}
				} else if (SqlConstants.isOpLike(operator)) {
					if (isAnd) {
						query.andLike(condition.getField(), condition.getValue().toString());
					} else {
						query.orLike(condition.getField(), condition.getValue().toString());
					}
				} else if (SqlConstants.isOpNotLike(operator)) {
					if (isAnd) {
						query.andNotLike(condition.getField(), condition.getValue().toString());
					} else {
						query.orNotLike(condition.getField(), condition.getValue().toString());
					}
				} else if (SqlConstants.isOpGt(operator)) {
					if (isAnd) {
						query.andGreat(condition.getField(), condition.getValue());
					} else {
						query.orGreat(condition.getField(), condition.getValue());
					}
				} else if (SqlConstants.isOpGe(operator)) {
					if (isAnd) {
						query.andGreatEq(condition.getField(), condition.getValue());
					} else {
						query.orGreatEq(condition.getField(), condition.getValue());
					}
				} else if (SqlConstants.isOpLt(operator)) {
					if (isAnd) {
						query.andLess(condition.getField(), condition.getValue());
					} else {
						query.orLess(condition.getField(), condition.getValue());
					}
				} else if (SqlConstants.isOpLe(operator)) {
					if (isAnd) {
						query.andLessEq(condition.getField(), condition.getValue());
					} else {
						query.orLessEq(condition.getField(), condition.getValue());
					}
				} else if (SqlConstants.isOpIn(operator)) {
					if (isAnd) {
						query.andIn(condition.getField(), (List<?>) condition.getValue());
					} else {
						query.orIn(condition.getField(), (List<?>) condition.getValue());
					}
				} else if (SqlConstants.isOpNotIn(operator)) {
					if (isAnd) {
						query.andNotIn(condition.getField(), (List<?>) condition.getValue());
					} else {
						query.orNotIn(condition.getField(), (List<?>) condition.getValue());
					}
				} else if (SqlConstants.isOpIsNull(operator)) {
					if (isAnd) {
						query.andIsNull(condition.getField());
					} else {
						query.orIsNull(condition.getField());
					}
				} else if (SqlConstants.isOpIsNotNull(operator)) {
					if (isAnd) {
						query.andIsNotNull(condition.getField());
					} else {
						query.orIsNotNull(condition.getField());
					}
				} else if (SqlConstants.isOpBetween(operator)) {
					if (isAnd) {
						query.andBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					} else {
						query.orBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					}
				}  else if (SqlConstants.isOpNotBetween(operator)) {
					if (isAnd) {
						query.andNotBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					} else {
						query.orNotBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					}
				} else {
					if (condition.getValue() != null) {
						if (isAnd) {
							//criteriaNew.andCondition(condition.getField(), condition.getValue());
						} else {
							//criteriaNew.orCondition(condition.getField(), condition.getValue());
						}
					} else {
						if (isAnd) {
							query.and(query.condition().appendSql(condition.getField()));
							//criteriaNew.andCondition(condition.getField());
						} else {
							query.or(query.condition().appendSql(condition.getField()));
							//criteriaNew.orCondition(condition.getField());
						}
					}
				}
			}
		}
		return true;
	}
}
