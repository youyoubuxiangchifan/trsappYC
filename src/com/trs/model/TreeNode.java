/**
 * Description:<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: TreeNode
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-19 下午05:13:36
 * @version 1.0
 */
package com.trs.model;

import java.util.List;

/**
 * Description: 节点对象，保存节点所有属性<BR>
 * 
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * author liujian
 * ClassName: TreeNode
 * Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * date 2014-3-19 下午05:13:36
 * version 1.0
 */
public class TreeNode {
	private String id; //节点ID
	private String pId; //父节点ID
	private String name; //节点名称
	private String url; //节点a标签href值
	private String target; //节点a标签target属性
	private String rel;  //节点a标签rel属性
	private Boolean checked = false; //节点的勾选状态 ，true：被勾选，false：未勾选
	private Boolean open =false; //节点的展开状态，true：展开，false:不展开
	private Boolean isParent =false; //节点是否是父节点，异步加载节点信息时用
	private Boolean parentConChild =true; //父节点是否有子组织
	private Boolean chkDisabled = false;

	/** 
	* Description: 获取节点ID <BR>   
	* author liujian
	* date 2014-3-20 下午01:17:20
	* @return id
	* version 1.0
	*/
	public String getId() {
		return id;
	}

	/** 
	* Description: 设置节点ID <BR>   
	* author liujian
	* date 2014-3-20 下午01:18:12
	* @param id
	* version 1.0
	*/
	public void setId(String id) {
		this.id = id;
	}

	/** 
	* Description: 获取父节点ID <BR>   
	* author liujian
	* date 2014-3-20 下午01:18:40
	* @return pId
	* version 1.0
	*/
	public String getPId() {
		return pId;
	}

	/** 
	* Description: 设置获取父节点ID <BR>   
	* author liujian
	* date 2014-3-20 下午01:19:44
	* @param id
	* version 1.0
	*/
	public void setPId(String id) {
		pId = id;
	}

	/** 
	* Description: 获取节点名称 <BR>   
	* author liujian
	* date 2014-3-20 下午01:20:28
	* @return name
	* version 1.0
	*/
	public String getName() {
		return name;
	}

	/** 
	* Description: 设置节点名称 <BR>   
	* author liujian
	* date 2014-3-20 下午01:21:25
	* @param name
	* version 1.0
	*/
	public void setName(String name) {
		this.name = name;
	}
	
	/** 
	* Description: 获取节点链接属性 <BR>   
	* author liujian
	* date 2014-3-20 下午01:30:52
	* @return url
	* version 1.0
	*/
	public String getUrl() {
		return url;
	}
	/** 
	* Description: 设置节点链接href属性 <BR>   
	* author liujian
	* date 2014-3-20 下午01:31:33
	* @param url
	* version 1.0
	*/
	public void setUrl(String url) {
		this.url = url;
	}
	
	/** 
	* Description: 获取链接target属性 <BR>   
	* author liujian
	* date 2014-3-20 下午01:34:00
	* @return target
	* version 1.0
	*/
	public String getTarget() {
		return target;
	}
	/** 
	* Description: 设置链接target属性 <BR>   
	* author liujian
	* date 2014-3-20 下午01:34:44
	* @param target
	* version 1.0
	*/
	public void setTarget(String target) {
		this.target = target;
	}
	/** 
	* Description: 获取链接rel属性 <BR>   
	* author liujian
	* date 2014-3-20 下午01:35:06
	* @return rel
	* version 1.0
	*/
	public String getRel() {
		return rel;
	}
	/** 
	* Description: 设置链接rel属性 <BR>   
	* author liujian
	* date 2014-3-20 下午01:35:44
	* @param rel
	* version 1.0
	*/
	public void setRel(String rel) {
		this.rel = rel;
	}
	/** 
	* Description: 获取节点选择状态 <BR>   
	* author liujian
	* date 2014-3-20 下午01:22:02
	* @return checked
	* version 1.0
	*/
	public Boolean isChecked() {
		return checked;
	}

	/** 
	* Description: 设置节点选择状态 <BR>   
	* author liujian
	* date 2014-3-20 下午01:22:51
	* @param checked
	* version 1.0
	*/
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	/** 
	* Description: 获取节点展开状态 <BR>   
	* author liujian
	* date 2014-3-20 下午01:23:42
	* @return open
	* version 1.0
	*/
	public Boolean isOpen() {
		return open;
	}

	/** 
	* Description: 设置节点暂开状态 <BR>   
	* author liujian
	* date 2014-3-20 下午01:24:50
	* @param open
	* version 1.0
	*/
	public void setOpen(Boolean open) {
		this.open = open;
	}
	/** 
	* Description: 获取节点展开状态 <BR>   
	* author liujian
	* date 2014-3-20 下午01:23:42
	* @return open
	* version 1.0
	*/
	public Boolean isParent() {
		return isParent;
	}
	/** 
	* Description: 设置节点暂开状态 <BR>   
	* author liujian
	* date 2014-3-20 下午01:24:50
	* @param open
	* version 1.0
	*/
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}	
	
	public Boolean getChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(Boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	/** 
	* Description: 构造树节点<BR>
	* @param id
	* @param pId
	* @param name
	* @param checked
	* @param open 
	*/ 
	public TreeNode(String id, String pId, String name,String url,String target,String rel, Boolean checked, Boolean open,Boolean isParent, Boolean chkDisabled) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.url = url;
		this.target = target;
		this.rel = rel;
		this.checked = checked;
		this.open = open;
		this.isParent = isParent;
		this.chkDisabled = chkDisabled;
	}
	public TreeNode() {
		super();
	}
    /** 
    * Description: 拼接节点信息为json字符串 <BR>   
    * author liujian
    * date 2014-3-20 下午05:15:07
    * @param roles
    * @return
    * version 1.0
    */
    public static String getTreeJsonStr(List<TreeNode> treeNodes){  
        //Check Roles is null  
        StringBuffer sb = new StringBuffer();  
        TreeNode r = null;  
            sb.append("[");  
            for(int i=0;i<treeNodes.size();i++){  
                r=(TreeNode) treeNodes.get(i);  
                sb.append("{id:").append(r.getId()).append(",pId:").append(r.getPId()).append(",name:\"")  
                .append(r.getName()).append("\"").append(",url:\"").append(r.getUrl()).append("\"")
                .append(",target:\"").append(r.getTarget()).append("\"")
                .append(",emailx:\"xxxx").append("\"")
                .append(",checked:").append(r.isChecked()).append(",isParent:").append(r.isParent()).append(",open:true")
                .append(",chkDisabled:").append(r.getChkDisabled()).append("},");  
            }
            if(treeNodes.size()>0){
            	return sb.substring(0,sb.length()-1)+"]";
            }
            return sb.substring(0,sb.length())+"]";  
    } 	

}
