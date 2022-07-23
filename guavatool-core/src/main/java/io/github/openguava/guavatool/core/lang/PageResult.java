package io.github.openguava.guavatool.core.lang;

import java.util.ArrayList;
import java.util.List;

import io.github.openguava.guavatool.core.enums.HttpStatus;

/**
 * 分页结果
 * @author openguava
 *
 * @param <T>
 */
public class PageResult<T> implements Result<List<T>> {
	
	/** 默认当前页 */
	public static Long DEFAULT_PAGE_NO = 1L;
	
	/** 默认页大小 */
	public static Long DEFAULT_PAGE_SIZE = 1L;
	
	
	/** 成功 */
	public static int CODE_SUCCESS = HttpStatus.OK.getValue();

	/** 失败 */
	public static int CODE_FAIL = HttpStatus.INTERNAL_SERVER_ERROR.getValue();

	private Integer code;
	
	@Override
	public int getCode() {
		return this.code;
	}
	
	public PageResult<T> setCode(Integer code) {
		this.code = code;
		return this;
	}

	private String msg;
	
	@Override
	public String getMsg() {
		return this.msg;
	}
	
	public PageResult<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	
	/** 当前页 */
	private Long pageNo;
	
	public Long getPageNo() {
		return pageNo;
	}
	
	public PageResult<T> setPageNo(Long pageNo) {
		this.pageNo = pageNo;
		return this;
	}
	
	/** 页大小 */
	private Long pageSize;
	
	public Long getPageSize() {
		return this.pageSize;
	}
	
	public PageResult<T> setPageSize(Long pageSize) {
		this.pageSize = pageSize;
		return this;
	}
	
	/** 分页总数 */
	private Long pageCount = 0L;
	
	public Long getPageCount() {
		return this.pageCount;
	}

	/** 起始索引 */
	private Long startIndex = 0L;
	
	public Long getStartIndex() {
		return this.startIndex;
	}
	
	/** 数据总数 */
	private Long dataCount = 0L;
	
	public Long getDataCount() {
		return this.dataCount;
	}
	
	public PageResult<T> setDataCount(Long dataCount) {
		this.dataCount = dataCount;
		this.initPageInfo();
		return this;
	}
	
	private List<T> data;
	
	@Override
	public List<T> getData() {
		if(this.data == null) {
			this.data = new ArrayList<>();
		}
		return this.data;
	}
	
	public void setData(List<T> data) {
		this.data = data;
		if(this.dataCount == null) {
			this.setDataCount(((Integer)data.size()).longValue());
		}
	}

	@Override
	public boolean isSuccess() {
		return this.code == CODE_SUCCESS;
	}

	@Override
	public boolean isFail() {
		return this.code == CODE_FAIL;
	}
	
	public PageResult() {
		
	}
	
	public PageResult(List<T> data, Long dataCount) {
		this.data = data;
		this.dataCount = dataCount;
	}

	/**
	 * 初始化分页
	 * @return
	 */
	public PageResult<T> initPageInfo() {
		this.startIndex = getStartIndex(this.getPageNo(), this.getPageSize());
		this.pageCount = getPageCount(this.getDataCount(), this.getPageSize());
		return this;
	}
	
	/**
	 * 计算分页数量
	 * @param dataCount 数据量数
	 * @param pageSize 页大小
	 * @return
	 */
	public static Long getPageCount(Long dataCount, Long pageSize) {
		long pgSize = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
		long dtCount = dataCount != null ? dataCount : 0L;
		long pgCount = dtCount / pgSize;
		return (dtCount % pgSize == 0 ?  pgCount : pgCount + 1);
	}
	
	/**
	 * 计算数据开始索引
	 * @param pageNo 当前页
	 * @param pageSize 页大小
	 * @return
	 */
	public static Long getStartIndex(Long pageNo, Long pageSize) {
		long pgNo = pageNo != null ? pageNo : DEFAULT_PAGE_NO;
		long pgSize = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
		return (pgNo - 1) * pgSize;
	}
	
	public static void main(String[] args) {
		PageResult<String> pg = new PageResult<String>();
		pg.setPageSize(3L);
		pg.setDataCount(100L);
		System.out.println(pg.getPageCount());
	}
}
