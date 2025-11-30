
package com.example.uni.db;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

public class Database {
    private static DataSource dataSource;

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            JdbcDataSource ds = new JdbcDataSource();
            // Arquivo em ./data/appdb (criado automaticamente)
            ds.setURL("jdbc:h2:file:./data/appdb;AUTO_SERVER=TRUE;DATABASE_TO_UPPER=false");
            ds.setUser("sa");
            ds.setPassword("");
            dataSource = ds;
        }
        return dataSource;
    }

    public static void initSchema() {
        try (Connection conn = getDataSource().getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE IF NOT EXISTS courses (
                  id IDENTITY PRIMARY KEY,
                  name VARCHAR(120) NOT NULL,
                  code VARCHAR(30) UNIQUE NOT NULL,
                  description VARCHAR(400),
                  status VARCHAR(20) NOT NULL,
                  created_at TIMESTAMP NOT NULL,
                  updated_at TIMESTAMP
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS lessons (
                  id IDENTITY PRIMARY KEY,
                  course_id BIGINT NOT NULL,
                  title VARCHAR(160) NOT NULL,
                  duration_minutes INT,
                  content_url VARCHAR(255),
                  scheduled_date DATE,
                  created_at TIMESTAMP NOT NULL,
                  updated_at TIMESTAMP,
                  CONSTRAINT fk_course FOREIGN KEY (course_id)
                    REFERENCES courses(id) ON DELETE CASCADE
                );
            """);
        } catch (Exception e) {
            throw new RuntimeException("Failed to init schema", e);
        }
    }
}
