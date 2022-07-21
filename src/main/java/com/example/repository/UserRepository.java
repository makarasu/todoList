package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Token;
import com.example.domain.Users;
import com.example.form.UsersForm;

@Repository
public class UserRepository {

	private static final RowMapper<Users> USERS_ROW_MAPPER = (rs, i) -> {
		Users users = new Users();
		users.setId(rs.getInt("id"));
		users.setName(rs.getString("name"));
		users.setEmail(rs.getString("email"));
		users.setPassword(rs.getString("password"));
		return users;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * 同一メールアドレスが登録されていないか確認
	 * 
	 * @param users
	 * @return
	 */
	public Users findByEmail(UsersForm form) {
		String sql = "SELECT id ,name ,email ,password FROM users WHERE email=:email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", form.getEmail());
		List<Users> user = template.query(sql, param, USERS_ROW_MAPPER);
		if (user.size() == 0) {
			return null;
		}
		return user.get(0);
	}

	/**
	 * ユーザー登録
	 * 
	 * @param users
	 */
	public boolean insertUser(UsersForm form) {
		String insertSql = "INSERT INTO users (name ,email ,password"
				+ " ,secret_question1 ,secret_answer1 ,secret_question2 ,secret_answer2 ,secret_question3 ,secret_answer3)"
				+ " VALUES (:name ,:email ,:password ,:secretQuestion1 ,:secretAnswer1 ,:secretQuestion2 ,:secretAnswer2"
				+ " ,:secretQuestion3 ,:secretAnswer3)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(form);
		try {
			template.update(insertSql, param);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ログイン情報取得
	 * 
	 * @param users
	 * @return
	 */
	public Users findByEmailAndPassword(Users users) {
		String sql = "SELECT id ,name ,email ,password FROM users WHERE email=:email";
		SqlParameterSource param = new MapSqlParameterSource().addValue(sql, users.getEmail());
		Users user = template.queryForObject(sql, param, USERS_ROW_MAPPER);
		return user;
	}

	/**
	 * トークンの追加
	 * 
	 * @param token
	 */
	public void insertToken(Token token) {
		String sql = "INSERT INTO token (token ,generate_date ,update_date ,user_id)"
				+ " VALUES (:token ,:generate_date ,:update_date)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(token);
		template.update(sql, param);
	}

	/**
	 * トークンの更新
	 * 
	 * @param token
	 * @param newToken
	 */
	public void updateToken(Token token, String newToken) {
		String sql = "UPDATE token SET token=:newToken ,update_date=:update_date WHERE token=:token";
		SqlParameterSource param = new MapSqlParameterSource().addValue("newToken", newToken).addValue("token",
				token.getToken());
		template.update(sql, param);
	}
}
