package io.github.openguava.guavatool.core.constant;

/**
 * 字符常量
 * @author openguava
 *
 */
public class CharConstants {

	/** 空格字符 */
	public static final char CHAR_SPACE = ' ';
	
	/** tab制表符 字符*/
	public static final char CHAR_TAB = '	';
	
	/** 斜杠 */
	public static final char CHAR_SLASH = '/';
	
	/** 反斜杠 */
	public static final char CHAR_BACKSLASH = '\\';
	
	/** \r 字符 */
	public static final char CHAR_CR = '\r';
	
	/** \n 字符 */
	public static final char CHAR_LF = '\n';
	
	/** 字符常量：花括号（左） <code>'{'</code> */
	public static final char CHAR_DELIM_START = '{';
	
	/**  字符常量：花括号（右） <code>'}'</code> */
	public static final char CHAR_DELIM_END = '}';
	
	/** 字符常量：中括号（左） {@code '['} */
	public static final char CHAR_BRACKET_START = '[';
	
	/** 字符常量：中括号（右） {@code ']'} */
	public static final char CHAR_BRACKET_END = ']';
	
	/** \r\n 字符集合 */
	public static final char[] CHARS_CRLF =  { CHAR_CR, CHAR_LF };
	
	/** 10个数字字符数组 */
	public static final char[] CHARS_NUMBERS = StringConstants.STRING_NUMBERS.toCharArray();
	
	/** 26个字母字符数组  */
	public static final char[] CHARS_LETTERS = StringConstants.STRING_LETTERS.toCharArray();
}
