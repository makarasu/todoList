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
	private EntityManager entityManager;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	public List<TodoList> findByIdTodo(String token, boolean enforcement) {
		try {
			String jpql = "SELECT l FROM TodoList l ,Token t WHERE l.userId=t.userId AND t.token=:token AND l.enforcement=:enforcement";
			TypedQuery<TodoList> query = entityManager.createQuery(jpql, TodoList.class);
			query.setParameter("token", token).setParameter("enforcement", enforcement);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<TodoList> listOrder(String token, Integer order, boolean bool) {
		try {
			String jpql = "SELECT l FROM TodoList l , Token t WHERE l.userId=t.userId AND t.token=:token AND l.enforcement=:bool";
			if (order == 0) {
				jpql += " ORDER BY l.userId";
			} else if (order == 1) {
				jpql += " ORDER BY l.importance ,l.term";
			} else if (order == 2) {
				jpql += " ORDER BY l.importance DESC ,l.term";
			} else if (order == 3) {
				jpql += " ORDER BY l.term ,l.importance";
			} else if (order == 4) {
				jpql += " ORDER BY l.term DESC ,l.importance";
			} else if (order == 5) {
				jpql += " ORDER BY l.category ,l.importance";
			}
			TypedQuery<TodoList> query = entityManager.createQuery(jpql, TodoList.class);
			query.setParameter("token", token).setParameter("bool", bool);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
