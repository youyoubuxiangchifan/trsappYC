package com.trs.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.trs.service.AppSysConfigService;
import com.trs.util.CMyString;
import com.trs.util.UploadFileUtil;

/**
 * Description:文件上传 <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppFileUploadController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-24 下午07:29:38
 * @version 1.0
 */
@Controller
@RequestMapping("/appUpload.do")
public class AppFileUploadController {
	@Autowired
	private AppSysConfigService configService;
	/**
	 * 在线编辑器单个文件上传
	* Description: 在线编辑器单个文件上传 <BR>   
	* @author liu.zhuan
	* @date 2014-4-25 上午09:49:09
	* @param model
	* @param request
	* @param response
	* @throws ServletException
	* @throws IOException
	* @version 1.0
	 */
	@RequestMapping(params = "method=doXheditorUpload")
	public void doImgUpload(ModelMap model, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String err = "";//上传异常信息
		String msg = "";//成功信息
		String sFileName = "";//新文件名
		String sLocalName = "";//上传的文件名
		String sExt = "";//文件后缀名
		String basePath = "";//根目录
		String allowFileExt = "";//允许上传的文件类型
		long maxSize = 102400;//允许上传的最大值kb
		try {
			maxSize = Long.valueOf(configService.findSysConfigCon("DOC_MAX_UPLOAD_SIZE", "0"));
			allowFileExt = configService.findSysConfigCon("ALLOW_UPLOAD_FILE", "swf,jpg,jpeg,bmp,gif,png,wmv,avi,wma,mp3,mid,zip,rar,txt,doc,docx,xls,xlsx");
			sFileName = UploadFileUtil.getFileName();
			basePath = configService.findSysConfigCon("APP_UPLOAD_PATH");
			if(CMyString.isEmpty(basePath)){
				printInfo(response, "系统上传目录不存在", "");
				return;
			}
			if("application/octet-stream".equals(request.getContentType())){
	            String dispoString = request.getHeader("Content-Disposition"); 
	            int iFindStart = dispoString.indexOf("filename=\"") + 10;  
	            int iFindEnd = dispoString.indexOf("\"", iFindStart);  
	            sLocalName = dispoString.substring(iFindStart, iFindEnd);
	            /*获取文件扩展名*/
	            sExt = UploadFileUtil.getExt(sLocalName);
	            int i = request.getContentLength();  
	            byte buffer[] = new byte[i];  
	            int j = 0;  
	            while (j < i) { //获取表单的上传文件  
	                int k = request.getInputStream().read(buffer, j, i - j);  
	                j += k;  
	            }
	            /*检查文件类型*/
				if (("," + allowFileExt.toLowerCase() + ",").indexOf("," + sExt.toLowerCase() + ",") < 0){
					printInfo(response, "不允许上传此类型的文件", "");
					return;
				}
	            /*文件是否为空*/
				if (buffer.length == 0){
					printInfo(response, "上传文件不能为空", "");
					return;
				}
	            /*检查文件大小*/
				if (buffer.length > maxSize * 1024){
					printInfo(response, "上传文件的大小超出限制,最大不能超过"+maxSize+"KB", "");
					return;
				}
	            sFileName = sFileName + "." + sExt;
				/*文件存储在容器中的绝对路径*/
				String saveFilePath = UploadFileUtil.createFolder(basePath, sFileName) + File.separator + sFileName;
                File file = new File(saveFilePath);  
                OutputStream out = new BufferedOutputStream(new FileOutputStream(file, true));  
                out.write(buffer);  
                out.close();  
	            msg = "{\"url\":\"/upload/" + UploadFileUtil.getFileFullPath(sFileName) + "\",\"localname\":\"" + sLocalName + "\"}";
			}else{
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest)request;
				response.setContentType("text/html; charset=UTF-8");
				response.setHeader("Cache-Control", "no-cache");
				MultipartFile mFile = mRequest.getFile("filedata");
				/*获取文件上传路径名称*/
				sLocalName = mFile.getOriginalFilename();
				/*获取文件扩展名*/
				sExt = sLocalName.substring(sLocalName.lastIndexOf(".") + 1);
				/*检查文件类型*/
				if (("," + allowFileExt.toLowerCase() + ",").indexOf("," + sExt.toLowerCase() + ",") < 0){
					printInfo(response, "不允许上传此类型的文件", "");
					return;
				}
				/*文件是否为空*/
				if (mFile.getSize() == 0){
					printInfo(response, "上传文件不能为空", "");
					return;
				}
				/*检查文件大小*/
				if (mFile.getSize() > maxSize * 1024){
					printInfo(response, "上传文件的大小超出限制,最大不能超过"+maxSize+"KB", "");
					return;
				}
				sFileName = sFileName + "." + sExt;
				/*文件存储在容器中的绝对路径*/
				String saveFilePath = UploadFileUtil.createFolder(basePath, sFileName) + File.separator + sFileName;
				/*存储上传文件*/
				File savefile = new File(saveFilePath);
				FileOutputStream fos = new FileOutputStream(savefile);
				fos.write(mFile.getBytes());
				if(fos != null){
					fos.close();
				}
				//msg = "{\"url\":\"/upload/" + UploadFileUtil.getFileFullPath(sFileName) + "\",\"localname\":\"aaaaa\",\"id\":\"1\"}";	
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			err = "系统错误: " + ex.getMessage();
		}
		msg = "{\"url\":\"/upload/" + UploadFileUtil.getFileFullPath(sFileName) + "\",\"localname\":\"" + sLocalName + "\"}";
		printInfo(response, err, msg);
	}
	
	/**
	 * 使用I/O流输出 json格式的数据
	 * @param response
	 * @param err
	 * @param newFileName
	 * @throws IOException
	 */
	public void printInfo(HttpServletResponse response, String err, String newFileName) throws IOException {
		PrintWriter out = response.getWriter();
		//String filename = newFileName.substring(newFileName.lastIndexOf("/") + 1);
		out.println("{\"err\":\"" + err + "\",\"msg\":" + newFileName + "}");
		out.flush();
		out.close();
	}
}
