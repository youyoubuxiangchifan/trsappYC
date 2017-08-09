/**
 * 
 */
package com.trs.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import sun.nio.cs.ext.GBK;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: HttpUtil
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2015-5-25 上午09:54:00
 * @version 1.0
 */
public class HttpUtil {
	public static String CHARSET_UTF8 = "utf-8";
	public static String CHARSET_GBK = "gbk";
	public static String CHARSET_GB2312 = "gb2312";
	/**
	 * Description: HttpPost方式提交数据并返回结果 <BR>   
	 * @author liu.zhuan
	 * @date 2015-5-25 上午10:04:55
	 * @param url
	 * @param postData
	 * @return String
	 * @version 1.0
	 */
	public static String getResponseTextWithPost(String url, String postData, String charset){
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.addRequestHeader("Content-Type",
		"application/x-www-form-urlencoded;charset=" + charset);
		// 填入各个表单域的值
		if(CMyString.isEmpty(postData)){
			return "error";
		}
		String[] parameters = postData.split("&");
		NameValuePair[] data = new NameValuePair[parameters.length];
		NameValuePair valuePair = null;
		for (int i = 0; i < parameters.length; i++) {
			String[] parameter = parameters[i].split("=");
			valuePair = new NameValuePair(parameter[0], parameter[1]); 
			data[i] = valuePair;
		}
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);
		// 执行postMethod
		int statusCode = 0;
		String responseBody = null;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			if(statusCode == HttpStatus.SC_OK){
				responseBody = postMethod.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			postMethod.releaseConnection();		
		}
		return CMyString.isEmpty(responseBody) ? null : responseBody.trim();
	}
	public static String getResponseTextWithGet(String url, String charset){
		 /*
	     * 使用 GetMethod 来访问一个 URL 对应的网页,实现步骤: 1:生成一个 HttpClinet 对象并设置相应的参数。
	     * 2:生成一个 GetMethod 对象并设置响应的参数。 3:用 HttpClinet 生成的对象来执行 GetMethod 生成的Get
	     * 方法。 4:处理响应状态码。 5:若响应正常，处理 HTTP 响应内容。 6:释放连接。
	     */
	    /* 1 生成 HttpClinet 对象并设置参数 */
	    HttpClient httpClient = new HttpClient();
	    // 设置 Http 连接超时为5秒
	    //httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
	    /* 2 生成 GetMethod 对象并设置参数 */
	    GetMethod getMethod = new GetMethod(url);
	    // 设置 get 请求超时为 5 秒
	    //getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
	    // 设置请求重试处理，用的是默认的重试处理：请求三次
	    //getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler());
	    String responseBody = null;
	    /* 3 执行 HTTP GET 请求 */
	    try {
	    	int statusCode = httpClient.executeMethod(getMethod);
	      /* 4 判断访问的状态码 */
	    	if (statusCode == HttpStatus.SC_OK) {
	    		responseBody = getMethod.getResponseBodyAsString();
	    	}
	      /* 5 处理 HTTP 响应内容 */
	      // HTTP响应头部信息，这里简单打印
	      // 读取 HTTP 响应内容，这里简单打印网页内容
	      // 读取为 InputStream，在网页内容数据量大时候推荐使用
	      // InputStream response = getMethod.getResponseBodyAsStream();
	    } catch (HttpException e) {
	    	// 发生致命的异常，可能是协议不对或者返回的内容有问题
	    	e.printStackTrace();
	    	return null;
	    } catch (IOException e) {
	    	// 发生网络异常
	    	e.printStackTrace();
	    	return null;
	    } finally {
	    	/* 6 .释放连接 */
	    	getMethod.releaseConnection();
	    }
	    return CMyString.isEmpty(responseBody) ? null : responseBody.trim();
	}
	/** 
	 * Description:  <BR>   
	 * @author liu.zhuan
	 * @date 2015-5-25 上午09:54:00
	 * @param args
	 * @version 1.0
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String result = null;
		try {
			String mobile = "15857164622";
			String pwd = String.valueOf(Integer.parseInt(mobile.substring(7))*3 + 9433);//
			System.out.println(pwd);
			result = getResponseTextWithGet("http://111.1.5.170/mas_https/send.jsp?usr=tz98194&srcid=106573076817&mobile=" + mobile + "&msg="+URLEncoder.encode("\"你好！\"", "gbk")+"&yzm=" + pwd + "&tjpc=1", HttpUtil.CHARSET_UTF8);
			System.out.println(result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		String smsUrl = "http://111.1.5.170/mas_https/send.jsp?usr=tz98194&srcid=106573076817&mobile=${mobile}&msg=${content}&yzm=<9433>&tjpc=1";
//		String pwd = smsUrl.substring(smsUrl.indexOf("<") + 1, smsUrl.lastIndexOf(">"));
//		System.out.println(pwd);
	}

}
