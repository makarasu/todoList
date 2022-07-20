package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.TodoList;
import com.example.domain.Users;

@Repository
public class ToDoListRepository {
	
	private static final RowMapper<TodoList> TODO_LIST_ROW_MAPPER = (rs, i) -> {
		TodoList todoList = new TodoList();
		todoList.setId(rs.getInt("id"));
		todoList.setImportance(rs.getInt("importance"));
		todoList.setTodo(rs.getString("todo"));
		todoList.setTerm(rs.getString("term"));
		todoList.setCategory(rs.getString("category"));
		todoList.setMemo(rs.getString("memo"));
		todoList.setEnforcement(rs.getBoolean("enforcement"));
		todoList.setUserId(rs.getInt("user_id"));
		return todoList;
	};
	private static final RowMapper<Users> USERS_ROW_MAPPER = (rs, i) ->{
		Users users = new Users();
		users.setId(rs.getInt("id"));
		users.setEmail(rs.getString("email"));
		users.setPassword(rs.getString("password"));
		return users;
	};
	
	private static final RowMapper<String> TOKEN_ROW_MAPPER = (rs, i) ->{
		String token = rs.getString("token");
		return token;
	};
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public String findValidToken(String checkTime) {
		String sql = "SELECT token WHERE update_date>:checkTime;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("checkTime", checkTime);
		String token = template.queryForObject(sql, param, TOKEN_ROW_MAPPER);
		return token;
	}
	
	public Users findByToken(String token) {
		String sql = "SELECT u.id ,u.email ,u.password FROM users AS u JOIN token AS t ON u.id=t.user_id"
				+ " WHERE t.token=:token";
		SqlParameterSource param = new MapSqlParameterSource().addValue("token", token);
		Users users = template.queryForObject(sql, param, USERS_ROW_MAPPER);
		return users;
	}
	
	public List<TodoList> findTodoList(Integer userId){
		String sql = "SELECT id ,importance ,todo ,term ,category ,memo ,enforcement ,user_id"
				+ " FROM todo_list WHERE user_id=:userId AND enforcement=true";
	
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<TodoList> todoList = template.query(sql, param, TODO_LIST_ROW_MAPPER);
		return todoList;
	}

}
