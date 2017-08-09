/**
 * Description:<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: MailAuthenticator
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 上午11:26:40
 * @version 1.0
 */
package com.trs.util.email;

import javax.mail.Authenticator; 
import javax.mail.PasswordAuthentication; 

/**
 * @Description: TODO(描述类的作用)<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liujian
 * @ClassName: MailAuthenticator
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 上午11:26:40
 * @version 1.0
 */
public class MailAuthenticator extends Authenticator { 
	  
    /** 
     * 用户名（登录邮箱） 
     */
    private String username; 
    /** 
     * 密码 
     */
    private String password; 
  
    /** 
     * 初始化邮箱和密码  
     * @param username 邮箱 
     * @param password 密码 
     */

    public MailAuthenticator(String username, String password) { 
    this.username = username; 
    this.password = password; 
    } 
  
    /** 
    * @Description: 获取密码 <BR>   
    * @author liujian
    * @date 2014-3-10 上午11:28:27
    * @return
    * @version 1.0
    */
    String getPassword() { 
    return password; 
    } 
  
    
    protected PasswordAuthentication getPasswordAuthentication() { 
    return new PasswordAuthentication(username, password); 
    
    } 
  
    /** 
    * @Description: 获取用户名 <BR>   
    * @author liujian
    * @date 2014-3-10 上午11:29:07
    * @return
    * @version 1.0
    */
    String getUsername() { 
    return username; 
    } 
  
    /** 
    * @Description: 设置密码 <BR>   
    * @author liujian
    * @date 2014-3-10 上午11:29:27
    * @param password
    * @version 1.0
    */
    public void setPassword(String password) { 
    this.password = password; 
    } 
  
    /** 
    * @Description: 设置用户名 <BR>   
    * @author liujian
    * @date 2014-3-10 上午11:29:43
    * @param username
    * @version 1.0
    */
    public void setUsername(String username) { 
    this.username = username; 
    } 
  
}  