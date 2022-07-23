package io.github.openguava.guavatool.core.util;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.openguava.guavatool.core.constant.PatternConstants;
import io.github.openguava.guavatool.core.constant.StringConstants;

/**
 * 正则工具类
 * @author openguava
 *
 */
public class RegexUtils {
	/**
	 * 验证是否为可用邮箱地址
	 * @param content
	 * @return
	 */
	public static boolean isEmail(String content) {
		return isMatch(PatternConstants.PATTERN_EMAIL, content);
	}
	
	/**
	 * 验证是否为出生日期
	 * @param content
	 * @return
	 */
	public static boolean isBirthday(String content) {
		if (isMatch(PatternConstants.PATTERN_BIRTHDAY, content)) {
			Matcher matcher = PatternConstants.PATTERN_BIRTHDAY.matcher(content);
			if (matcher.find()) {
				int year = Integer.parseInt(matcher.group(1));
				int month = Integer.parseInt(matcher.group(3));
				int day = Integer.parseInt(matcher.group(5));
				return isBirthday(year, month, day);
			}
		}
		return false;
	}
	
	/**
	 * 验证是否为生日
	 * 
	 * @param year 年，从1900年开始计算
	 * @param month 月，从1开始计数
	 * @param day 日，从1开始计数
	 * @return 是否为生日
	 */
	public static boolean isBirthday(int year, int month, int day) {
		// 验证年
		int thisYear = DateUtils.getYear(DateUtils.getDate());
		if (year < 1900 || year > thisYear) {
			return false;
		}

		// 验证月
		if (month < 1 || month > 12) {
			return false;
		}

		// 验证日
		if (day < 1 || day > 31) {
			return false;
		}
		if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
			return false;
		}
		if (month == 2) {
			if (day > 29 || (day == 29 && !DateUtils.isLeapYear(year))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证是否为身份证号码（18位中国）
	 * @param content
	 * @return
	 */
	public static boolean isCitizenId(String content) {
		return isMatch(PatternConstants.PATTERN_CITIZEN_ID, content);
	}
	
	/**
	 * 验证是否为邮政编码（中国）
	 * @param content
	 * @return
	 */
	public static boolean isZipCode(String content) {
		return isMatch(PatternConstants.PATTERN_ZIP_CODE, content);
	}
	
	/**
	 * 验证是否为给定长度范围的英文字母 、数字和下划线
	 * @param content
	 * @param min 最小长度，负数自动识别为0
	 * @param max 最大长度，0或负数表示不限制最大长度
	 * @return
	 */
	public static boolean isGeneral(String content, int min, int max) {
		String reg = "^\\w{" + min + "," + max + "}$";
		if (min < 0) {
			min = 0;
		}
		if (max <= 0) {
			reg = "^\\w{" + min + ",}$";
		}
		return isMatch(reg, content);
	}
	
	/**
	 * 验证该字符串是否是数字
	 * @param content
	 * @return
	 */
	public static boolean isNumber(String content) {
		if (StringUtils.isBlank(content)) {
			return false;
		}
		char[] chars = content.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = (chars[0] == '-') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && chars[start + 1] == 'x') {
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
				// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (!foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				// single trailing decimal point after non-exponent is ok
				return foundDigit;
			}
			if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {
				// not allowing L with an exponent
				return foundDigit && !hasExp;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return !allowSigns && foundDigit;
	}
	
	/**
	 * 验证是否为汉字
	 * @param content
	 * @return
	 */
	public static boolean isChinese(String content) {
		return isMatch("^" + PatternConstants.PATTERN_CHINESE.pattern() + "+$", content);
	}
	
	/**
	 * 获得匹配的字符串，对应分组0表示整个匹配内容，1表示第一个括号分组内容，依次类推
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return
	 */
	public static String get(Pattern pattern, CharSequence content, int groupIndex) {
		if (null == content || null == pattern) {
			return null;
		}

		final Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group(groupIndex);
		}
		return null;
	}
	
	/**
	 * 获得匹配的字符串
	 * @param regex 匹配的正则
	 * @param content 被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return
	 */
	public static String get(String regex, CharSequence content, int groupIndex) {
		if (null == content || null == regex) {
			return null;
		}
		Pattern pattern = PatternConstants.getPattern(regex, Pattern.DOTALL);
		return get(pattern, content, groupIndex);
	}
	
	/**
	 * 指定内容中是否有表达式匹配的内容
	 *
	 * @param regex   正则表达式
	 * @param content 被查找的内容
	 * @return 指定内容中是否有表达式匹配的内容
	 */
	public static boolean contains(String regex, CharSequence content) {
		if (null == regex || null == content) {
			return false;
		}
		final Pattern pattern = PatternConstants.getPattern(regex, Pattern.DOTALL);
		return contains(pattern, content);
	}

	/**
	 * 指定内容中是否有表达式匹配的内容
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @return 指定内容中是否有表达式匹配的内容
	 */
	public static boolean contains(Pattern pattern, CharSequence content) {
		if (null == pattern || null == content) {
			return false;
		}
		return pattern.matcher(content).find();
	}

	/**
	 * 找到指定正则匹配到字符串的开始位置
	 *
	 * @param regex   正则
	 * @param content 字符串
	 * @return 位置，{@code null}表示未找到
	 */
	public static MatchResult indexOf(String regex, CharSequence content) {
		if (null == regex || null == content) {
			return null;
		}
		final Pattern pattern = PatternConstants.getPattern(regex, Pattern.DOTALL);
		return indexOf(pattern, content);
	}

	/**
	 * 找到指定模式匹配到字符串的开始位置
	 *
	 * @param pattern 模式
	 * @param content 字符串
	 * @return 位置，{@code null}表示未找到
	 */
	public static MatchResult indexOf(Pattern pattern, CharSequence content) {
		if (null != pattern && null != content) {
			final Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				return matcher.toMatchResult();
			}
		}
		return null;
	}

	/**
	 * 找到指定正则匹配到第一个字符串的位置
	 *
	 * @param regex   正则
	 * @param content 字符串
	 * @return 位置，{@code null}表示未找到
	 */
	public static MatchResult lastIndexOf(String regex, CharSequence content) {
		if (null == regex || null == content) {
			return null;
		}
		final Pattern pattern = PatternConstants.getPattern(regex, Pattern.DOTALL);
		return lastIndexOf(pattern, content);
	}

	/**
	 * 找到指定模式匹配到最后一个字符串的位置
	 *
	 * @param pattern 模式
	 * @param content 字符串
	 * @return 位置，{@code null}表示未找到
	 */
	public static MatchResult lastIndexOf(Pattern pattern, CharSequence content) {
		MatchResult result = null;
		if (null != pattern && null != content) {
			final Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				result = matcher.toMatchResult();
			}
		}
		return result;
	}
	
	/**
	 * 删除匹配的第一个内容
	 *
	 * @param regex   正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delFirst(String regex, CharSequence content) {
		if (StringUtils.hasBlank(regex, content)) {
			return StringUtils.toString(content);
		}
		final Pattern pattern = PatternConstants.getPattern(regex, Pattern.DOTALL);
		return delFirst(pattern, content);
	}

	/**
	 * 删除匹配的第一个内容
	 *
	 * @param pattern 正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delFirst(Pattern pattern, CharSequence content) {
		return replaceFirst(pattern, content, StringConstants.STRING_EMPTY);
	}

	/**
	 * 替换匹配的第一个内容
	 *
	 * @param pattern     正则
	 * @param content     被匹配的内容
	 * @param replacement 替换的内容
	 * @return 替换后剩余的内容
	 * @since 5.6.5
	 */
	public static String replaceFirst(Pattern pattern, CharSequence content, String replacement) {
		if (null == pattern || StringUtils.isEmpty(content)) {
			return StringUtils.toString(content);
		}
		return pattern.matcher(content).replaceFirst(replacement);
	}

	/**
	 * 删除匹配的最后一个内容
	 *
	 * @param regex 正则
	 * @param str   被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delLast(String regex, CharSequence str) {
		if (StringUtils.hasBlank(regex, str)) {
			return StringUtils.toString(str);
		}
		final Pattern pattern = PatternConstants.getPattern(regex, Pattern.DOTALL);
		return delLast(pattern, str);
	}

	/**
	 * 删除匹配的最后一个内容
	 *
	 * @param pattern 正则
	 * @param str     被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delLast(Pattern pattern, CharSequence str) {
		if (null != pattern && StringUtils.isNotEmpty(str)) {
			final MatchResult matchResult = lastIndexOf(pattern, str);
			if (null != matchResult) {
				return StringUtils.subPre(str, matchResult.start()) + StringUtils.subSuf(str, matchResult.end());
			}
		}
		return StringUtils.toString(str);
	}

	/**
	 * 删除匹配的全部内容
	 *
	 * @param regex   正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delAll(String regex, CharSequence content) {
		if (StringUtils.hasBlank(regex, content)) {
			return StringUtils.toString(content);
		}
		final Pattern pattern = PatternConstants.getPattern(regex, Pattern.DOTALL);
		return delAll(pattern, content);
	}

	/**
	 * 删除匹配的全部内容
	 *
	 * @param pattern 正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delAll(Pattern pattern, CharSequence content) {
		if (null == pattern || StringUtils.isBlank(content)) {
			return StringUtils.toString(content);
		}
		return pattern.matcher(content).replaceAll(StringConstants.STRING_EMPTY);
	}

	/**
	 * 删除正则匹配到的内容之前的字符 如果没有找到，则返回原文
	 *
	 * @param regex   定位正则
	 * @param content 被查找的内容
	 * @return 删除前缀后的新内容
	 */
	public static String delPre(String regex, CharSequence content) {
		if (null == content || null == regex) {
			return StringUtils.toString(content);
		}
		final Pattern pattern = PatternConstants.getPattern(regex, Pattern.DOTALL);
		return delPre(pattern, content);
	}

	/**
	 * 删除正则匹配到的内容之前的字符 如果没有找到，则返回原文
	 *
	 * @param pattern 定位正则模式
	 * @param content 被查找的内容
	 * @return 删除前缀后的新内容
	 */
	public static String delPre(Pattern pattern, CharSequence content) {
		if (null == content || null == pattern) {
			return StringUtils.toString(content);
		}

		final Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return StringUtils.sub(content, matcher.end(), content.length());
		}
		return StringUtils.toString(content);
	}
	
	/**
	 * 给定内容是否匹配正则
	 * @param regex 正则
	 * @param content 内容
	 * @return
	 */
	public static boolean isMatch(String regex, CharSequence content) {
		// 提供null的字符串为不匹配
		if (content == null) {
			return false;
		}
		// 正则不存在则为全匹配
		if (StringUtils.isEmpty(regex)) {
			return true;
		}
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return isMatch(pattern, content);
	}
	
	/**
	 * 给定内容是否匹配正则
	 * @param pattern 模式
	 * @param content 内容
	 * @return
	 */
	public static boolean isMatch(Pattern pattern, CharSequence content) {
		// 提供null的字符串为不匹配
		if (content == null || pattern == null) {
			return false;
		}
		return pattern.matcher(content).matches();
	}
	
	public static void main(String[] args) {
		System.out.println("");
	}
}
