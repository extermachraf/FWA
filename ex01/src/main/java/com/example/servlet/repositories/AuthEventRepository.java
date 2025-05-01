package com.example.servlet.repositories;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.servlet.models.AuthEvent;

@Repository
public class AuthEventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<AuthEvent> AUTH_EVENT_MAPPER = (ResultSet rs, int rowNum) -> {
        AuthEvent event = new AuthEvent();
        event.setId(rs.getLong("id"));
        event.setUserId(rs.getLong("user_id"));
        event.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        event.setIpAddress(rs.getString("ip_address"));
        return event;
    };

    @Autowired
    public AuthEventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(AuthEvent authEvent) {
        String sql = "INSERT INTO auth_events (user_id, timestamp, ip_address) "
                + "VALUES (:user_id, :timestamp, :ip_address)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", authEvent.getUserId())
                .addValue("timestamp", authEvent.getTimestamp())
                .addValue("ip_address", authEvent.getIpAddress());

        jdbcTemplate.update(sql, params);
    }

    public List<AuthEvent> findByUserId(Long userId) {
        String sql = "SELECT * FROM auth_events WHERE user_id = :user_id ORDER BY timestamp DESC";

        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);

        return jdbcTemplate.query(sql, params, AUTH_EVENT_MAPPER);
    }
}
