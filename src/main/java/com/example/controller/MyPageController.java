package com.example.controller;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.DeletedUser;
import com.example.domain.TodoList;
import com.example.domain.Token;
import com.example.domain.Users;
import com.example.form.TodoListForm;
import com.example.form.UsersForm;
import com.example.service.MyPageService;
import com.example.service.ToDoListService;
import com.example.service.UserService;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

	@Autowired
	UserService userService;

	@Autowired
	ToDoListService toDoListService;

	@Autowired
	MyPageService myPageService;

	@ModelAttribute
	public UsersForm usersForm() {
		return new UsersForm();
	}

	@ModelAttribute
	public TodoListForm todoListForm() {
		return new TodoListForm();
	}

	/**
	 * マイページトップ画面の表示
	 * 
	 * @param token
	 * @param model
	 * @return
	 */
	@RequestMapping("/top")
	public String index(String token, Model model) {
		String view = "myPage_top";
		view = checkToken(token, view, model);
		return view;
	}

	/**
	 * 実施済みtodoの表示
	 * 
	 * @param token
	 * @param model
	 * @return
	 */
	@RequestMapping("/log")
	public String todoLog(String token, Model model) {
		String view = "todo_log";
		view = checkToken(token, view, model);
		if (view.equals("todo_log")) {
			token = model.getAttribute("token").toString();
			Boolean enforcement = true;
			List<TodoList> todoLists = toDoListService.findList(token, enforcement);
			model.addAttribute("todoList", todoLists);
		}
		return view;
	}

	/**
	 * todoリスト追加ページ表示
	 * 
	 * @param token
	 * @param model
	 * @return
	 */
	@RequestMapping("/registrateTodo")
	public String regiTodo(String token, Model model) {
		String view = "todo_insert";
		view = checkToken(token, view, model);
		return view;
	}

	/**
	 * todoリスト追加
	 * 
	 * @param form
	 * @param token
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/insertTodo")
	public String insertTodo(@Validated TodoListForm form, BindingResult result, String token, Model model)
			throws ParseException {
		if (result.hasErrors()) {
			String view = "todo_insert";
			view = checkToken(token, view, model);
			return view;
		}
		String view = "todoList";
		view = checkToken(token, view, model);
		if (view.equals("todoList")) {
			token = (String) model.getAttribute("token");
			toDoListService.insertTodo(form, token);
			Boolean enforcement = false;
			List<TodoList> todoLists = toDoListService.findList(token, enforcement);
			model.addAttribute("todoList", todoLists);
		}
		return view;
	}

	/**
	 * パスワード変更画面表示
	 * 
	 * @return
	 */
	@RequestMapping("/changePassword")
	public String changePassword(String token, UsersForm form, Model model) {
		String view = "changePasswordFromMypage";
		view = checkToken(token, view, model);
		if (view.equals("changePasswordFromMypage")) {
			token = (String) model.getAttribute("token");
			Users users = myPageService.findByToken(token);
			form.setEmail(users.getEmail());
		}
		return view;
	}

	/**
	 * パスワード更新
	 * 
	 * @param form
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping("/changePasswordComplete")
	public String changePasswordComplete(String token, UsersForm form, BindingResult result, Model model)
			throws NoSuchAlgorithmException {
		String view = "mypage_top";
		view = checkToken(token, view, model);
		if (form.getPassword().equals("") || form.getPassword() == null) {
			model.addAttribute("error", "パスワードを入力してください");
			return "changePasswordFromMypage";
		}
		if (view.equals("mypage_top")) {
			token = (String) model.getAttribute("token");
			userService.updatePassword(form);
		}
		return view;
	}

	/**
	 * 退会手続き画面表示
	 * 
	 * @return
	 */
	@RequestMapping("/secession")
	public String secession(String token, Model model) {
		String view = "user_secession";
		view = checkToken(token, view, model);
		return view;
	}

	/**
	 * 退会手続き処理
	 * 
	 * @param form
	 * @param token
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping("/secessionComplete")
	public String secessionComplete(UsersForm form, String token, Model model) throws NoSuchAlgorithmException {
		String view = "secession_complete";
		view = checkToken(token, view, model);
		if (view.equals("secession_complete")) {
			if (form.getPassword().equals("")) {
				model.addAttribute("error", "パスワードを入力してください");
				return "user_secession";
			}
			token = model.getAttribute("token").toString();
			DeletedUser deletedUser = userService.secessionUser(form, token);
			if (deletedUser == null) {
				model.addAttribute("error", "パスワードが一致しません");
				return "user_secession";
			}
		}
		return view;
	}

	/**
	 * トークンの有効性チェックと更新
	 * 
	 * @param token
	 * @param view
	 * @param model
	 * @return
	 */
	public String checkToken(String token, String view, Model model) {
		Token result = userService.checkToken(token);
		if (result == null) {
			model.addAttribute("errorMessage", "再度ログインしてください");
			return "todo_login";
		}
		result = userService.updateToken(result);
		model.addAttribute("token", result.getToken());
		return view;
	}
}
