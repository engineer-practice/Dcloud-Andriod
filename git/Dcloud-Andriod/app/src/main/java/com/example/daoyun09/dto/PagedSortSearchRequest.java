package com.example.daoyun09.dto;

public class PagedSortSearchRequest {
    int maxCount;
    int pageIndex;
    String orderBy;
    boolean isASC;

    public PagedSortSearchRequest(int maxCount, int pageIndex, String orderBy, boolean isASC, String search) {
        this.maxCount = maxCount;
        this.pageIndex = pageIndex;
        this.orderBy = orderBy;
        this.isASC = isASC;
        this.search = search;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isASC() {
        return isASC;
    }

    public void setASC(boolean ASC) {
        isASC = ASC;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    String search;
}