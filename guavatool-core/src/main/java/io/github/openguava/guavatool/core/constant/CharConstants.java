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
	
	/** 字符串: . */
	public static final char CHAR_DOT = '.';
	
	/** \r\n 字符集合 */
	public static final char[] CHARS_CRLF =  { CHAR_CR, CHAR_LF };
	
	/** 10个数字字符数组 */
	public static final char[] CHARS_NUMBERS = StringConstants.STRING_NUMBERS.toCharArray();
	
	/** 26个字母字符数组  */
	public static final char[] CHARS_LETTERS = StringConstants.STRING_LETTERS.toCharArray();
	
	/** 用于建立十六进制字符的输出的小写字符数组 */
	public static final char[] CHARS_HEX_DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/** 用于建立十六进制字符的输出的大写字符数组 */
	public static final char[] CHARS_HEX_DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
