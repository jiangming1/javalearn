package com.stylefeng.guns.core.util;

/**
 * MyPage分页类
 *
 * @author 周蒙牛
 * @date 2017-12-14 18:37
 */

import java.util.ArrayList;

public class MyPage {
    private int recordTotal; //记录总条数 320
    private int sizePerPage; //每页分多少条记录 20
    private int pagingTotal; // 总共分多少页 320/20=16
    private int currentPageNum; // 当前页码, 页码从1开始计数
    private int offset; // 当前页码对应的偏移量 一般从0开始
    private int pagingBtnLimit; // 页面最多显示的分页按钮数 7

    private int firstPageNum;
    private int firstPageOffset;
    private int lastPageNum; //最后一页的页码 等同 pagingTotal
    private int lastPageOffset;
    private int prevPageNum;
    private int prevPageOffset;
    private int nextPageNum;
    private int nextPageOffset;

    private boolean doPagingSuccess;
    private boolean displayPrevPageBtn = false; //以下6个boolean值可能会被doPaging()修改
    private boolean displayNextPageBtn = false;
    private boolean displayFirstPageBtn = false;
    private boolean displayLastPageBtn = false;
    private boolean displayHeadEllipsesBtn = false;
    private boolean displayTailEllipsesBtn = false;
    private int headEllipsesPageNum; //以下4个int值可能会被doPaging()修改
    private int headEllipsesOffset;
    private int tailEllipsesPageNum;
    private int tailEllipsesOffset;

    private ArrayList<Integer> pageNumList = new ArrayList<>();
    private ArrayList<Integer> offsetList = new ArrayList<>();

    public MyPage(int recordTotal, int sizePerPage, int currentPageNum, int pagingBtnLimit) {
        this.recordTotal = recordTotal;
        this.sizePerPage = sizePerPage;
        this.pagingTotal = (int) Math.ceil((double) recordTotal / (double) sizePerPage);
        this.currentPageNum = currentPageNum;
        this.offset = (currentPageNum - 1) * sizePerPage;
        this.pagingBtnLimit = pagingBtnLimit;

        this.firstPageNum = 1;
        this.firstPageOffset = 0;
        this.lastPageNum = pagingTotal;
        this.lastPageOffset = (lastPageNum - 1) * sizePerPage;
        this.prevPageNum = currentPageNum - 1;
        this.prevPageOffset = (prevPageNum - 1) * sizePerPage;
        this.nextPageNum = currentPageNum + 1;
        this.nextPageOffset = (nextPageNum - 1) * sizePerPage;
        this.doPagingSuccess = doPaging();
    }

    private boolean doPaging() {
        if (currentPageNum >= 1 && currentPageNum <= lastPageNum) {

            if (currentPageNum > 1) {
                displayPrevPageBtn = true;
            }

            if (pagingTotal - pagingBtnLimit > 0) {
                int tailBtnCount;
                int headBtnCount = tailBtnCount = pagingBtnLimit / 2;
                if (pagingBtnLimit % 2 == 0) {
                    headBtnCount -= 1;
                }
                if (currentPageNum <= pagingBtnLimit) {
                    for (int i = currentPageNum - 1; i >= 1; i--) {
                        pageNumList.add(currentPageNum - i);
                        offsetList.add(offset - sizePerPage * i);
                    }
                    pageNumList.add(currentPageNum);
                    offsetList.add(offset);
                    for (int i = 1; i <= (pagingBtnLimit - currentPageNum); i++) {
                        pageNumList.add(currentPageNum + i);
                        offsetList.add(offset + sizePerPage * i);
                    }
                    int tailPageNum = currentPageNum + (pagingBtnLimit - currentPageNum) + 1;
                    tailEllipsesPageNum = (tailPageNum + lastPageNum) / 2;
                    tailEllipsesOffset = (tailEllipsesPageNum - 1) * sizePerPage;
                    // 输出尾部的省略号页码..., 不输出头部的省略号页码...
                    displayTailEllipsesBtn = true;
                    if (tailPageNum < lastPageNum) {
                        displayLastPageBtn = true;
                    }
                } else if ((lastPageNum - currentPageNum) < pagingBtnLimit) {
                    // 输出头部的省略号页码..., 不输出尾部的省略号页码...
                    int headPageNum = currentPageNum - (pagingBtnLimit - (lastPageNum - currentPageNum) - 1);
                    if (headPageNum > 1) {
                        displayFirstPageBtn = true;
                    }
                    displayHeadEllipsesBtn = true;
                    headEllipsesPageNum = (headPageNum + 1) / 2;
                    headEllipsesOffset = (headEllipsesPageNum - 1) * sizePerPage;
                    for (int i = pagingBtnLimit - (lastPageNum - currentPageNum) - 1; i >= 1; i--) {
                        pageNumList.add(currentPageNum - i);
                        offsetList.add(offset - sizePerPage * i);
                    }
                    pageNumList.add(currentPageNum);
                    offsetList.add(offset);
                    for (int i = 1; i <= lastPageNum - currentPageNum; i++) {
                        pageNumList.add(currentPageNum + i);
                        offsetList.add(offset + sizePerPage * i);
                    }
                } else {
                    displayFirstPageBtn = true;
                    displayHeadEllipsesBtn = true;
                    int headPageNum = currentPageNum - headBtnCount - 1;
                    int n = (headPageNum % 2 == 0) ? 2 : 1; //修正中位数
                    headEllipsesPageNum = (headPageNum + n) / 2;
                    headEllipsesOffset = (headEllipsesPageNum - 1) * sizePerPage;
                    for (int i = headBtnCount; i >= 1; i--) {
                        pageNumList.add(currentPageNum - i);
                        offsetList.add(offset - sizePerPage * i);
                    }
                    pageNumList.add(currentPageNum);
                    offsetList.add(offset);
                    for (int i = 1; i <= tailBtnCount; i++) {
                        pageNumList.add(currentPageNum + i);
                        offsetList.add(offset + sizePerPage * i);
                    }
                    int tailPageNum = currentPageNum + tailBtnCount + 1;
                    tailEllipsesPageNum = (tailPageNum + lastPageNum) / 2;
                    tailEllipsesOffset = (tailEllipsesPageNum - 1) * sizePerPage;
                    displayTailEllipsesBtn = true;
                    displayLastPageBtn = true;
                }

            } else {
                //输出全部
                int offsetForLoop = firstPageOffset;
                for (int i = 1; i <= pagingTotal; i++) {
                    pageNumList.add(i);
                    offsetList.add(offsetForLoop);
                    offsetForLoop += sizePerPage;
                }
            }

            if (currentPageNum < lastPageNum) {
                displayNextPageBtn = true;
            }
        } else {
            // 跳转到404页面
            return false;
        }
        return true;
    }

    public int getRecordTotal() {
        return recordTotal;
    }

    public int getSizePerPage() {
        return sizePerPage;
    }

    public int getPagingTotal() {
        return pagingTotal;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public int getOffset() {
        return offset;
    }

    public int getPagingBtnLimit() {
        return pagingBtnLimit;
    }

    public int getFirstPageNum() {
        return firstPageNum;
    }

    public int getFirstPageOffset() {
        return firstPageOffset;
    }

    public int getLastPageNum() {
        return lastPageNum;
    }

    public int getLastPageOffset() {
        return lastPageOffset;
    }

    public int getPrevPageNum() {
        return prevPageNum;
    }

    public int getPrevPageOffset() {
        return prevPageOffset;
    }

    public int getNextPageNum() {
        return nextPageNum;
    }

    public int getNextPageOffset() {
        return nextPageOffset;
    }

    public boolean isDoPagingSuccess() {
        return doPagingSuccess;
    }

    public boolean isDisplayPrevPageBtn() {
        return displayPrevPageBtn;
    }

    public boolean isDisplayNextPageBtn() {
        return displayNextPageBtn;
    }

    public boolean isDisplayFirstPageBtn() {
        return displayFirstPageBtn;
    }

    public boolean isDisplayLastPageBtn() {
        return displayLastPageBtn;
    }

    public boolean isDisplayHeadEllipsesBtn() {
        return displayHeadEllipsesBtn;
    }

    public boolean isDisplayTailEllipsesBtn() {
        return displayTailEllipsesBtn;
    }

    public int getHeadEllipsesPageNum() {
        return headEllipsesPageNum;
    }

    public int getHeadEllipsesOffset() {
        return headEllipsesOffset;
    }

    public int getTailEllipsesPageNum() {
        return tailEllipsesPageNum;
    }

    public int getTailEllipsesOffset() {
        return tailEllipsesOffset;
    }

    public ArrayList<Integer> getPageNumList() {
        return pageNumList;
    }

    public ArrayList<Integer> getOffsetList() {
        return offsetList;
    }
}

