package io.github.openguava.guavatool.core.constant;

import java.util.regex.Pattern;

import io.github.openguava.guavatool.core.cache.SimpleCache;
import io.github.openguava.guavatool.core.lang.FuncR;

/**
 * 正则模式常量
 * @author openguava
 *
 */
public class PatternConstants {

	private static final SimpleCache<RegexWithFlag, Pattern> CACHE = new SimpleCache<>();
	
	/** 英文字母 、数字和下划线 */
	public final static Pattern PATTERN_GENERAL = Pattern.compile("^\\w+$");
	
	/** 数字 */
	public final static Pattern PATTERN_NUMBERS = Pattern.compile("\\d+");
	
	/** 字母 */
	public final static Pattern PATTERN_LETTERS = Pattern.compile("[a-zA-Z]+");
	
	/** 单个中文汉字 */
	public final static Pattern PATTERN_CHINESE = Pattern.compile("[\u4E00-\u9FFF]");
	
	/** 中文汉字 */
	public final static Pattern PATTERN_CHINESES = Pattern.compile("[\u4E00-\u9FFF]+");
	
	/** 邮箱 */
	public final static Pattern PATTERN_EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);
	
	/** 邮编 */
	public final static Pattern PATTERN_ZIP_CODE = Pattern.compile("[1-9]\\d{5}(?!\\d)");
	
	/** 18位身份证号码 */
	public final static Pattern PATTERN_CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)");
	
	/** 生日 */
	public final static Pattern PATTERN_BIRTHDAY = Pattern.compile("^(\\d{2,4})([/\\-.年]?)(\\d{1,2})([/\\-.月]?)(\\d{1,2})日?$");
	
	/** IP v4 */
	public final static Pattern PATTERN_IPV4 = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

	/** MAC 地址 */
	public static final Pattern PATTERN_MAC_ADDRESS = Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE);

	/**
	 * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
	 *
	 * @param regex 正则表达式
	 * @return {@link Pattern}
	 */
	public static Pattern getPattern(String regex) {
		return getPattern(regex, 0);
	}

	/**
	 * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
	 *
	 * @param regex 正则表达式
	 * @param flags 正则标识位集合 {@link Pattern}
	 * @return {@link Pattern}
	 */
	public static Pattern getPattern(final String regex, final int flags) {
		final RegexWithFlag regexWithFlag = new RegexWithFlag(regex, flags);
		return CACHE.get(regexWithFlag, new FuncR<Pattern>() {
			
			@Override
			public Pattern call() {
				return Pattern.compile(regex, flags);
			}
		});
	}

	/**
	 * 移除缓存
	 *
	 * @param regex 正则
	 * @param flags 标识
	 * @return 移除的{@link Pattern}，可能为{@code null}
	 */
	public static Pattern removePattern(String regex, int flags) {
		return CACHE.remove(new RegexWithFlag(regex, flags));
	}

	/**
	 * 清空缓存池
	 */
	public static void clearPattern() {
		CACHE.clear();
	}
	
	/**
	 * 正则表达式和正则标识位的包装
	 *
	 * @author openguava
	 */
	private static class RegexWithFlag {
		
		private final String regex;
		
		private final int flag;

		/**
		 * 构造
		 *
		 * @param regex 正则
		 * @param flag  标识
		 */
		public RegexWithFlag(String regex, int flag) {
			this.regex = regex;
			this.flag = flag;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + flag;
			result = prime * result + ((regex == null) ? 0 : regex.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			RegexWithFlag other = (RegexWithFlag) obj;
			if (flag != other.flag) {
				return false;
			}
			if (regex == null) {
				return other.regex == null;
			} else {
				return regex.equals(other.regex);
			}
		}
	}
}
