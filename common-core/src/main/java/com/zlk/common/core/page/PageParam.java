package com.zlk.common.core.page;

import java.io.Serializable;

/**
 * @Description 分页查询参数
 * @Date 2021/07/26
 **/
public class PageParam implements Serializable {

	/** 默认页码 */
	public static final long DEFAULT_PAGENO = 1;
	/** 默认页容量 */
	public static final long DEFAULT_PAGESIZE = 20;

	protected long pageNo;

	protected long pageSize;
	// 偏移量
	private transient long offset;

	public PageParam() {
		// 一定要先设置页容量
		this(DEFAULT_PAGENO, DEFAULT_PAGESIZE);
	}

	public PageParam(long pageNo, long pageSize) {
		// 一定要先设置页容量
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.offset = (pageNo - 1) * pageSize;
	}

	public long getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(long pageNo) {
		if (pageNo <= 0) {
			this.pageNo = DEFAULT_PAGENO;
		} else {
			this.pageNo = pageNo;
		}
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		if (pageSize <= 0) {
			this.pageSize = DEFAULT_PAGESIZE;
		} else {
			this.pageSize = pageSize;
		}
	}

	public long getOffset() {
		return this.offset;
	}

	public void setOffset() {
		this.offset = (this.pageNo - 1) * this.pageSize;
	}
}

