package io.github.openguava.guavatool.core.constant;

/**
 * sql 关键字
 * @author openguava
 *
 */
public class SqlConstants {
	
	/** sql 常见注入字符 */
	public static final String[] STRINGS_INJECT = new String[] { "'", "--", "#", "/*", "*/"};
	
	/** 关键字修正前缀 */
	public static final String KEYWORD_PREFIX = "`";
	
	/** 关键字修正后缀 */
	public static final String KEYWORD_SUFFIX = "`";
	
	/** select */
	public static final String KEYWORD_SELECT = "SELECT";
	
	/** insert */
	public static final String KEYWORD_INSERT = "INSERT";
	
	/*** into */
	public static final String KEYWORD_INTO = "INTO";
	
	public static final String KEYWORD_INSERT_INTO = KEYWORD_INSERT + " " + KEYWORD_INTO;
	
	public static final String KEYWORD_VALUES = "VALUES";
	
	public static final String KEYWORD_UPDATE = "UPDATE";
	
	public static final String KEYWORD_SET = "SET";
	
	public static final String KEYWORD_DELETE = "DELETE";
	
	public static final String KEYWORD_FROM = "FROM";
	
	public static final String KEYWORD_WHERE = "WHERE";
	
	public static final String KEYWORD_AND = "AND";
	
	public static final String KEYWORD_OR = "OR";
	
	public static final String KEYWORD_GROUP_BY = "GROUP BY";
	
	public static final String KEYWORD_ORDER_BY = "ORDER BY";
	
	public static final String KEYWORD_ASC = "ASC";
	
	public static final String KEYWORD_DESC = "DESC"; 
	
	public static final String KEYWORD_EQ = "=";
	
	public static final String KEYWORD_NE = "<>";
	
	public static final String KEYWORD_LIKE = "LIKE";
	
	public static final String KEYWORD_NOT_LIKE = "NOT LIKE";
	
	public static final String KEYWORD_GT = ">";
	
	public static final String KEYWORD_GE = ">=";
	
	public static final String KEYWORD_LT = "<";
	
	public static final String KEYWORD_LE = "<=";
	
	public static final String KEYWORD_IN = "IN";
	
	public static final String KEYWORD_NOT_IN = "NOT IN";
	
	public static final String KEYWORD_IS = "IS";
	
	public static final String KEYWORD_NULL = "NULL";
	
	public static final String KEYWORD_IS_NULL = "IS NULL";
	
	public static final String KEYWORD_IS_NOT_NULL = "IS NOT NULL";
	
	public static final String KEYWORD_BETWEEN = "BETWEEN";
	
	public static final String KEYWORD_NOT_BETWEEN = "NOT BETWEEN";
	
	/**
	 * 是否 = 操作符号
	 * @param op
	 * @return
	 */
	public static boolean isOpEq(String op) {
		return op != null && "=".equals(op.trim());
	}
	
	/**
	 * 是否 <>  操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpNe(String op) {
		return op != null && ("!=".equals(op.trim()) || "<>".equals(op.trim()));
	}
	
	/**
	 * 是否 like 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpLike(String op) {
		return op != null && "like".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 not like 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpNotLike(String op) {
		return op != null && "not like".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 > 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpGt(String op) {
		return op != null && ">".equals(op.trim());
	}
	
	/**
	 * 是否 >= 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpGe(String op) {
		return op != null && ">=".equals(op.trim());
	}
	
	/**
	 * 是否 <  操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpLt(String op) {
		return op != null && "<".equals(op.trim());
	}
	
	/**
	 * 是否 <= 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpLe(String op) {
		return op != null && "<=".equals(op.trim());
	}
	
	/**
	 * 是否 in 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpIn(String op) {
		return op != null && "in".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 not in 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpNotIn(String op) {
		return op != null && "not in".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 is null 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpIsNull(String op) {
		return op != null && ("isnull".equalsIgnoreCase(op.trim()) || "is null".equalsIgnoreCase(op.trim()));
	}
	
	/**
	 * 是否 is not null 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpIsNotNull(String op) {
		return op != null && "is not null".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 between 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpBetween(String op) {
		return op != null && "between".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 not between 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpNotBetween(String op) {
		return op != null && "not between".equalsIgnoreCase(op.trim());
	}
}
