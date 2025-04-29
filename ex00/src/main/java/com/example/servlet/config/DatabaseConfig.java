package com.example.servlet.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfig {

    private final Environment env;

    public DatabaseConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getProperty("db.url"));
        config.setUsername(env.getProperty("db.username"));
        config.setPassword(env.getProperty("db.password"));
        config.setDriverClassName(env.getProperty("db.driver"));

        config.setMaximumPoolSize(env.getProperty("db.pool.maxSize", Integer.class, 10));
        config.setMinimumIdle(env.getProperty("db.pool.minIdle", Integer.class, 2));
        config.setIdleTimeout(env.getProperty("db.pool.idleTimeout", Long.class, 300000L));
        config.setConnectionTimeout(env.getProperty("db.pool.connectionTimeout", Long.class, 20000L));

        return new HikariDataSource(config);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
