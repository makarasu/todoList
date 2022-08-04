package com.example.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "todo_list")
public class TodoList implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "importance")
	private Integer importance;

	@Column(name = "todo")
	private String todo;

	@Column(name = "term")
	private Date term;

	@Column(name = "category")
	private String category;

	@Column(name = "memo")
	private String memo;

	@Column(name = "enforcement")
	private boolean enforcement;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getImportance() {
		return importance;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public Date getTerm() {
		return term;
	}

	public void setTerm(String term) {
		Date dateTerm = Date.valueOf(term);
		this.term = dateTerm;
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

	public Users getUserId() {
		return userId;
	}

	public void setUserId(Users userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "TodoList [id=" + id + ", importance=" + importance + ", todo=" + todo + ", term=" + term + ", category="
				+ category + ", memo=" + memo + ", enforcement=" + enforcement + ", userId=" + userId + "]";
	}
}
