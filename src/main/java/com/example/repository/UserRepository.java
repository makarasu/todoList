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

	private static final RowMapper<Users> SECRET_QUESTIONS_ROW_MAPPER = (rs, i) -> {
		Users users = new Users();
		users.setEmail(rs.getString("email"));
		users.setSecretQuestion1(rs.getString("secret_question1"));
		users.setSecretQuestion2(rs.getString("secret_question2"));
		users.setSecretQuestion3(rs.getString("secret_question3"));
		return users;
	};

	private static final RowMapper<Users> SECRET_ANSWERS_ROW_MAPPER = (rs, i) -> {
		Users users = new Users();
		users.setEmail(rs.getString("email"));
		users.setSecretAnswer1(rs.getString("secret_answer1"));
		users.setSecretAnswer2(rs.getString("secret_answer2"));
		users.setSecretAnswer3(rs.getString("secret_answer3"));
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
	public Users findByEmailAndPassword(UsersForm form) {
		String sql = "SELECT id ,name ,email ,password FROM users WHERE email=:email AND password=:password ;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", form.getEmail()).addValue("password",
				form.getPassword());
		List<Users> user = template.query(sql, param, USERS_ROW_MAPPER);
		if (user.size() == 0) {
			return null;
		}
		return user.get(0);
	}

	/**
	 * パスワード再発行時の秘密の質問取得
	 * 
	 * @param email
	 * @return
	 */
	public Users findByEmailForQuestion(UsersForm form) {
		String sql = "SELECT email ,secret_question1 ,secret_question2 ,secret_question3 FROM users WHERE email=:email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", form.getEmail());
		List<Users> users = template.query(sql, param, SECRET_QUESTIONS_ROW_MAPPER);
		return users.get(0);
	}

	/**
	 * 秘密の質問と回答の照合結果取得
	 * 
	 * @param form
	 * @return
	 */
	public Users findBySecretQuestions(UsersForm form) {
		String sql = "SELECT email ,secret_answer1 ,secret_answer2 ,secret_answer3 FROM users WHERE email=:email AND"
				+ " secret_question1=:secretQuestion1 AND secret_question2=:secretQuestion2 AND secret_question3=:secretQuestion3"
				+ " AND secret_answer1=:secretAnswer1 AND secret_answer2=:secretAnswer2 AND secret_answer3=:secretAnswer3 ;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", form.getEmail())
				.addValue("secretQuestion1", form.getSecretQuestion1())
				.addValue("secretQuestion2", form.getSecretQuestion2())
				.addValue("secretQuestion3", form.getSecretQuestion3())
				.addValue("secretAnswer1", form.getSecretAnswer1()).addValue("secretAnswer2", form.getSecretAnswer2())
				.addValue("secretAnswer3", form.getSecretAnswer3());
		List<Users> users = template.query(sql, param, SECRET_ANSWERS_ROW_MAPPER);
		if (users.size() == 0) {
			return null;
		}
		return users.get(0);
	}

	/**
	 * パスワードの更新
	 * 
	 * @param form
	 */
	public void updatePassword(UsersForm form) {
		String sql = "UPDATE users SET password=:password WHERE email=:email ;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("password", form.getPassword())
				.addValue("email", form.getEmail());
		template.update(sql, param);
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
