package io.github.openguava.guavatool.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;

import io.github.openguava.guavatool.core.constant.CharConstants;
import io.github.openguava.guavatool.core.constant.StringConstants;
import io.github.openguava.guavatool.core.enums.SystemProperty;

/**
 * 文件工具类
 * @author openguava
 *
 */
public class FileUtils {
	
	/**
	 * 特殊后缀
	 */
	private static final CharSequence[] SPECIAL_SUFFIX = {"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};

	/**
	 * 类Unix路径分隔符
	 */
	public static final char UNIX_SEPARATOR = CharConstants.CHAR_SLASH;
	
	/**
	 * Windows路径分隔符
	 */
	public static final char WINDOWS_SEPARATOR = CharConstants.CHAR_BACKSLASH;
	
	/**
	 * 获取文件对象
	 * @param filePath
	 * @return
	 */
	public static File getFile(String filePath) {
		if(filePath == null) {
			return null;
		}
		return new File(filePath);
	}
	
	/**
	 *获取指定路径下的目录和文件
	 * @param dirPath
	 * @param filter
	 * @return
	 */
	public static File[] getFiles(String dirPath, FilenameFilter filter) {
		File dir = getFile(dirPath);
		if(dir == null || !dir.exists() || !dir.isDirectory()) {
			return new File[0];
		}
		if(filter != null) {
			return dir.listFiles(filter);
		} else {
			return dir.listFiles();
		}
	}
	
	/**
	 * 获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName(File file) {
		return file != null ? file.getName() : null;
	}
	
	/**
	 * 返回文件名<br>
	 * <pre>
	 * "d:/test/aaa" 返回 "aaa"
	 * "/test/aaa.jpg" 返回 "aaa.jpg"
	 * </pre>
	 *
	 * @param filePath 文件
	 * @return 文件名
	 */
	public static String getFileName(String filePath) {
		if (filePath == null) {
			return null;
		}
		int len = filePath.length();
		if (len == 0) {
			return filePath;
		}
		if (CharUtils.isFileSeparator(filePath.charAt(len - 1))) {
			// 以分隔符结尾的去掉结尾分隔符
			len--;
		}
		int begin = 0;
		char c;
		for (int i = len - 1; i > -1; i--) {
			c = filePath.charAt(i);
			if (CharUtils.isFileSeparator(c)) {
				// 查找最后一个路径分隔符（/或者\）
				begin = i + 1;
				break;
			}
		}
		return filePath.substring(begin, len);
	}
	
	/**
	 * 返回主文件名
	 *
	 * @param fileName 完整文件名
	 * @return 主文件名
	 */
	public static String getFileNameWithoutExtension(String fileName) {
		String name = getFileName(fileName);
		String ext = getFileExtension(name);
		return StringUtils.isEmpty(ext) ? name : StringUtils.subBefore(name, "." + ext, true);
	}
	
	/**
	 * 获得文件的扩展名（后缀名），扩展名不带“.”
	 *
	 * @param fileName 文件名
	 * @return 扩展名
	 */
	public static String getFileExtension(String fileName) {
		if (fileName == null) {
			return null;
		}
		int index = fileName.lastIndexOf(StringConstants.STRING_DOT);
		if (index == -1) {
			return StringConstants.STRING_EMPTY;
		} else {
			// issue#I4W5FS@Gitee
			int secondToLastIndex = fileName.substring(0, index).lastIndexOf(StringConstants.STRING_DOT);
			String substr = fileName.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
			if (StringUtils.containsAny(substr, SPECIAL_SUFFIX)) {
				return substr;
			}
			String ext = fileName.substring(index + 1);
			// 扩展名中不能包含路径相关的符号
			return StringUtils.containsAny(ext, new char[] { UNIX_SEPARATOR, WINDOWS_SEPARATOR }) ? StringConstants.STRING_EMPTY : ext;
		}
	}

	/**
	 * 获取文件最后修改时间
	 * @param filePath 文件路径
	 * @return
	 */
	public static Date getFileLastModified(String filePath) {
		File file = getFile(filePath);
		return existsFile(file) ? new Date(file.lastModified()) : null;
	}
	
	/**
	 * 文件或目录是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean exists(String path) {
		File file = getFile(path);
		return exists(file);
	}
	
	/**
	 * 文件或目录是否存在
	 * @param file
	 * @return
	 */
	public static boolean exists(File file) {
		return file != null && file.exists();
	}
	
	/**
	 * 文件是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean existsFile(String filePath) {
		File file = getFile(filePath);
		return existsFile(file);
	}
	
	/**
	 * 文件是否存在
	 * @param file
	 * @return
	 */
	public static boolean existsFile(File file) {
		return file != null && file.exists() && file.isFile();
	}
	
	/**
	 * 创建文件
	 * @param filePath
	 * @return
	 */
	public static boolean createFile(String filePath) {
		File file = getFile(filePath);
		if(file == null) {
			return false;
		}
		if(file.exists()) {
			return !file.isDirectory();
		}
		try {
			return file.createNewFile();
		} catch (Exception e) {
			LogUtils.error(FileUtils.class, e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * 目录是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean existsDir(String dirPath) {
		File dir = getFile(dirPath);
		return existsDir(dir);
	}
	
	/**
	 * 目录是否存在
	 * @param dir
	 * @return
	 */
	public static boolean existsDir(File dir) {
		return dir != null && dir.exists() && dir.isDirectory();
	}
	
	/**
	 * 创建目录
	 * @param dirPath
	 * @return
	 */
	public static boolean createDir(String dirPath) {
		File file = getFile(dirPath);
		if(file == null) {
			return false;
		}
		if(file.exists()) {
			return !file.isFile();
		}
		return file.mkdirs();
	}
	
	/**
	 * 获取当前工作用户目录
	 * @return
	 */
	public static String getUserWorkDir() {
		return SystemProperty.USER_DIR.getValue(true);
	}
	
	/**
	 * 获取操作系统用户目录
	 * @return
	 */
	public static String getUserHomeDir() {
		return SystemProperty.USER_HOME.getValue(true);
	}
	
	/**
	 * 获取临时目录
	 * @return
	 */
	public static String getTempDir() {
		return SystemProperty.JAVA_IO_TMPDIR.getValue(true);
	}
	
	/**
	 * 读取字节数组
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(String filePath) throws IOException {
		File file = getFile(filePath);
		return readBytes(file);
	}
	
	/**
	 * 读取字节数组
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(File file) throws IOException {
		if(file == null || !file.exists() || !file.isFile()) {
			return new byte[0];
		}
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			throw new IOException("File is larger then max array size");
		}
		byte[] bytes = new byte[(int) len];
		FileInputStream in = null;
		int readLength;
		try {
			in = new FileInputStream(file);
			readLength = in.read(bytes);
			if(readLength < len){
				throw new IOException(StringUtils.format("File length is [{}] but read [{}]!", len, readLength));
			}
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			IoUtils.close(in);
		}
		return bytes;
	}
	
	public static void main(String[] args) {
		System.out.println(getFileNameWithoutExtension("c:/a.tar.gz"));
	}
}
