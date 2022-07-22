package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

	@RequestMapping("/top")
	public String index() {
		return null;
	}

	@RequestMapping("/log")
	public String todoLog() {
		return null;
	}

}
