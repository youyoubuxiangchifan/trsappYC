package com.trs.dbhibernate;

import java.util.List;

/**
 * Description:通用分页保存分页信息<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author wen.junhui
 * @ClassName: Page
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 下午02:21:25
 * @version 1.0
 */
public class Page {

	private int pageSize; // 页大小

	private int startPage; // 起始页 从1开始

	private int totalResults; // 总记录的条数

	private int totalPages; // 总页数
	
	private int startIndex; // 开始记录数
	
	

	private int[] pager;//保存翻页开始到结束页码

    private List<Object> ldata = null; //保存分页的数据集
	/**
	 * 构造方法
	 * @param startPage
	 * @param pageSize
	 */
	public Page(int startPage, int pageSize) {
		this.startPage = startPage;
		this.pageSize = pageSize; 
	}
	
	/**
	 * 构造方法
	 * @param startPage
	 * @param pageSize
	 * @param totalResults
	 */
	public Page(int startPage, int pageSize, int totalResults) {
		this.startPage = startPage;
		this.pageSize = pageSize; 
		this.totalResults = totalResults;
		
		this.setStartIndex();
		this.setTotalPages();
		this.pager=pager(startPage, this.totalPages, 8);
	}
	
	/** 
	 * @return startIndex 
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/** 
	 * 设置属性： startIndex
	 * @param startIndex 
	 */
	public void setStartIndex() {
		this.startIndex = (this.startPage-1)*this.pageSize;
	}
	/**
	 * 静态方法
	 * 获得开始页面
	 * @author Page
	 * @param startPageStr String 获得的开始页面字符串
	 * @return startPage int 返回开始页面
	 */
	public static int getStartPage(String startPageStr) {
		int startPage = 1;
		try {
			if (startPageStr == null || startPageStr.equals("") || Integer.parseInt(startPageStr)==0) {
				startPage = 1;
			} else {
				startPage = Integer.parseInt(startPageStr);
			}
			
		} catch (Exception e) {
			startPage = 1;
		}
		return startPage;
	}
	
	/**
	 * 获得分页显示第一页和最后一页,即需要在页面上显示哪些分页链接
	 * @author wenjunhui
	 * 
	 * @param startPage 开始页面
	 * @param totalPage 总的页面
	 * @param disMaxPage 最大显示页面
	 * @return 第一个,和最后一个页数,
	 * 用数组传递,第一个元素保存第一页
	 * 第二个元素保存最后页
	 */
	public static int[] pager(int startPage, int totalPage, int disMaxPage) {
		int[] result = new int[]{0,0};
		
		if (totalPage==0) {
			return result;
		}
		
		result[0] = startPage - disMaxPage/2;//设置第一个页数
		result[1] = startPage + ((disMaxPage%2==0)?(disMaxPage/2-1):(disMaxPage/2));//设置最后一个页数
		
		if (result[0]<=0) {//如果第一个为负数,或0
			result[0] = 1;//把第一个页数设为1
			result[1] = totalPage<disMaxPage? totalPage : disMaxPage;
		}
		if (result[1]>totalPage) {//判断最后一个页数,是否比总页数多
			result[1] = totalPage;
			result[0] = totalPage-disMaxPage+1<=1? 1 : totalPage-disMaxPage+1;
		}
		
		return result;
	}
	
	
	/** */
	/**
	 * 得到起始页
	 * @author wenjunhui
	 * @return
	 */
	public int getStartPage() {
		if (startPage < 1) {
			startPage = 1;
		}
		if (startPage > totalPages) {
			startPage = totalPages;
		}
		return startPage;
	}

	/** */
	/**
	 * 得到记录总数
	 * 
	 * @return
	 */
	public int getTotalResults() {
		return totalResults;
	}
	/**
	 * 设置总记录数
	 * @return
	 */
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
		this.setTotalPages();
	}

	/** */
	/**
	 * 得到页大小
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/** */
	/**
	 * 判断是否是第一页
	 * 
	 * @return
	 */
	public boolean isFirstPage() {
		return this.startPage == 1;
	}

	/** */
	/**
	 * 判断是否是有后一页
	 * 
	 * @return
	 */
	public boolean hasNextPage() {
		return this.startPage < this.totalPages;
	}

	/** */
	/**
	 * 判断是否是有前一页
	 * 
	 * @return
	 */
	public boolean hasPreviousPage() {
		return this.startPage > 1;
	}

	/** */
	/**
	 * 设置总页数
	 * 
	 */
	private void setTotalPages() {
		this.totalPages = this.totalResults / this.pageSize;
		if (totalPages * pageSize < totalResults) {
			totalPages++;
		}
	}

	/** */
	/**
	 * 得到总页数
	 * 
	 * @return
	 */
	public int getTotalPages() {
		return totalPages;
	}

	public int[] getPager() {
		return pager;
	}

	public void setPager(int[] pager) {
		this.pager = pager;
	}

	public List<Object> getLdata() {
		return ldata;
	}

	public void setLdata(List<Object> ldata) {
		this.ldata = ldata;
	}

}
