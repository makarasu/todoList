package com.example.service;

import java.security.NoSuchAlgorithmException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Users;
import com.example.form.UsersForm;
import com.example.repository.MyPageRepository;
import com.example.repository.UsersRepository;

@Service
public class MyPageService {

	@Autowired
	UserService userService;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	MyPageRepository myPageRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public Users findByToken(String token) {
		Users users = myPageRepository.findByToken(token);
		return users;
	}

	@Transactional
	public void changePasswordFromMypage(UsersForm form) throws NoSuchAlgorithmException {
		form = userService.changePasswordHash(form);
		Users users = usersRepository.findByEmail(form);
		users.setPassword(form.getPassword());
		entityManager.merge(users);
	}
}
