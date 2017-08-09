/**   
* @Title: MD5Test.java 
* @Package com.trs.service.test 
* @Description: TODO 
* @author jin.yu 
* @date 2014-3-11 上午09:54:39 
* @version V1.0   
*/
package com.trs.service.test;

import com.trs.util.MD5;

/**
 * @Description:<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: MD5Test
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-11 上午09:54:39
 * @version 1.0
 */
public class MD5Test {

	/** 
	 * @Description: TODO<BR>   
	 * @author jin.yu
	 * @date 2014-3-11 上午09:54:39
	 * @param args
	 * @version 1.0
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MD5 md5 = new MD5();
		System.out.println(md5.getMD5ofStr("trsadmin123456712121212121218990"));
	}

}
