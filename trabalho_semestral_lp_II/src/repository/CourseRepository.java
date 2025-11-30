
package com.example.uni.repository;

import com.example.uni.db.Database;
import com.example.uni.domain.Course;
import com.example.uni.domain.CourseStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepository implements Repository<Course> {

    @Override
    public Course insert(Course c) {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO courses (name, code, description, status, created_at)
                    VALUES (?, ?, ?, ?, ?)
                """, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getCode());
            ps.setString(3, c.getDescription());
            ps.setString(4, c.getStatus().name());
            c.setCreatedAt(LocalDateTime.now());
            ps.setTimestamp(5, Timestamp.valueOf(c.getCreatedAt()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getLong(1));
            }
            return c;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Course> findById(Long id) {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM courses WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> findAll() {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM courses ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            List<Course> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course update(Long id, Course c) {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                 UPDATE courses SET name=?, code=?, description=?, status=?, updated_at=?
                 WHERE id=?
             """)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getCode());
            ps.setString(3, c.getDescription());
            ps.setString(4, c.getStatus().name());
            c.setUpdatedAt(LocalDateTime.now());
            ps.setTimestamp(5, Timestamp.valueOf(c.getUpdatedAt()));
            ps.setLong(6, id);
            int changed = ps.executeUpdate();
            if (changed == 0) throw new RuntimeException("Course not found id=" + id);
            c.setId(id);
            return c;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM courses WHERE id=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Course map(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setId(rs.getLong("id"));
        c.setName(rs.getString("name"));
        c.setCode(rs.getString("code"));
        c.setDescription(rs.getString("description"));
        c.setStatus(CourseStatus.valueOf(rs.getString("status")));
        Timestamp created = rs.getTimestamp("created_at");
        Timestamp updated = rs.getTimestamp("updated_at");
        c.setCreatedAt(created != null ? created.toLocalDateTime() : null);
        c.setUpdatedAt(updated != null ? updated.toLocalDateTime() : null);
        return c;
    }
}
