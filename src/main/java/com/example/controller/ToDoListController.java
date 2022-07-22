package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.TodoList;
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

	/**
	 * やることリストを表示 <br>
	 * トークンがない、またはトークンが有効期限切れの場合はログイン画面を表示する
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String index(String token, Model model) {
		if (token.isEmpty()) {
			return "redirect:/todoList/login";
		}
		try {
			List<TodoList> todoLists = toDoListService.findList(token);
			model.addAttribute("todoList", todoLists);
			return "todoList";
		} catch (NullPointerException e) {
			model.addAttribute("errorMessage", "もう一度ログインしてください。");
			return "redirect:/todoList/login";
		}
	}

	/**
	 * やることリスト登録
	 * 
	 * @return
	 */
	@RequestMapping("/registration")
	public String registration(String token, Model model) {
		if (token.isEmpty()) {
			return "redirect:/todoList/login";
		}

		return "registration_todo";
	}

}
