package com.trs.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * Description: 应用数据操作webservice(供数据迁移使用)<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppMetaDataWebService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-6-20 下午12:50:41
 * @version 1.0
 */
@WebService
public interface AppDataWebService {
	/**
	 * Description:  保存或修改应用数据<BR>   
	 * @author liu.zhuan
	 * @date 2014-6-20 下午12:56:13
	 * @param jsonData json字符串    数据字段-值集合。包含必须输入的参数应用编号-APPID。
	 * @return String 保存时返回metadataid,修改时返回0。如果要保存数据不需要输入metadataid。或者返回错误号。
	 * @version 1.0
	 */
	//@XmlJavaTypeAdapter(MapAdapter.class)
	public String saveOrUpdateAppData(@WebParam(name="jsonData") String jsonData, @WebParam(name="appType") String appType);
	
	/**
	 * Description: 保存应用数据的评论信息 <BR>   
	 * @author liu.zhuan
	 * @date 2014-7-4 下午02:38:13
	 * @param jsonData json字符串    数据字段-值集合。包含必须输入的参数应用编号-DATA_ID,APP_ID,字段名需要大写，且和数据库中字段名称完全一致。
	 * @return 评论编号或错误号
	 * @version 1.0
	 */
	public String saveAppDataComment(@WebParam(name="jsonData") String jsonData);
}
