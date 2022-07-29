package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.TodoList;

@Service
public class ToDoListService {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * to-doリストの取得 tokenが無効の場合は取得結果を返さない
	 * 
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<TodoList> findList(String token) {
		List<TodoList> todoLists = (List<TodoList>) entityManager.find(TodoList.class, token);
		return todoLists;

	}

	/**
	 * todoリストの追加をする
	 * 
	 * @return
	 */
	@Transactional
	public TodoList insertTodo() {
		TodoList todoList = new TodoList();
		return todoList;
	}

	/**
	 * todoリストの更新を行う
	 * 
	 * @return
	 */
	@Transactional
	public void updateTodo() {

	}

	/**
	 * todoの削除
	 * 
	 * @return
	 */
	@Transactional
	public void deleteTodo() {

	}
}
