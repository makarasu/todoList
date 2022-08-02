package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Token;
import com.example.service.UserService;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

	@Autowired
	UserService userService;

	@RequestMapping("/top")
	public String index(String token, Model model) {
		Token result = userService.checkToken(token);
		if (result == null) {
			model.addAttribute("errorMessage", "再度ログインしてください");
			return "/user/login";
		}
		result = userService.updateToken(result);
		model.addAttribute("token", result.getToken());
		return "mypage_top";
	}

	@RequestMapping("/log")
	public String todoLog() {
		return null;
	}

	@RequestMapping("/registrateTodo")
	public String regiTodo() {
		return null;
	}

	@RequestMapping("/changePassword")
	public String changePassword() {
		return null;
	}

	@RequestMapping("/secession")
	public String secession() {
		return null;
	}
}
