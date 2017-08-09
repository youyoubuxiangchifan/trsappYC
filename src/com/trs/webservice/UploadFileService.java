/**
 * 
 */
package com.trs.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.trs.model.AppAppendix;

/**
 * Description: 文件上传<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: FileUploadService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-6-23 上午09:53:11
 * @version 1.0
 */
@WebService
public interface UploadFileService {
	/**
	 * Description: 文件上传<BR>   
	 * @author liu.zhuan
	 * @date 2014-6-23 上午09:57:59
	 * @param appendix 附件对象
	 * @return String 上传后文件名
	 * @version 1.0
	 */
//	@WebMethod
//	public String uploadFile(@WebParam(name="fileEntity") AppAppendix fileEntity);
	
	/**
	 * Description: 附件上传保存<BR>   
	 * @author liu.zhuan
	 * @date 2014-6-23 上午09:57:59
	 * @param appendix 附件对象
	 * @return String 文件名或者错误号
	 * @version 1.0
	 */
	//@WebMethod
	public String saveAppendix(@WebParam(name="appendix") AppAppendix appendix);
	
}
