package com.example.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Token;
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

	@Autowired
	MyPageController myPageController;

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
	 * @param form
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping("/registrationConfirmation")
	public String registrationUser(UsersForm form, Model model) throws NoSuchAlgorithmException {
		Users result = userService.checkEmail(form);
		model.addAttribute("registrateUser", form);
		if (result != null) {
			session.setAttribute("error", "このメールアドレスは使用されています。");
			return "user_registrate";
		} else {
			model.addAttribute("user", form);
			return "registration_confirmation";
		}
	}

	/**
	 * ユーザー登録フォームのリセットを行う
	 * 
	 * @param form
	 * @param model
	 * @return
	 */
	@RequestMapping("/registrationReset")
	public String registrationReset(UsersForm form, Model model) {
		form.setId(null);
		form.setName(null);
		form.setEmail(null);
		form.setPassword(null);
		form.setSecretAnswer1(null);
		form.setSecretAnswer2(null);
		form.setSecretAnswer3(null);
		session.removeAttribute("error");
		return "user_registrate";
	}

	/**
	 * ユーザー登録
	 * 
	 * @param form
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping("/registrationCheck")
	public String registrationCheck(UsersForm form) throws NoSuchAlgorithmException {
		userService.insertUsers(form);
		session.removeAttribute("error");
		return "redirect:/user/registrationComplete";
	}

	/**
	 * ユーザー情報の登録完了後に登録完了画面表示
	 * 
	 * @return
	 */
	@RequestMapping("/registrationComplete")
	public String registrationComplete() {
		return "registration_complete";
	}

	/**
	 * ユーザーログイン
	 * 
	 * @return
	 */
	@RequestMapping("/login")
	public String toLogin() {
		userService.deleteInvalidToken();
		return "todo_login";
	}

	/**
	 * 入力されたメールアドレスとパスワードが一致するか確認
	 * 
	 * @param form
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping("/loginCheck")
	public String loginCheck(UsersForm form, Model model) throws NoSuchAlgorithmException {
		Token result = userService.findUser(form);
		if (result == null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードに誤りがあります");
			return "todo_login";
		}
		model.addAttribute("token", result.getToken());
		return myPageController.index(result.getToken(), model);
	}

	/**
	 * パスワード再設定用ページ表示 <br>
	 * 登録済みメールアドレスを入力する
	 * 
	 * @return
	 */
	@RequestMapping("/resetPassword")
	public String resetPassword() {
		return "resetPassword";
	}

	/**
	 * 入力されたメールアドレスが登録されているか確認し、あれば登録時に設定した秘密の質問を取得する <br>
	 * 表示するページで秘密の質問に回答する
	 * 
	 * @param email
	 * @param model
	 * @return
	 */
	@RequestMapping("/secretQuestion")
	public String secretQuestion(UsersForm form, Model model) {
		Users result = userService.checkEmail(form);
		if (result == null) {
			model.addAttribute("error", "このメールアドレスは登録されていません。");
			return "resetPassword";
		}
		return "secret_question";
	}

	/**
	 * 秘密の質問の答えと照合し、合っている場合はパスワード再設定用のページを表示する
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping("/changePassword")
	public String changePassword(UsersForm form, Model model) {
		Users users = userService.checkSecretAnswer(form);
		if (users == null) {
			model.addAttribute("error", "回答が一致しません");
			return "secret_question";
		}
		model.addAttribute("email", users.getEmail());
		return "change_password";
	}

	/**
	 * パスワードの変更を行い、ログイン画面に返す
	 * 
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping("/resetPasswordComplete")
	public String resetPasswordComplete(UsersForm form) throws NoSuchAlgorithmException {
		userService.updatePassword(form);
		return "redirect:/user/login";
	}

}
