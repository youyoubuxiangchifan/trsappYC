/**
 * Description:<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: SimpleMail
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 上午11:30:30
 * @version 1.0
 */
package com.trs.util.email;

/**
 * @Description: TODO(描述类的作用)<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liujian
 * @ClassName: SimpleMail
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 上午11:30:30
 * @version 1.0
 */
public class SimpleMail {
	private static String subject;
	private static String content;
	public void SimpleMail(String subject,String content){
		this.subject=subject;
		this.content=content;
	}
	
	/** 
	* @Description: 设置邮件主题 <BR>   
	* @author liujian
	* @date 2014-3-10 下午02:49:17
	* @param subject
	* @version 1.0
	*/
	public void setSubject(String subject){
		this.subject=subject;
	}
	/** 
	* @Description: 设置邮件内容 <BR>   
	* @author liujian
	* @date 2014-3-10 下午02:49:36
	* @param content
	* @version 1.0
	*/
	public void setContent(String content){
		this.content=content;
	}
	
	/** 
	* @Description: 获取邮件主题 <BR>   
	* @author liujian
	* @date 2014-3-10 下午02:49:58
	* @return
	* @version 1.0
	*/
	public String getSubject(){		
		return this.subject;
	}
	
	/** 
	* @Description: 获取邮件内容 <BR>   
	* @author liujian
	* @date 2014-3-10 下午02:50:22
	* @return
	* @version 1.0
	*/
	public String getContent(){
		return this.content;
	}
}

