package com.example.repository;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.domain.Token;
import com.example.domain.Users;
import com.example.form.UsersForm;

@Repository
public class UsersRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	/**
	 * 入力されたメールアドレスが登録されているか
	 * 
	 * @param form
	 * @return DBから取得した値を返す。DBにデータが無い場合はnullを返す
	 */
	public Users findByEmail(UsersForm form) {
		try {
			String jpql = "SELECT u FROM Users u WHERE u.email=:email";
			TypedQuery<Users> query = entityManager.createQuery(jpql, Users.class);
			query.setParameter("email", form.getEmail());
			return query.getSingleResult();
		} catch (NonUniqueResultException e) {
			return null;
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * メールアドレスと秘密の質問の答えが一致するものがあるか
	 * 
	 * @param form
	 * @return 検索結果を返す。DBに値が無い場合はnullを返す
	 */
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

	/**
	 * 入力されたメールアドレスとパスワードが一致するものがあるか
	 * 
	 * @param form
	 * @return 検索結果を返す。無ければnullを返す。
	 */
	public Users findByEmailAndPassword(UsersForm form) {
		try {
			String jpql = "SELECT u FROM Users u WHERE u.email=:email AND u.password=:password";
			TypedQuery<Users> query = entityManager.createQuery(jpql, Users.class);
			query.setParameter("email", form.getEmail()).setParameter("password", form.getPassword());
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 渡されたトークンが有効期限内か
	 * 
	 * @param token
	 * @param timestamp
	 * @return
	 */
	public Token findByToken(String token, Timestamp timestamp) {
		System.out.println("token:" + token);
		System.out.println("time:" + timestamp);
		try {
			String jpql = "SELECT t FROM Token t WHERE t.token=:token AND t.updateDate>=:checkDate";
			TypedQuery<Token> query = entityManager.createQuery(jpql, Token.class);
			query.setParameter("token", token).setParameter("checkDate", timestamp);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 有効期限切れのトークンをテーブルから削除する
	 * 
	 * @param timestamp
	 */
	public void deleteInvalidToken(Timestamp timestamp) {
		try {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			String jpql = "DELETE FROM Token t WHERE t.updateDate<:checkDate";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("checkDate", timestamp).executeUpdate();
			entityManager.getTransaction().commit();
			entityManager.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
