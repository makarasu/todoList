package com.example.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TodoListForm {

	private Integer id;
	@NotBlank(message = "重要度を選択してください")
	private String importance;
	@NotBlank(message = "to-doを入力してください")
	private String todo;
	@Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = "実施期限を入力してください")
	private String term;
	@NotBlank(message = "カテゴリを選択してください")
	private String category;
	private String memo;
	private boolean enforcement;
	private Integer userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public boolean isEnforcement() {
		return enforcement;
	}

	public void setEnforcement(boolean enforcement) {
		this.enforcement = enforcement;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "TodoListForm [id=" + id + ", importance=" + importance + ", todo=" + todo + ", term=" + term
				+ ", category=" + category + ", memo=" + memo + ", enforcement=" + enforcement + ", userId=" + userId
				+ "]";
	}

}
