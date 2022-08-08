package com.example.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.example.domain.Users;

@Repository
public class MyPageRepository {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * 渡されたトークンからユーザー情報を取得する
	 * 
	 * @param token
	 * @return
	 */
	public Users findByToken(String token) {
		try {
			String jpql = "SELECT u FROM Users u, Token t WHERE u.id=t.userId AND t.token=:token";
			TypedQuery<Users> query = entityManager.createQuery(jpql, Users.class);
			query.setParameter("token", token);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
