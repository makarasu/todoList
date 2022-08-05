package com.example.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.domain.TodoList;

@Repository
public class TodoListRepository {

	@PersistenceContext
	private EntityManager eniEntityManager;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	public List<TodoList> findByIdTodo(String token, boolean enforcement) {
		try {
			String jpql = "SELECT l FROM TodoList l ,Token t WHERE l.userId=t.userId AND t.token=:token AND l.enforcement=:enforcement";
			TypedQuery<TodoList> query = eniEntityManager.createQuery(jpql, TodoList.class);
			query.setParameter("token", token).setParameter("enforcement", enforcement);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
}
