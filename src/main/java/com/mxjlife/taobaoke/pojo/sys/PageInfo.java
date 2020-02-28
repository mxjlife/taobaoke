package com.mxjlife.taobaoke.pojo.sys;

import java.util.List;

/**
 * description 返回一页数据
 *
 * @author mxj
 * email mxjlife@163.com
 * date 2019/1/3 11:02
 */
public class PageInfo<T> {

    private int currentPage = 1; // 当前页
    private int pageSize = 20; // 每页显示多少条记录
    // 查询数据库
    private List<T> dataList; // 本页的数据列表
    private int totalCount; // 总记录数
    // 计算
    private int pageCount; // 总页数


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int count = totalCount / this.pageSize;
        int a = totalCount % this.pageSize;
        this.pageCount = a == 0 ? count : count + 1; // 总页数
    }

    public int getPageCount() {
        return pageCount;
    }



    @Override
    public String toString() {
        return "PageBean{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", dataList=" + dataList +
                ", totalCount=" + totalCount +
                ", pageCount=" + pageCount +
                '}';
    }
}
