package com.example.domain;

import java.sql.Date;

public class Token {
	
	private String token;
	private Date generateDate;
	private Date updateDate;
	private Integer userId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getGenerateDate() {
		return generateDate;
	}
	public void setGenerateDate(Date generateDate) {
		this.generateDate = generateDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "Token [token=" + token + ", generateDate=" + generateDate + ", updateDate=" + updateDate + ", userId="
				+ userId + "]";
	}
}
