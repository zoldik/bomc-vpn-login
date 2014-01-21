package com.boco.bomc.vpn.db.bean;

import java.util.List;

/**
 * 
 */
public class Page{

    static final int  DEFAULT_PAGE_SIZE = 20;
    static final int  FIRST_PAGE_NUMBER = 1;

    private int pageSize = DEFAULT_PAGE_SIZE;
    private int pageNum = FIRST_PAGE_NUMBER;
    private int pageCount;
    private int recordCount;
    
    private List objects = null;
    
    public int getPageSize(){
        return  pageSize;
    }

    public int getPageNum(){
        return pageNum;
    }

    public Page resetPageCount(){
        this.pageCount = -1;
        return this;
    }

    public int getPageCount(){
        if(pageCount<1){
            pageCount = (int)Math.ceil((double)recordCount/pageSize);
        }
        return pageCount;
    }

    public int getRecordCount(){
        return recordCount;
    }

    public Page setPageNum(int pn){
        this.pageNum = pn>0?pn:FIRST_PAGE_NUMBER;
        return this;
    }

    public Page setPageSize(int ps){
        this.pageSize = ps>0?ps:DEFAULT_PAGE_SIZE; 
        return resetPageCount();
    }

    public Page setRecordCount(int rc){
        this.recordCount = rc>0?rc:0;
        this.pageCount = (int)Math.ceil((double)recordCount/pageSize);
        return this;
    }

    public int getOffset() {
        return pageSize * (pageNum-1);
    }

	public List getObjects() {
		return objects;
	}

	public void setObjects(List objects) {
		this.objects = objects;
	}

    public boolean isFirst(){
        return this.pageNum == FIRST_PAGE_NUMBER;
    }

    public boolean isLast(){
        if(pageCount==0)
            return true;
        return  pageNum == pageCount;
    }
    
	@Override
    public String toString() {
        return String.format(   "size: %d, total: %d, page: %d/%d",
                                                                pageSize,
                                                                recordCount,
                                                                pageNum,
                                                                this.getPageCount());
    }
}

