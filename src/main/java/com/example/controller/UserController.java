package com.example.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Users;
import com.example.form.UsersForm;
import com.example.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	HttpSession session;

	@Autowired
	UserService userService;

	@ModelAttribute
	public UsersForm usersForm() {
		return new UsersForm();
	}

	/**
	 * ユーザー登録画面表示
	 * 
	 * @return
	 */
	@RequestMapping("/registration")
	public String registrate(Users users) {
		session.setAttribute("registrateUser", users);
		session.removeAttribute("error");
		return "user_registrate";
	}

	/**
	 * ユーザー登録確認画面表示 <br>
	 * 同一メールアドレスが使用されている場合は登録完了せずに登録画面に戻る
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * @param secretQuestion1
	 * @param secretQuestion2
	 * @param secretQuestion3
	 * @param secretAnswer1
	 * @param secretAnswer2
	 * @param secretAnswer3
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping("/registrationConfirmation")
	public String registrationUser(UsersForm form, Model model) throws NoSuchAlgorithmException {
		boolean result = userService.checkEmail(form);
		model.addAttribute("registrateUser", form);
		if (result == false) {
			session.setAttribute("error", "このメールアドレスは使用されています。");
			return "user_registrate";
		} else {
			model.addAttribute("user", form);
			return "registration_confirmation";
		}
	}

	@RequestMapping("/registrationComplete")
	public String registrationComplete(UsersForm form) throws NoSuchAlgorithmException {
		boolean result = userService.registrationUser(form);
		if (result == false) {
			return "redirect:/user/registration";
		}
		session.removeAttribute("error");
		return "registration_complete";
	}

}
