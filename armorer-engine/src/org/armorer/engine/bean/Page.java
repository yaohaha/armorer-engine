package org.armorer.engine.bean;

import org.armorer.engine.common.Constant;

public class Page {

    //当前页面的数
    private int currentPage = 1;

    //总条数
    private int count = 0;

    //总页数
    private int pageNum = 0;
    
    //一页显示记录数
    private int onePageCount = Constant.ONE_PAGE_COUNT ;//一页显示的条数

    private String action;

    private String method;

    public Page(){
        System.out.println("this is page init!!!!");
    }
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        if(count%onePageCount==0){
            pageNum = count/onePageCount;
        }else{
            pageNum = count/onePageCount +1;
        }

    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public int getOnePageCount() {
        return onePageCount;
    }
    public void setOnePageCount(int onePageCount) {
        this.onePageCount = onePageCount;
    }



}
