package io.github.openguava.guavatool.core.util;

import java.nio.charset.Charset;

import io.github.openguava.guavatool.core.constant.CharConstants;
import io.github.openguava.guavatool.core.constant.StringConstants;

/**
 * 数据转换工具类 
 * @author openguava
 *
 */
public class ConvertUtils {
	
	/**
	 * 字符串转换为字节数组
	 * @param str
	 * @param charset
	 * @return
	 */
	public static byte[] stringToBytes(CharSequence str, String charset) {
		return stringToBytes(str, StringUtils.isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}
	
	/**
	 * 字符串转换为字节数组
	 * @param str
	 * @param charset
	 * @return
	 */
	public static byte[] stringToBytes(CharSequence str, Charset charset) {
		if (str == null) {
			return null;
		}
		if (charset == null) {
			return str.toString().getBytes(Charset.defaultCharset());
		}
		return str.toString().getBytes(charset);
	}
	
	/**
	 * 字节数组转换为字符串
	 * @param bytes
	 * @param charset
	 * @return
	 */
	public static String bytesToString(byte[] bytes, String charset) {
		return bytesToString(bytes, StringUtils.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}
	
	/**
	 * 字节数组转换为字符串
	 * @param bytes
	 * @param charset
	 * @return
	 */
	public static String bytesToString(byte[] bytes, Charset charset) {
		if (bytes == null) {
			return null;
		}
		if (charset == null) {
			return new String(bytes);
		}
		return new String(bytes, charset);
	}
	
	/**
	 * byte数组转16进制串
	 * @param bytes 被转换的byte数组
	 * @return
	 */
	public static String bytesToHex(byte[] bytes, boolean toLowerCase) {
		if(bytes == null) {
			return null;
		}
		if(bytes.length == 0) {
			return StringConstants.STRING_EMPTY;
		}
		char[] toDigits = toLowerCase ? CharConstants.CHARS_HEX_DIGITS_LOWER : CharConstants.CHARS_HEX_DIGITS_UPPER;
		int l = bytes.length;
		char[] out = new char[l << 1];
		// two characters from the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & bytes[i]) >>> 4];
			out[j++] = toDigits[0x0F & bytes[i]];
		}
		return new String(out);
	}
	
	/**
	 * hex字符串转换为byte数组
	 * @param hex Byte字符串，每个Byte之间没有分隔符
	 * @return
	 */
	public static byte[] hexToBytes(String hex) {
		if(hex == null) {
			return null;
		}
		if(hex.length() == 0) {
			return new byte[0];
		}
		char[] hexData = hex.toCharArray();
		int len = hexData.length;
		if ((len & 0x01) != 0) {
			throw new RuntimeException("Odd number of characters.");
		}
		byte[] out = new byte[len >> 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = Character.digit(hexData[j], 16) << 4; //toDigit(hexData[j], j) << 4;
			j++;
			f = f | Character.digit(hexData[j], 16); //toDigit(hexData[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}
}
