package com.example.service;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.TodoList;
import com.example.domain.Token;
import com.example.form.TodoListForm;
import com.example.repository.TodoListRepository;

@Service
public class ToDoListService {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	UserService userService;

	@Autowired
	TodoListRepository todoListRepository;

	/**
	 * to-doリストの取得 tokenが無効の場合は取得結果を返さない
	 * 
	 * @param token
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TodoList> findList(String token) {
		List<TodoList> todoLists = todoListRepository.findByIdTodo(token);
		return todoLists;
	}

	/**
	 * todoリストの追加をする
	 * 
	 * @return
	 * @throws ParseException
	 */
	@Transactional
	public TodoList insertTodo(TodoListForm form, String token) throws ParseException {
		Token result = userService.checkToken(token);
		TodoList todoList = new TodoList();
		todoList.setImportance(form.getImportance());
		todoList.setTodo(form.getTodo());
		todoList.setTerm(form.getTerm());
		todoList.setCategory(form.getCategory());
		todoList.setMemo(form.getMemo());
		todoList.setEnforcement(false);
		todoList.setUserId(result.getUserId());
		entityManager.persist(todoList);
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
