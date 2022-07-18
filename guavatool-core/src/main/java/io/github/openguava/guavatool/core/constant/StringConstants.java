package io.github.openguava.guavatool.core.constant;

/**
 * 字符串常量
 * @author openguava
 *
 */
public class StringConstants {

	/** 空字符 */
	public static final String STRING_EMPTY = "";
	
	/** 空json字符串  */
	public static final String STRING_EMPTY_JSON = "{}";
	
	/** 空格字符串 */
	public static final String STRING_SPACE = String.valueOf(CharConstants.CHAR_SPACE);
	
	/** \r\n 换行字符串  */
	public static final String STRING_CRLF = new String(CharConstants.CHARS_CRLF);
	
	/** 10个数字字符串 */
	public static final String STRING_NUMBERS = "0123456789";
	
	/** 26个字母字符串 */
	public static final String STRING_LETTERS = "abcdefghijklmnopqrstuvwxyz";
	
	/** 空字符数组 */
	public static final String[] STRING_EMPTY_ARRAY = {};
}
