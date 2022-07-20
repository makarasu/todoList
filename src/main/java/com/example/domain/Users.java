package com.example.domain;

public class Users {
	
	private Integer id;
	private String email;
	private String password;
	private String secretQuestion;
	private String secretAnswer;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecretQuestion() {
		return secretQuestion;
	}
	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}
	public String getSecretAnswer() {
		return secretAnswer;
	}
	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}
	@Override
	public String toString() {
		return "Users [id=" + id + ", email=" + email + ", password=" + password + ", secretQuestion=" + secretQuestion
				+ ", secretAnswer=" + secretAnswer + "]";
	}

}
