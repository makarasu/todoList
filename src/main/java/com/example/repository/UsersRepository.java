package com.example.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.example.domain.Users;
import com.example.form.UsersForm;

@Repository
public class UsersRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public Users findByEmail(UsersForm form) {
		try {
			String jpql = "SELECT u FROM Users u WHERE u.email=:email";
			TypedQuery<Users> query = entityManager.createQuery(jpql, Users.class);
			query.setParameter("email", form.getEmail());
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Users findBySecrets(UsersForm form) {
		try {
			String jpql = "SELECT u FROM Users u WHERE u.email=:email AND u.secretAnswer1=:secretAnswer1 "
					+ "AND u.secretAnswer2=:secretAnswer2 AND u.secretAnswer3=:secretAnswer3";
			TypedQuery<Users> query = entityManager.createQuery(jpql, Users.class);
			query.setParameter("email", form.getEmail()).setParameter("secretAnswer1", form.getSecretAnswer1())
					.setParameter("secretAnswer2", form.getSecretAnswer2())
					.setParameter("secretAnswer3", form.getSecretAnswer3());
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
