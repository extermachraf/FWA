package com.example.servlet.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.servlet.models.User;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        try {
            return jdbcTemplate.queryForObject(sql,
                    new MapSqlParameterSource("email", email),
                    BeanPropertyRowMapper.newInstance(User.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = :email";
        Integer count = jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource("email", email), Integer.class);
        return count != null && count > 0;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (first_name, last_name, phone_number, email, password) "
                + "VALUES (:first_name, :last_name, :phone_number, :email, :password)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("first_name", user.getFirst_name())
                .addValue("last_name", user.getLast_name())
                .addValue("phone_number", user.getPhone_number())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword());

        jdbcTemplate.update(sql, params);
    }
}
