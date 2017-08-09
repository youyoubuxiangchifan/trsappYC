package com.trs.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.trs.dao.impl.BaseDaoImpl;
import com.trs.model.AppAppendix;
import com.trs.model.AppSysConfig;
import com.trs.util.Global;
import com.trs.util.UploadFileUtil;
/**
 * 
 * @Description:用于处理一些通用的服务<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author zhangzun
 * @ClassName: CommonService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-18 下午01:48:20
 * @version 1.0
 */
@Service
public class CommonService extends BaseService {
	@Autowired
	@Qualifier("baseDaoImpl") 
	private BaseDaoImpl baseDao;
	/**
	 * 
	* Description:上传附件到指定路径 <BR>   
	* @author zhangzun
	* @date 2014-4-21 上午11:09:07
	* @param file   文件信息
	* @throws Exception
	* @version 1.0
	 */
	public AppAppendix UploadFile(MultipartFile file) throws Exception{
			String basePath = "";
			//获取附件保存的环境变量
			AppSysConfig sysConfig= (AppSysConfig)this.findObject(AppSysConfig.class.getName(),
					"configName=?",Global.APP_UPLOAD_PATH);
			basePath = sysConfig.getConfigValue();
			AppAppendix adix = UploadFileUtil.upload(file, basePath);
			if(adix==null)
				throw new Exception("保存附件失败");
			return adix;
	}
	/**
	 * 
	* Description:上传附件并保存数据库 <BR>   
	* @author zhangzun
	* @date 2014-4-21 上午11:09:07
	* @param file 文件信息
	* @throws Exception
	* @version 1.0
	 */
	public void saveUploadFile(MultipartFile file,Long metaDataId,Long appId) throws Exception{
			String basePath = "";
			//获取附件保存的环境变量
			AppSysConfig sysConfig= (AppSysConfig)this.findObject(AppSysConfig.class.getName(),
					"configName=?",Global.APP_UPLOAD_PATH);
			basePath = sysConfig.getConfigValue();
			//上传附件到指定路径
			AppAppendix adix = UploadFileUtil.upload(file, basePath);
			if(adix==null)
				throw new Exception("保存附件失败");
			adix.setMetadataId(metaDataId);
			adix.setAppId(appId);
			//保存附件信息到数据库
			this.save(adix);
	}
	/**
	 * 
	* Description:上传多个附件到指定路径 <BR>   
	* @author zhangzun
	* @date 2014-4-21 上午11:09:07
	* @param files   文件信息集合
	* @throws Exception
	* @version 1.0
	 */
	public List<AppAppendix> UploadFiles(List<MultipartFile> files) throws Exception{
			String basePath = "";
			//获取附件保存的环境变量
			AppSysConfig sysConfig= (AppSysConfig)this.findObject(AppSysConfig.class.getName(),
					"configName=?",Global.APP_UPLOAD_PATH);
			basePath = sysConfig.getConfigValue();
			List<AppAppendix> adixs = UploadFileUtil.uploadFiles(files, basePath);
			if(adixs==null)
				throw new Exception("保存附件失败");
			return adixs;
	}
	/**
	 * 
	* Description:上传多个附件到指定路径，并保存文件信息到数据库 <BR>   
	* @author zhangzun
	* @date 2014-4-21 上午11:09:07
	* @param files   文件信息集合
	* @throws Exception
	* @version 1.0
	 */
	public void SaveUploadFiles(List<MultipartFile> files,Long metaDataId,Long appId) throws Exception{
			String basePath = "";
			//获取附件保存的环境变量
			AppSysConfig sysConfig= (AppSysConfig)this.findObject(AppSysConfig.class.getName(),
					"configName=?",Global.APP_UPLOAD_PATH);
			basePath = sysConfig.getConfigValue();
			List adixs = UploadFileUtil.uploadFiles(files, basePath);
			if(adixs==null)
				throw new Exception("保存附件失败");
			AppAppendix adix = null ;
			for(int i=0;i<adixs.size();i++){
				adix = (AppAppendix)adixs.get(i);
				adix.setMetadataId(metaDataId);
				adix.setAppId(appId);
			}
			baseDao.saveOrUpdateAll(adixs);
	}
}
