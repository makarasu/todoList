package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UsersForm {

	private Integer id;

	@NotBlank(message = "ユーザー名を入力してください")
	private String name;

	@Email(message = "不正な入力です")
	@NotBlank(message = "メールアドレスを入力してください")
	private String email;

	@NotBlank(message = "パスワードを入力してください")
	private String password;

	@NotBlank(message = "質問1に回答してください")
	private String secretAnswer1;

	@NotBlank(message = "質問2に回答してください")
	private String secretAnswer2;

	@NotBlank(message = "質問3に回答してください")
	private String secretAnswer3;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSecretAnswer1() {
		return secretAnswer1;
	}

	public void setSecretAnswer1(String secretAnswer1) {
		this.secretAnswer1 = secretAnswer1;
	}

	public String getSecretAnswer2() {
		return secretAnswer2;
	}

	public void setSecretAnswer2(String secretAnswer2) {
		this.secretAnswer2 = secretAnswer2;
	}

	public String getSecretAnswer3() {
		return secretAnswer3;
	}

	public void setSecretAnswer3(String secretAnswer3) {
		this.secretAnswer3 = secretAnswer3;
	}

}
