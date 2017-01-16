package com.changhong.oto.common.utils.mail;

import java.util.List;



public class MailInfo {
	// 设置服务器
	private  String VALUE_SMTP;
	private  String MAIL_PORT;
	// 发件人用户名、密码
	private String SEND_USER;
	private String SEND_PWD; 
	
	public MailInfo() {}
	
	
	
	public MailInfo(String vALUE_SMTP, String mAIL_PORT, String sEND_USER,
			String sEND_PWD) {
		this.VALUE_SMTP = vALUE_SMTP;
		this.MAIL_PORT = mAIL_PORT;
		this.SEND_USER = sEND_USER;
		this.SEND_PWD = sEND_PWD;
	}



	public String getVALUE_SMTP() {
		return VALUE_SMTP;
	}
	public void setVALUE_SMTP(String vALUE_SMTP) {
		VALUE_SMTP = vALUE_SMTP;
	}
	public String getMAIL_PORT() {
		return MAIL_PORT;
	}
	public void setMAIL_PORT(String mAIL_PORT) {
		MAIL_PORT = mAIL_PORT;
	}
	public String getSEND_USER() {
		return SEND_USER;
	}
	public void setSEND_USER(String sEND_USER) {
		SEND_USER = sEND_USER;
	}
	public String getSEND_PWD() {
		return SEND_PWD;
	}
	public void setSEND_PWD(String sEND_PWD) {
		SEND_PWD = sEND_PWD;
	}
	
	
	//--------------------------
	/**邮件标题*/
	private String headName; 
	/**邮件内容*/
	private String sendHtml;
	/**收件人*/
	private String receiveUsers;
	/**抄送人*/
	private String receiveUsersCC;
	/**秘密抄送人*/
	private String receiveUsersBCC;
	/**图片*/
	private String imgSrc;
	/**邮件附件*/
	private List<String> files;
	
	
	

	public MailInfo(String headName, String sendHtml, String receiveUsers,
			String receiveUsersCC, String receiveUsersBCC, String imgSrc,
			List<String> files) {
		this.headName = headName;
		this.sendHtml = sendHtml;
		this.receiveUsers = receiveUsers;
		this.receiveUsersCC = receiveUsersCC;
		this.receiveUsersBCC = receiveUsersBCC;
		this.imgSrc = imgSrc;
		this.files = files;
	}



	public String getHeadName() {
		return headName;
	}
	public void setHeadName(String headName) {
		this.headName = headName;
	}
	public String getSendHtml() {
		return sendHtml;
	}
	public void setSendHtml(String sendHtml) {
		this.sendHtml = sendHtml;
	}
	public String getReceiveUsers() {
		return receiveUsers;
	}
	public void setReceiveUsers(String receiveUsers) {
		this.receiveUsers = receiveUsers;
	}
	public String getReceiveUsersCC() {
		return receiveUsersCC;
	}
	public void setReceiveUsersCC(String receiveUsersCC) {
		this.receiveUsersCC = receiveUsersCC;
	}
	public String getReceiveUsersBCC() {
		return receiveUsersBCC;
	}
	public void setReceiveUsersBCC(String receiveUsersBCC) {
		this.receiveUsersBCC = receiveUsersBCC;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public List<String> getFiles() {
		return files;
	}
	public void setFiles(List<String> files) {
		this.files = files;
	}
	
	
	
	private Integer error;  // 错误编码
	private String msg;  //消息
	private Boolean success;  //是否成功
	private Object data;  //是否成功
	
	
	
	public MailInfo(Boolean success, String msg,Integer error,Object data) {
		super();
		this.error = error;
		this.msg = msg;
		this.success = success;
		this.data = data;
	}
	public MailInfo(Boolean success, String msg,Integer error) {
		super();
		this.error = error;
		this.msg = msg;
		this.success = success;
	}



	public Integer getError() {
		return error;
	}
	public void setError(Integer error) {
		this.error = error;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	
	
}
