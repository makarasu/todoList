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

	public boolean checkEmail(UsersForm form) throws NoSuchAlgorithmException {
		Users checkUsers = userRepository.findByEmail(form);
		if (checkUsers != null) {
			return false;
		}
		return true;
	}

	public boolean registrationUser(UsersForm form) throws NoSuchAlgorithmException {
		form = changePasswordHash(form);
		boolean result = userRepository.insertUser(form);
		return result;
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
