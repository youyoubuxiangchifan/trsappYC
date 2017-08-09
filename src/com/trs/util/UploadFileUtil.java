package com.trs.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.trs.model.AppAppendix;

/**
 * 
 * Description:用于处理附件上传<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author zhangzun
 * @ClassName: UploadFileUtil
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-16 下午08:58:29
 * @version 1.0
 */
public class UploadFileUtil {
	/**
	 * 
	* Description:上传单个附件 <BR>   
	* @author zhangzun
	* @date 2014-4-16 下午09:24:43
	* @param file 文件名称
	* @param path 文件路径
	* @return AppAppendix 文件对象
	* @version 1.0
	 */
	public static AppAppendix upload(MultipartFile file, String basePath){
		AppAppendix adix = null;
		String fileName="";  //系统生成的文件名
		String fileExt = "";      //文件扩展名  
		if (file != null && file.getSize() != 0) {
			
			FileOutputStream fos = null;
			try {
				fileExt = getExt(file.getOriginalFilename());
				fileName = getFileName();
				basePath = createFolder(basePath, fileName);
				fileName = fileName + "."+fileExt;
				File _file = new File(basePath+File.separator+fileName);
				fos = new FileOutputStream(_file);
				fos.write(file.getBytes());
				adix = new AppAppendix();
				adix.setFileName(file.getOriginalFilename());
				adix.setSrcfile(fileName);
				adix.setFileExt(fileExt);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(fos!=null)
							fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return adix;
	}
	/**
	 * 
	* Description:上传多个附件<BR>   
	* @author zhangzun
	* @date 2014-4-16 下午09:23:35
	* @param files
	* @param path
	* @return
	* @version 1.0
	 * @throws Exception 
	 */
	public static List<AppAppendix> uploadFiles(List<MultipartFile> files, String basePath) throws Exception{
		
		if(files==null)
			   return null;
		List<AppAppendix> adixs = new ArrayList<AppAppendix>();
		AppAppendix adix = new AppAppendix();
		for(MultipartFile file : files){
			if (file == null || file.getSize() == 0) 
					continue;
			adix = upload(file,basePath);
			if(adix==null)
				throw new Exception("保存附件失败");
			adixs.add(adix);
		}
		return adixs;
	}
	
	/**
	 * 
	* Description: 文件后缀取得 <BR>   
	* @author jin.yu
	* @date 2014-4-23 下午08:14:41
	* @param fileName 文件名称
	* @return String文件后缀名称
	* @version 1.0
	 */
	public static String getExt(String fileName){
		String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
	    return ext;
	}
	
	/**
	 * 
	* Description:根据文件名生成文件的相对路径<BR>   
	* @author zhangzun
	* @date 2014-4-17 下午02:04:09
	* @param fileName 文件名 YYYYMMDD+4位随机数
	* @return
	* @version 1.0
	 * @throws IOException 
	 */
	public static String getFileFullPath(String fileName){
		if(CMyString.isEmpty(fileName))
			return "";
		return fileName.substring(0,4) + "/" + fileName.substring(4,6) + "/" + fileName.substring(6,8) + "/" + fileName;
	}
	
	/**
	 * 
	* Description:根据文件名生成文件的相对路径<BR>   
	* @author zhangzun
	* @date 2014-4-17 下午02:04:09
	* @param fileName 文件名 YYYYMMDD+4位随机数
	* @return
	* @version 1.0
	 * @throws IOException 
	 */
	public static String getFileFullPath(String basePath,String fileName){
		if(CMyString.isEmpty(basePath) || CMyString.isEmpty(fileName))
			return "";
		String fullPath = basePath;
		if(!fullPath.endsWith(File.separator)){
			fullPath += File.separator;
		}
		
		fullPath += fileName.substring(0,4)+File.separator+fileName.substring(4,6)
		+ File.separator + fileName.substring(6,8) + File.separator;

		return fullPath+fileName;
	}
	
	/**
	 * 
	* Description:生成文件的名字：YYYYMMDD+当天的毫秒数+4位随机数 <BR>   
	* @author zhangzun
	* @date 2014-4-17 下午02:20:10
	* @return
	* @version 1.0
	 */
	public static String getFileName(){
		return DateUtil.format(new Date(), "yyyyMMddhhmmssSSS");
	}
	
	/**
	 * 
	* Description: 通过文件名称来生成文件的存储路径 <BR>   
	* @author jin.yu
	* @date 2014-4-24 上午10:26:59
	* @param basePath 文件存储路径
	* @param fileName 文件名称
	* @return String 文件的最终存储路径
	* @version 1.0
	 */
	public static String createFolder(String basePath,String fileName){
		String firstFolder = fileName.substring(0,4);//第一层文件夹路径
		String secondFolder = fileName.substring(4,6);//第二层文件夹路径
		String thirdFolder = fileName.substring(6,8);//第三层文件夹路径
		//创建文件夹
		File thirdPath=new File(basePath + File.separator + firstFolder + File.separator + secondFolder + File.separator + thirdFolder);
		if(!thirdPath.exists()){
			thirdPath.mkdirs();
		}
		return thirdPath.getPath();
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println(createFolder("E:\\","201404232030"));
		
		System.out.println(getFileFullPath("20140424041537058.jpg"));
	}
}
