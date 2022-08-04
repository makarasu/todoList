package com.example.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Token;
import com.example.form.TodoListForm;
import com.example.service.ToDoListService;
import com.example.service.UserService;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

	@Autowired
	UserService userService;

	@Autowired
	ToDoListService toDoListService;

	@ModelAttribute
	public TodoListForm todoListForm() {
		return new TodoListForm();
	}

	@RequestMapping("/top")
	public String index(String token, Model model) {
		String view = "myPage_top";
		view = checkToken(token, view, model);
		return view;
	}

	@RequestMapping("/log")
	public String todoLog() {
		return null;
	}

	@RequestMapping("/registrateTodo")
	public String regiTodo(String token, Model model) {
		String view = "todo_insert";
		view = checkToken(token, view, model);
		return view;
	}

	@RequestMapping("/insertTodo")
	public String insertTodo(TodoListForm form, String token, Model model) throws ParseException {
		String view = "todoList";
		view = checkToken(token, view, model);
		System.out.println("term:" + form.getTerm());
		token = (String) model.getAttribute("token");
		toDoListService.insertTodo(form, token);
		return view;
	}

	@RequestMapping("/changePassword")
	public String changePassword() {
		return null;
	}

	@RequestMapping("/secession")
	public String secession() {
		return null;
	}

	public String checkToken(String token, String view, Model model) {
		Token result = userService.checkToken(token);
		if (result == null) {
			model.addAttribute("errorMessage", "再度ログインしてください");
			return "/user/login";
		}
		result = userService.updateToken(result);
		model.addAttribute("token", result.getToken());
		return view;
	}
}
