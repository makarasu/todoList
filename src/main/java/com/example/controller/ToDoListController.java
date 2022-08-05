package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.TodoList;
import com.example.form.TodoListForm;
import com.example.form.UsersForm;
import com.example.service.ToDoListService;

/**
 * to-doリスト表示に関わるコントローラー
 * 
 * @author makarasu
 *
 */
@Controller
@RequestMapping("/todoList")
public class ToDoListController {

	@Autowired
	ToDoListService toDoListService;

	@Autowired
	MyPageController myPageController;

	@ModelAttribute
	public UsersForm usersForm() {
		return new UsersForm();
	}

	@ModelAttribute
	public TodoListForm todoListForm() {
		return new TodoListForm();
	}

	/**
	 * やることリストを表示 <br>
	 * トークンがない、またはトークンが有効期限切れの場合はログイン画面を表示する
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String index(String token, Model model) {
		String view = "todoList";
		view = myPageController.checkToken(token, view, model);
		if (view.equals("todoList")) {
			token = model.getAttribute("token").toString();
			Boolean enforcement = false;
			List<TodoList> todoLists = toDoListService.findList(token, enforcement);
			model.addAttribute("todoList", todoLists);
		}
		return view;
	}
}
