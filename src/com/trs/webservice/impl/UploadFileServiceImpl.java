/**
 * 
 */
package com.trs.webservice.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import com.trs.model.AppAppendix;
import com.trs.model.AppSysConfig;
import com.trs.service.AppSysConfigService;
import com.trs.service.PublicAppBaseService;
import com.trs.util.CMyString;
import com.trs.util.Global;
import com.trs.util.UploadFileUtil;
import com.trs.webservice.UploadFileService;
import com.trs.webservice.WSConstant;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: FileUploadServiceImpl
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-6-23 上午10:05:02
 * @version 1.0
 */
@WebService(endpointInterface = "com.trs.webservice.UploadFileService")
public class UploadFileServiceImpl implements UploadFileService {

	@Autowired
	private PublicAppBaseService pubAppBaseService;
	@Autowired
	private AppSysConfigService configService;
	public String saveAppendix(AppAppendix appendix) {
		if(appendix.getAppId() == null || appendix.getMetadataId() == null || CMyString.isEmpty(appendix.getFileExt()) || CMyString.isEmpty(appendix.getFileName())){
			return WSConstant.NO_PARAM;
		}
		//获取附件保存的环境变量
		String basePath = "";
		String allowFileExt = "";//允许上传的文件类型
		long maxSize = 0;
		try {
			basePath = configService.findSysConfigCon("APP_UPLOAD_PATH");//(AppSysConfig)pubAppBaseService.findObject(AppSysConfig.class.getName(), "configName=?",Global.APP_UPLOAD_PATH);
			allowFileExt = configService.findSysConfigCon("ALLOW_UPLOAD_FILE", "swf,jpg,jpeg,bmp,gif,png,wmv,avi,wma,mp3,mid,zip,rar,txt,doc,docx,xls,xlsx");
			maxSize = Long.valueOf(configService.findSysConfigCon("DOC_MAX_UPLOAD_SIZE", "0"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return WSConstant.SYS_ERROR;
		}
		/* 文件类型验证*/
		if (("," + allowFileExt.toLowerCase() + ",").indexOf("," + appendix.getFileExt().toLowerCase() + ",") < 0){
			return WSConstant.UN_ALLOW_FILE_EXT;
		}
		String fileName = UploadFileUtil.getFileName();
		basePath = UploadFileUtil.createFolder(basePath, fileName);
		fileName = fileName + "." + appendix.getFileExt();
		InputStream is = null;
        OutputStream os = null;
        try {
            is = appendix.getHandler().getInputStream();
            /*文件是否为空*/
			if (is.available() == 0){
				return WSConstant.NULL_FILE_SIZE;
			}
            /*检查文件大小*/
			if (is.available() > maxSize * 1024){
				return WSConstant.UN_ALLOW_FILE_SIZE;
			}
            os = new FileOutputStream(basePath + File.separator + fileName);
            int n = 0;
            byte[] b = new byte[1024];
            while ((n = is.read(b)) != -1) {
                os.write(b, 0, n);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return WSConstant.SYS_ERROR;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return WSConstant.SYS_ERROR;
            }
        }
        appendix.setSrcfile(fileName);
        pubAppBaseService.save(appendix);
		return fileName;
	}

}
