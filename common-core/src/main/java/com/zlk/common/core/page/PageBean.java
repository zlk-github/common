package com.zlk.common.core.page;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description 分页公共标签
 * @Date 2021/07/26
 **/
public class PageBean<T> extends PageParam implements Serializable {

    /**
     * 基础字段
     */

    //当前页的数量
    private long size;
    //总记录数
    private long total;
    //总页数
    private long pages;
    //结果集
    private List<T> list;

    /**
     * 构造方法
     */
    public PageBean() {
    }

    public PageBean(List<T> list) {
        if (list instanceof Page) {
            Page page = (Page)list;
            this.pageNo = page.getCurrent();
            this.pageSize = page.getSize();
            this.setOffset();
            this.pages = page.getPages();
            this.list = page.getRecords();
            this.size = page.getSize();
            this.total = (long)((int)page.getTotal());
        } else if (list instanceof Collection) {
            this.pageNo = 1;
            this.pageSize = list.size();
            this.setOffset();
            this.pages = this.pageSize > 0 ? 1 : 0;
            this.list = list;
            this.size = list.size();
            this.total = (long)list.size();
        }
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long totalCount) {
        if (totalCount <= 0) {
            this.total = 0;
        } else {
            this.total = totalCount;
        }
    }

    public long getPages() {
        if(getPageSize() <= 0){
            this.pageSize = DEFAULT_PAGESIZE;
        }
        if (getTotal() <= 0) {
            this.total = 0;
        }
        this.pages = (long) ((this.total - 1) / this.pageSize + 1);
        return pages;
    }
	public void setPages(long pages) {
		this.pages = pages;
	}

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "size=" + size +
                ", total=" + total +
                ", pages=" + pages +
                ", list=" + list +
                '}';
    }
}