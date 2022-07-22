package com.example.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Users;
import com.example.form.UsersForm;
import com.example.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	/**
	 * 登録済みメールアドレスではないか確認
	 * 
	 * @param form
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public boolean checkEmail(UsersForm form) throws NoSuchAlgorithmException {
		Users checkUsers = userRepository.findByEmail(form);
		if (checkUsers != null) {
			return false;
		}
		return true;
	}

	/**
	 * ユーザー情報登録
	 * 
	 * @param form
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public boolean registrationUser(UsersForm form) throws NoSuchAlgorithmException {
		form = changePasswordHash(form);
		boolean result = userRepository.insertUser(form);
		return result;
	}

	/**
	 * ログイン情報チェックとトークンの発行
	 * 
	 * @param form
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public boolean loginCheck(UsersForm form) throws NoSuchAlgorithmException {
		form = changePasswordHash(form);
		Users users = userRepository.findByEmailAndPassword(form);
		if (users == null) {
			return false;
		}
		return true;
	}

	/**
	 * 秘密の質問取得
	 * 
	 * @param email
	 * @return
	 */
	public UsersForm findByEmail(UsersForm form) {
		Users users = userRepository.findByEmailForQuestion(form);
		if (users == null) {
			return null;
		}
		form.setEmail(users.getEmail());
		form.setSecretQuestion1(users.getSecretQuestion1());
		form.setSecretQuestion2(users.getSecretQuestion2());
		form.setSecretQuestion3(users.getSecretQuestion3());
		return form;
	}

	/**
	 * 秘密の質問と回答・メールアドレスの照合
	 * 
	 * @param form
	 * @return
	 */
	public Users findBySecretQuestion(UsersForm form) {
		Users users = userRepository.findBySecretQuestions(form);
		return users;
	}

	/**
	 * パスワードの更新
	 * 
	 * @param form
	 * @throws NoSuchAlgorithmException
	 */
	public void updatePassword(UsersForm form) throws NoSuchAlgorithmException {
		form = changePasswordHash(form);
		userRepository.updatePassword(form);
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
