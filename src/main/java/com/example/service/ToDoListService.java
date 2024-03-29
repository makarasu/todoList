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
	 * to-doリストの取得
	 * 
	 * @param token
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TodoList> findList(String token, boolean enforcement) {
		return todoListRepository.findByIdTodo(token, enforcement);
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
		todoList.setImportance(Integer.valueOf(form.getImportance()));
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
	public void updateTodo(String token, Integer enforcement) {
		TodoList todoList = entityManager.find(TodoList.class, enforcement);
		todoList.setEnforcement(true);
		entityManager.merge(todoList);
	}

	/**
	 * todoリストの並び替え
	 * 
	 * @param token
	 * @param order
	 * @return
	 */
	@Transactional
	public List<TodoList> listOrder(String token, Integer order, boolean bool) {
		return todoListRepository.listOrder(token, order, bool);
	}
}
