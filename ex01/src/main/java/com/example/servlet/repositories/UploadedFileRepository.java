package com.example.servlet.repositories;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.servlet.models.UploadedFile;

@Repository
public class UploadedFileRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<UploadedFile> FILE_MAPPER = (ResultSet rs, int rowNum) -> {
        UploadedFile file = new UploadedFile();
        file.setId(rs.getLong("id"));
        file.setUserId(rs.getLong("user_id"));
        file.setOriginalFilename(rs.getString("original_filename"));
        file.setStoredFilename(rs.getString("stored_filename"));
        file.setMimeType(rs.getString("mime_type"));
        file.setFileSize(rs.getLong("file_size"));
        file.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
        return file;
    };

    @Autowired
    public UploadedFileRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UploadedFile save(UploadedFile file) {
        String sql = "INSERT INTO uploaded_files (user_id, original_filename, stored_filename, mime_type, file_size, upload_date) "
                + "VALUES (:user_id, :original_filename, :stored_filename, :mime_type, :file_size, :upload_date) "
                + "RETURNING id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", file.getUserId())
                .addValue("original_filename", file.getOriginalFilename())
                .addValue("stored_filename", file.getStoredFilename())
                .addValue("mime_type", file.getMimeType())
                .addValue("file_size", file.getFileSize())
                .addValue("upload_date", file.getUploadDate());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);

        file.setId((Long) keyHolder.getKeys().get("id"));
        return file;
    }

    public List<UploadedFile> findByUserId(Long userId) {
        String sql = "SELECT * FROM uploaded_files WHERE user_id = :user_id ORDER BY upload_date DESC";
        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        return jdbcTemplate.query(sql, params, FILE_MAPPER);
    }

    public Optional<UploadedFile> findByStoredFilename(String storedFilename) {
        String sql = "SELECT * FROM uploaded_files WHERE stored_filename = :stored_filename";
        MapSqlParameterSource params = new MapSqlParameterSource("stored_filename", storedFilename);

        List<UploadedFile> files = jdbcTemplate.query(sql, params, FILE_MAPPER);
        return files.isEmpty() ? Optional.empty() : Optional.of(files.get(0));
    }
}
