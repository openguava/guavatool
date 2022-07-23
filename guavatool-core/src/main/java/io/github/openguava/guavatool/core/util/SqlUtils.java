package io.github.openguava.guavatool.core.util;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.github.openguava.guavatool.core.constant.PatternConstants;
import io.github.openguava.guavatool.core.constant.SqlConstants;
import io.github.openguava.guavatool.core.constant.StringConstants;

/**
 * sql工具类
 * @author openguava
 *
 */
public class SqlUtils {
	
	/**
	 * 是否为常规表名或字段名
	 * @param name
	 * @return
	 */
	public static boolean isGeneralTableOrField(String name) {
		if(name == null || name.length() == 0) {
			return false;
		}
		// 如果不是字母开头，则返回false
		if(!CharUtils.isLetter(name.charAt(0))) {
			return false;
		}
		// 判断是否存在否字母、数字、下划线之外的字符
		if(!RegexUtils.isMatch(PatternConstants.PATTERN_GENERAL, name)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 生成[表]部分sql
	 * @param table 表名称
	 * @return
	 */
	public static String generateTableSql(String table) {
		if(StringUtils.isEmpty(table)) {
			return StringConstants.STRING_EMPTY;
		}
		// 判断是常规表名
		boolean isGeneralTable = isGeneralTableOrField(table);
		StringBuffer str = new StringBuffer();
		if(SqlConstants.KEYWORD_PREFIX.length() > 0 && isGeneralTable) {
			str.append(SqlConstants.KEYWORD_PREFIX);
		}
		str.append(table);
		if(SqlConstants.KEYWORD_SUFFIX.length() > 0 &&isGeneralTable) {
			str.append(SqlConstants.KEYWORD_SUFFIX);
		}
		return str.toString();
	}
	
	/**
	 * 生成[字段]部分sql
	 * @param field 字段名称
	 * @return
	 */
	public static String generateFieldSql(String field) {
		if(StringUtils.isEmpty(field)) {
			return StringConstants.STRING_EMPTY;
		}
		boolean isGeneralField = isGeneralTableOrField(field);
		StringBuffer str = new StringBuffer();
		if(SqlConstants.KEYWORD_PREFIX.length() > 0 && isGeneralField) {
			str.append(SqlConstants.KEYWORD_PREFIX);
		}
		str.append(field);
		if(SqlConstants.KEYWORD_SUFFIX.length() > 0 &&isGeneralField) {
			str.append(SqlConstants.KEYWORD_SUFFIX);
		}
		return str.toString();
	}
	
	/**
	 * 生成多个[字段]部分语句
	 * @param fields
	 * @return
	 */
	public static String generateFieldsSql(String... fields) {
		StringBuffer str = new StringBuffer();
		int index = 0;
		for(String field : fields) {
			if(index > 0) {
				str.append(",");
			}
			str.append(generateFieldSql(field));
			index++;
		}
		return str.toString();
	}
	
	/**
	 * 生成[值]部分sql
	 * @param value
	 * @return
	 */
	public static String generateValueSql(Object value) {
		if(value == null) {
			return SqlConstants.KEYWORD_NULL;
		}
		StringBuffer str = new StringBuffer();
		// 非字符串类型，且能转换为数字
		if(!(value instanceof String) && NumberUtils.isNumber(value.toString())) {
			str.append(value);
		}
		// 日期类型判断
		if(value instanceof Date) {
			str.append("'");
			str.append(DateUtils.format((Date)value, DateUtils.FORMAT_DATETIME));
			str.append("'");
			return str.toString();
		}
		// 字符串处理
		str.append("'");
		// 清洗字符串值
		String strValue = value.toString();
		for(String injectStr : SqlConstants.STRINGS_INJECT) {
			// 去除引起注入字符串
			if(strValue.contains(injectStr)) {
				strValue = strValue.replace(injectStr, StringConstants.STRING_EMPTY);
			}
		}
		str.append(strValue);
		str.append("'");
		return str.toString();
	}
	
	/**
	 * 生成[select]部分语句
	 * <pre>示例：SELECT * FROM TABLE</pre>
	 * @param fields
	 * @param table
	 * @return
	 */
	public static String generateSelectSql(String table, String... fields) {
		StringBuffer sql = new StringBuffer();
		sql.append(SqlConstants.KEYWORD_SELECT);
		sql.append(" ");
		if(fields != null && fields.length > 0) {
			sql.append(generateFieldsSql(fields));
		} else {
			sql.append("*");
		}
		sql.append(" ");
		sql.append(SqlConstants.KEYWORD_FROM);
		sql.append(" ");
		sql.append(generateTableSql(table));
		return sql.toString();
	}
	
	/**
	 * 生成[insert]语句
	 * @param table
	 * @param values
	 * @param expValue
	 * @return
	 */
	public static String generateInsertSql(String table, Map<String, Object> values, boolean expValue) {
		StringBuffer valueSql = new StringBuffer();
		String[] fields = new String[values != null ? values.size() : 0];
		if(values != null && !values.isEmpty()) {
			int index = -1;
			for(Entry<String, Object> kv : values.entrySet()) {
				if(kv.getValue() == null) {
					continue;
				}
				fields[++index] = generateFieldSql(kv.getKey());
				if(index > 0) {
					valueSql.append(", ");
				}
				if(expValue) {
					valueSql.append("#{");
					valueSql.append(kv.getKey());
					valueSql.append("}");
				} else {
					valueSql.append(generateWhereEqualSql(kv.getValue(), false));
				}
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append(SqlConstants.KEYWORD_INSERT_INTO);
		sql.append(" ");
		sql.append(generateTableSql(table));
		sql.append("(");
		sql.append(generateFieldsSql(fields));
		sql.append(")");
		sql.append(" ");
		sql.append(SqlConstants.KEYWORD_VALUES);
		sql.append("(");
		sql.append(valueSql.toString());
		sql.append(")");
		return sql.toString();
	}
	
	/**
	 * 生成[update]部分语句
	 * @param table 表名称
	 * @param values 要更新的值
	 * @param whereParams where条件
	 * @param expValue 值为表达式
	 * @return
	 */
	public static String generateUpdateSql(String table, Map<String, Object> values, Map<String, Object> whereParams, boolean expValue) {
		StringBuffer sql = new StringBuffer();
		sql.append(SqlConstants.KEYWORD_UPDATE);
		sql.append(" ");
		sql.append(generateTableSql(table));
		sql.append(" ");
		sql.append(SqlConstants.KEYWORD_SET);
		sql.append(" ");
		if(values != null && !values.isEmpty()) {
			int index = 0;
			for(Entry<String, Object> kv : values.entrySet()) {
				if(index > 0) {
					sql.append(", ");
				}
				// 忽略非常规字段
				if(!isGeneralTableOrField(kv.getKey())) {
					continue;
				}
				sql.append(generateFieldSql(kv.getKey()));
				sql.append(" ");
				if(expValue) {
					sql.append("= ");
					sql.append("#{");
					sql.append(kv.getKey());
					sql.append("}");
				} else {
					sql.append("= ");
					sql.append(generateWhereEqualSql(kv.getValue(), false));
				}
				index++;
			}
		}
		sql.append(" ");
		sql.append(SqlConstants.KEYWORD_WHERE);
		sql.append(" ");
		if(whereParams != null && !whereParams.isEmpty()) {
			sql.append(generateWhereSqlByMap(whereParams, expValue, false));
		}
		return sql.toString();
	}
	
	/**
	 * 根据Map生成where语句
	 * @param params Map参数
	 * @param expValue
	 * @param includeWhereChar 是否包含where字符
	 * @return
	 */
	public static String generateWhereSqlByMap(Map<String, Object> params, boolean expValue, boolean includeWhereChar) {
		StringBuffer sql = new StringBuffer();
		if(includeWhereChar) {
			sql.append(" ");
			sql.append(SqlConstants.KEYWORD_WHERE);
			sql.append(" ");
		}
		if(params != null && !params.isEmpty()) {
			int whereIndex = 0;
			for(Entry<String, Object> param : params.entrySet()) {
				if(whereIndex > 0) {
					sql.append(" ");
					sql.append(SqlConstants.KEYWORD_AND);
					sql.append(" ");
				}
				sql.append(generateFieldSql(param.getKey()));
				sql.append(" ");
				if(expValue) {
					sql.append("= ");
					sql.append("#{");
					sql.append(param.getKey());
					sql.append("}");
				} else {
					sql.append(generateWhereEqualSql(param.getValue(), true));
				}
				whereIndex++;
			}
		}
		return sql.toString();
	}

	/**
	 * 生成where部分"等于"或"in"语句
	 * @param values
	 * @return
	 */
	public static String generateWhereEqualOrInSql(Collection<?> values) {
		if(values == null || values.isEmpty()) {
			return null;
		}
		if(values.size() == 1) {
			return generateWhereEqualSql(values.iterator().next(), true);
		} else {
			return generateWhereInSql(values, true, true);
		}
	}
	
	/**
	 * 生成where部分"等于"语句
	 * <br>如：= 1 或者 IS NULL </br>
	 * @param value 值
	 * @return
	 */
	public static String generateWhereEqualSql(Object value) {
		return generateWhereEqualSql(value, true);
	}
	
	/**
	 * 生成where部分"等于"语句
	 * <br>如：= 1 或者 IS NULL </br>
	 * @param value 值
	 * @param includeEqualChar 是否包含=/is符号
	 * @return
	 */
	public static String generateWhereEqualSql(Object value, boolean includeEqualChar) {
		StringBuffer str = new StringBuffer();
		if(value == null) {
			if(includeEqualChar) {
				str.append(SqlConstants.KEYWORD_IS);
				str.append(" ");
			}
			str.append(SqlConstants.KEYWORD_NULL);
		} else {
			if(includeEqualChar) {
				str.append(SqlConstants.KEYWORD_EQ);
				str.append(" ");
			}
			str.append(generateValueSql(value));
		}
		return str.toString();
	}
	
	/**
	 *生成 where 部分"in"语句
	 *<br>如：IN (1,2,3) 或者 IN ('a','b','c')</br>
	 * @param values
	 * @return
	 */
	public static String generateWhereInSql(Collection<?> values) {
		return generateWhereInSql(values, true, true);
	}
	
	/**
	 * 生成 where 部分"in"语句
	 * <br>如：IN (1,2,3) 或者 IN ('a','b','c')</br>
	 * @param values 值列表
	 * @param includeBracket 是否包含括号
	 * @param includeInChar in标记
	 * @return
	 */
	public static String generateWhereInSql(Collection<?> values, boolean includeBracket, boolean includeInChar) {
		StringBuffer str = new StringBuffer();
		if(includeInChar) {
			str.append(SqlConstants.KEYWORD_IN);
			str.append(" ");
		}
		if(includeBracket) {
			str.append("(");
		}
		int index = 0;
		for(Object value : values) {
			if(index > 0) {
				str.append(",");
			}
			str.append(generateValueSql(value));
			index++;
		}
		if(includeBracket) {
			str.append(")");
		}
		return str.toString();
	}
	
	public static void main(String[] args) {
		Map<String, Object> values = new LinkedHashMap<String, Object>();
		values.put("name", "--/*' or */ '' = '");
		values.put("id", "1");
		values.put("status", null);
		//values.put("1+1", "1");
		System.out.println(generateSelectSql("sys_user", "name", "id"));
		
		System.out.println(generateUpdateSql("sys_user", values, values, true));
		
		System.out.println(generateInsertSql("sys_user", values, false));
	}
}
