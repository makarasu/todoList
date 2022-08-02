package com.example.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Token;
import com.example.domain.Users;
import com.example.form.UsersForm;
import com.example.repository.UsersRepository;

@Service
public class UserService {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	UsersRepository usersRepository;

	private static int TPOKEN_LENGTH = 16;

	/**
	 * 同一メールアドレスが登録されていないか確認する
	 * 
	 * @param email
	 * @return
	 */
	@Transactional(readOnly = true)
	public Users checkEmail(UsersForm form) {
		Users users = usersRepository.findByEmail(form);
		if (users == null) {
			return null;
		}
		return users;
	}

	/**
	 * ユーザー登録
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@Transactional
	public void insertUsers(UsersForm form) throws NoSuchAlgorithmException {
		form = changePasswordHash(form);
		Users users = new Users();
		users.setName(form.getName());
		users.setEmail(form.getEmail());
		users.setPassword(form.getPassword());
		users.setSecretAnswer1(form.getSecretAnswer1());
		users.setSecretAnswer2(form.getSecretAnswer2());
		users.setSecretAnswer3(form.getSecretAnswer3());
		entityManager.persist(users);
	}

	/**
	 * ログイン情報照会
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@Transactional
	public Token findUser(UsersForm form) throws NoSuchAlgorithmException {
		form = changePasswordHash(form);
		Users users = usersRepository.findByEmailAndPassword(form);
		if (users == null) {
			return null;
		}
		Token token = insertToken(users);

		return token;
	}

	/**
	 * トークンの登録
	 * 
	 * @return
	 */
	@Transactional
	public Token insertToken(Users users) {
		Token token = new Token();
		String createToken = createToken();
		Date nowDate = generateNowDate();
		token.setUserId(users);
		token.setToken(createToken);
		token.setGenerateDate(nowDate);
		token.setUpdateDate(nowDate);
		entityManager.persist(token);
		return token;
	}

	/**
	 * トークンの更新
	 */
	@Transactional
	public Token updateToken(Token token) {
		String newToken = createToken();
		Timestamp timestamp = generateNowDate();
		token.setToken(newToken);
		token.setUpdateDate(timestamp);
		entityManager.merge(token);
		return token;
	}

	// @Transactional(readOnly = true)
	public Users checkSecretAnswer(UsersForm form) {
		Users users = usersRepository.findBySecrets(form);
		if (users == null) {
			return null;
		}
		return users;
	}

	/**
	 * パスワードの更新
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@Transactional
	public void updatePassword(UsersForm form) throws NoSuchAlgorithmException {
		form = changePasswordHash(form);
		Users users = usersRepository.findByEmail(form);
		users.setPassword(form.getPassword());
		entityManager.merge(users);
	}

	/**
	 * トークンの生成
	 * 
	 * @return
	 */
	public String createToken() {
		byte token[] = new byte[TPOKEN_LENGTH];
		StringBuffer buffer = new StringBuffer();
		SecureRandom random = null;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			random.nextBytes(token);
			for (int i = 0; i < token.length; i++) {
				buffer.append(String.format("%02x", token[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * 受け取ったトークンが有効期限切れかチェックする
	 * 
	 * @param token
	 * @return
	 */
	public Token checkToken(String token) {
		Timestamp timestamp = generateCheckDate();
		Token result = usersRepository.findByToken(token, timestamp);
		if (result == null) {
			return null;
		} else {
			return result;
		}
	}

	/**
	 * 有効期限切れトークンの削除
	 */
	public void deleteInvalidToken() {
		Timestamp timestamp = generateCheckDate();
		usersRepository.deleteInvalidToken(timestamp);
	}

	/**
	 * 現在時刻の30分前の取得 <br>
	 * ※トークンの有効時間が最終更新から30分のため
	 * 
	 * @return
	 */
	public Timestamp generateCheckDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.minusMinutes(30);
		Timestamp timestamp = Timestamp.valueOf(localDateTime);
		return timestamp;
	}

	public Timestamp generateNowDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(localDateTime);
		return timestamp;
	}

	/**
	 * パスワードのハッシュ化
	 * 
	 * @param users
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public UsersForm changePasswordHash(UsersForm form) throws NoSuchAlgorithmException {
		MessageDigest hashPassword = MessageDigest.getInstance("SHA3-256");
		byte[] hashResult = hashPassword.digest(form.getPassword().getBytes());
		String hashedPassword = String.format("%40x", new BigInteger(1, hashResult));
		form.setPassword(hashedPassword);
		return form;
	}
}
