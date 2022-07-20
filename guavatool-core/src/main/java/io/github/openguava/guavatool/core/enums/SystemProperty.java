package io.github.openguava.guavatool.core.enums;

import io.github.openguava.guavatool.core.constant.StringConstants;
import io.github.openguava.guavatool.core.util.LogUtils;

/**
 * 系统属性枚举
 * @author openguava
 *
 */
public enum SystemProperty {
	/** 操作系统名称 */
	OS_NAME("os.name"),
	/** 操作系统的架构 */
	OS_ARCH("os.arch"),
	/** 操作系统版本号 */
	OS_VERSION("os.version"),
	/** JAVA_HOME */
	JAVA_HOME("java.home"),
	/** java版本号  */
	JAVA_VERSION("java.version"),
	/** java运行时版本 */
	JAVA_RUNTIME_VERSION("java.runtime.version"),
	JAVA_VM_VERSION("java.vm.version"),
	/** java输入输出临时路径 */
	JAVA_IO_TMPDIR("java.io.tmpdir"),
	JAVA_EXT_DIRS("java.ext.dirs"),
	/** java类路径 */
	JAVA_CLASS_PATH("java.class.path"),
	/** java类版本号 */
	JAVA_CLASS_VERSION("java.class.version"),
	/** 操作系统用户名 */
	USER_NAME("user.name"),
	/** 操作系统用户的主目录 */
	USER_HOME("user.home"),
	/** 当前程序所在目录 */
	USER_DIR("user.dir"),
	/** 文件分隔符 */
	FILE_SEPARATOR("file.separator"),
	/** 路径分隔符 */
	PATH_SEPARATOR("path.separator"),
	/** 换行分隔符 */
	LINE_SEPARATOR("line.separator"),
	/** 文件编码 */
	FILE_ENCODING("file.encoding"),
	;
	
	private String name;
	
	/**
	 * 获取属性名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	SystemProperty(String name) {
		this.name = name;
	}
	
	/**
	 * 获取属性值
	 * @param quiet
	 * @return
	 */
	public String getValue(boolean quiet) {
		try {
			return System.getProperty(this.name);
		} catch (SecurityException e) {
			if(!quiet) {
				throw new SecurityException(e);
			} else {
				LogUtils.error(SystemProperty.class, e.getMessage(), e);
				return null;
			}
		}
	}
	
	@Override
	public String toString() {
		String value = this.getValue(true);
		return value != null ? value : StringConstants.STRING_EMPTY;
	}
	
	public static void main(String[] args) {
		System.out.println(SystemProperty.LINE_SEPARATOR);
	}
}
