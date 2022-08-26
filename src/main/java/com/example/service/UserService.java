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

import com.example.domain.DeletedUser;
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

	private static int TOKEN_LENGTH = 16;

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
		return insertToken(users);
	}

	/**
	 * ユーザーの退会処理 <br>
	 * ユーザーDBから情報を削除する前に、退会済みユーザーDBにユーザー情報を登録する。
	 * ※後程問い合わせがあった場合（誤って退会してしまった、など）に対応するためであり、ユーザーパスワードは登録しない。 パスワードは復元後に再設定してもらう。
	 * 
	 * @param form
	 * @param token
	 * @throws NoSuchAlgorithmException
	 */
	@Transactional
	public DeletedUser secessionUser(UsersForm form, String token) throws NoSuchAlgorithmException {
		form = changePasswordHash(form);
		Users selectedUser = usersRepository.findByToken(token);
		if (!(selectedUser.getPassword().equals(form.getPassword()))) {
			return null;
		}
		Timestamp timestamp = generateNowDate();
		DeletedUser deletedUser = new DeletedUser();
		deletedUser.setUserId(selectedUser);
		deletedUser.setUserName(selectedUser.getName());
		deletedUser.setEmail(selectedUser.getEmail());
		deletedUser.setSecretAnswer1(selectedUser.getSecretAnswer1());
		deletedUser.setSecretAnswer2(selectedUser.getSecretAnswer2());
		deletedUser.setSecretAnswer3(selectedUser.getSecretAnswer3());
		deletedUser.setSecessionDate(timestamp);
		entityManager.persist(deletedUser);
		entityManager.remove(selectedUser);
		return deletedUser;
	}

	/**
	 * トークンの登録 <br>
	 * メールアドレス・パスワードがDBと一致した場合、以降のログイン認証はトークンを使用して行う。
	 * トークンはページを移動するごとに認証・更新される。ユーザー情報とはユーザーIDで紐づけを行う。
	 * 
	 * @return
	 */
	@Transactional
	public Token insertToken(Users users) {
		Token token = new Token();
		Date nowDate = generateNowDate();
		token.setUserId(users);
		token.setToken(createToken());
		token.setGenerateDate(nowDate);
		token.setUpdateDate(nowDate);
		entityManager.persist(token);
		return token;
	}

	/**
	 * トークンの更新 <br>
	 * ログイン後にページを移動するたび、トークンの認証・更新を行う。
	 * 更新時間もトークン更新に併せて登録し、最終更新時間から30分が経過したトークンは有効期限切れとする。
	 * 
	 */
	@Transactional
	public Token updateToken(Token token) {
		token.setToken(createToken());
		token.setUpdateDate(generateNowDate());
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
	 * パスワードの更新 <br>
	 * パスワードをハッシュ化し、DBの更新を行う。
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
	 * トークンの生成 <br>
	 * 16桁の英数字文字列を生成する。ログインする際に初回生成・登録を行い、その後ページを移動するたびに更新する。
	 * 
	 * @return
	 */
	public String createToken() {
		byte token[] = new byte[TOKEN_LENGTH];
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
	 * 受け取ったトークンの有効性チェック <br>
	 * トークンが有効期限切れ（最終更新から30分経過）しているか、DBに登録が無いものは不正
	 * 
	 * @param token
	 * @return
	 */
	public Token checkToken(String token) {
		return usersRepository.findByToken(token, generateCheckDate());
	}

	/**
	 * 有効期限切れトークンの削除 <br>
	 * 最終更新から30分が経過しているトークンを削除する
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
		return Timestamp.valueOf(localDateTime);
	}

	/**
	 * 現在時刻の取得 <br>
	 * トークン登録・更新、ユーザー退会時の退会済みユーザーDB登録時に使用する
	 * 
	 * @return
	 */
	public Timestamp generateNowDate() {
		return Timestamp.valueOf(LocalDateTime.now());
	}

	/**
	 * パスワードのハッシュ化 <br>
	 * パスワードをDBに登録する際、ハッシュ化処理を行う。また、ログイン時・退会時のパスワード一致確認を行う際にも入力されたパスワードをハッシュ化してDBと比較する。
	 * 
	 * @param form 該当ユーザー情報が格納されたformクラス
	 * @return ハッシュ化したパスワードが格納されたformクラスを返す
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
