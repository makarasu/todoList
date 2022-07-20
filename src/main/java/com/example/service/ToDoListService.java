package com.example.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.TodoList;
import com.example.domain.Users;
import com.example.repository.ToDoListRepository;

@Service
public class ToDoListService {
	
	@Autowired
	ToDoListRepository toDoListRepository;
	
	public List<TodoList> findList(String token){
		//現在時刻の取得
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//現在時刻から30分前の日時を取得
		calendar.add(Calendar.MINUTE, -30);
		String dateString = String.valueOf(calendar);
		
		//受け取ったトークンが有効か（最終更新から30分が経過していないか）チェックする
		String checkToken = toDoListRepository.findValidToken(dateString);
		
		if(!(checkToken.isEmpty())) {
			Users users = toDoListRepository.findByToken(checkToken);
			List<TodoList> todoLists = toDoListRepository.findTodoList(users.getId());
			return todoLists;
		}
		throw new NullPointerException();
	}

}
