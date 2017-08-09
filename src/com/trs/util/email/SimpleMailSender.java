/**
 * Description:<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: SimpleMailSender
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 上午11:31:26
 * @version 1.0
 */
package com.trs.util.email;

import java.util.List; 
import java.util.Properties; 
  
import javax.mail.MessagingException; 
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.AddressException; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMessage.RecipientType; 

/**
 * @Description: TODO(描述类的作用)<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liujian
 * @ClassName: SimpleMailSender
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 上午11:31:26
 * @version 1.0
 */
public class SimpleMailSender { 
	  
    /** 
     * 发送邮件的props文件 
     */
    private final transient Properties props = System.getProperties(); 
    /** 
     * 邮件服务器登录验证 
     */
    private transient MailAuthenticator authenticator; 
  
    /** 
     * 邮箱session 
     */
    private transient Session session; 
  
    /** 
     * 初始化邮件发送器 
     *  
     * @param smtpHostName 
     *                SMTP邮件服务器地址 
     * @param username 
     *                发送邮件的用户名(地址) 
     * @param password 
     *                发送邮件的密码 
     */
    public SimpleMailSender(final String smtpHostName, final String username, 
        final String password) { 
    	init(username, password, smtpHostName); 
    } 
  
    /** 
     * 初始化邮件发送器 
     *  
     * @param username 
     *                发送邮件的用户名(地址)，并以此解析SMTP服务器地址 
     * @param password 
     *                发送邮件的密码 
     */
    public SimpleMailSender(final String username, final String password) { 
    //通过邮箱地址解析出smtp服务器，对大多数邮箱都管用 
    final String smtpHostName = "smtp." + username.split("@")[1]; 
    init(username, password, smtpHostName); 
  
    } 
  
    /** 
     * 初始化 
     *  
     * @param username 
     *                发送邮件的用户名(地址) 
     * @param password 
     *                密码 
     * @param smtpHostName 
     *                SMTP主机地址 
     */
    private void init(String username, String password, String smtpHostName) { 
    // 初始化props 
    props.put("mail.smtp.auth", "true"); 
    props.put("mail.smtp.host", smtpHostName); 
    // 验证 
    authenticator = new MailAuthenticator(username, password); 
    // 创建session 
    session = Session.getInstance(props, authenticator); 
    } 
  
    /** 
     * 发送邮件 
     *  
     * @param recipient 
     *                收件人邮箱地址 
     * @param subject 
     *                邮件主题 
     * @param content 
     *                邮件内容 
     * @throws AddressException 
     * @throws MessagingException 
     */
    public void send(String recipient, String subject, Object content) 
        throws AddressException, MessagingException { 
    // 创建mime类型邮件 
    final MimeMessage message = new MimeMessage(session); 
    // 设置发信人 
    message.setFrom(new InternetAddress(authenticator.getUsername())); 
    // 设置收件人 
    message.setRecipient(RecipientType.TO, new InternetAddress(recipient)); 
    // 设置主题 
    message.setSubject(subject); 
    // 设置邮件内容 
    message.setContent(content.toString(), "text/html;charset=utf-8"); 
    // 发送 
    Transport.send(message); 
    } 
  
    /** 
     * 群发邮件 
     *  
     * @param recipients 
     *                收件人们 
     * @param subject 
     *                主题 
     * @param content 
     *                内容 
     * @throws AddressException 
     * @throws MessagingException 
     */
    public void send(List recipients, String subject, Object content) 
        throws AddressException, MessagingException { 
    // 创建mime类型邮件 
    final MimeMessage message = new MimeMessage(session); 
    // 设置发信人 
    message.setFrom(new InternetAddress(authenticator.getUsername())); 
    // 设置收件人们 
    final int num = recipients.size(); 
    InternetAddress[] addresses = new InternetAddress[num]; 
    for (int i = 0; i < num; i++) { 
        addresses[i] = new InternetAddress((String)recipients.get(i)); 
    } 
    message.setRecipients(RecipientType.TO, addresses); 
    // 设置主题 
    message.setSubject(subject); 
    // 设置邮件内容 
    message.setContent(content.toString(), "text/html;charset=utf-8"); 
    // 发送 
    Transport.send(message); 
    } 
  
    /** 
     * 发送邮件 
     *  
     * @param recipient 
     *                收件人邮箱地址 
     * @param mail 
     *                邮件对象 
     * @throws AddressException 
     * @throws MessagingException 
     */
    public void send(String recipient, SimpleMail mail) 
        throws AddressException, MessagingException { 
    	send(recipient, mail.getSubject(), mail.getContent()); 
    } 
    /** 
     * 发送单个邮件 
     *  
     * @param recipient 
     *                收件人邮箱地址 
     * @param mail 
     *                邮件对象 
     * @throws MessagingException 
     * @throws AddressException 
     * @throws AddressException 
     * @throws MessagingException 
     * @return 1 邮件发送成功
     * @return 0 邮件发送失败，地址验证错误 
     * @return -1 邮件发送失败，内容验证错误 
     * 
     */
    public int sendMail(String recipient, SimpleMail mail){ 
    	
    	try {
			send(recipient, mail.getSubject(), mail.getContent());
			return 1;//邮件发送成功
		} catch (AddressException e) {			
			e.printStackTrace();
			return 0;//邮件发送失败，地址验证错误
		} catch (MessagingException e) {
			e.printStackTrace();
			return 0;//邮件发送失败，内容验证错误
		} 
    } 
    /** 
     * 群发邮件 
     *  
     * @param recipients 
     *                收件人们 
     * @param mail 
     *                邮件对象 
     * @throws AddressException 
     * @throws MessagingException 
     */
    public void send(List recipients, SimpleMail mail) 
        throws AddressException, MessagingException { 
    send(recipients, mail.getSubject(), mail.getContent()); 
    } 

    
}
