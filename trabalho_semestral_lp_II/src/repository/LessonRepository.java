
package com.example.uni.repository;

import com.example.uni.db.Database;
import com.example.uni.domain.Lesson;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class LessonRepository implements Repository<Lesson> {

    @Override
    public Lesson insert(Lesson l) {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO lessons (course_id, title, duration_minutes, content_url, scheduled_date, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
             """, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, l.getCourseId());
            ps.setString(2, l.getTitle());
            ps.setObject(3, l.getDurationMinutes(), java.sql.Types.INTEGER);
            ps.setString(4, l.getContentUrl());
            if (l.getScheduledDate() != null)
                ps.setDate(5, Date.valueOf(l.getScheduledDate()));
            else ps.setNull(5, Types.DATE);
            l.setCreatedAt(LocalDateTime.now());
            ps.setTimestamp(6, Timestamp.valueOf(l.getCreatedAt()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) l.setId(rs.getLong(1));
            }
            return l;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Lesson> findById(Long id) {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM lessons WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Lesson> findByCourseId(Long courseId) {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM lessons WHERE course_id=? ORDER BY id")) {
            ps.setLong(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Lesson> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Lesson> findAll() {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM lessons ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            List<Lesson> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Lesson update(Long id, Lesson l) {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                UPDATE lessons SET course_id=?, title=?, duration_minutes=?, content_url=?, scheduled_date=?, updated_at=?
                WHERE id=?
             """)) {
            ps.setLong(1, l.getCourseId());
            ps.setString(2, l.getTitle());
            ps.setObject(3, l.getDurationMinutes(), java.sql.Types.INTEGER);
            ps.setString(4, l.getContentUrl());
            if (l.getScheduledDate() != null)
                ps.setDate(5, Date.valueOf(l.getScheduledDate()));
            else ps.setNull(5, Types.DATE);
            l.setUpdatedAt(LocalDateTime.now());
            ps.setTimestamp(6, Timestamp.valueOf(l.getUpdatedAt()));
            ps.setLong(7, id);
            int changed = ps.executeUpdate();
            if (changed == 0) throw new RuntimeException("Lesson not found id=" + id);
            l.setId(id);
            return l;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection conn = Database.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM lessons WHERE id=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Lesson map(ResultSet rs) throws SQLException {
        Lesson l = new Lesson();
        l.setId(rs.getLong("id"));
        l.setCourseId(rs.getLong("course_id"));
        l.setTitle(rs.getString("title"));
        int dur = rs.getInt("duration_minutes");
        l.setDurationMinutes(rs.wasNull() ? null : dur);
        l.setContentUrl(rs.getString("content_url"));
        Date sd = rs.getDate("scheduled_date");
        l.setScheduledDate(sd != null ? sd.toLocalDate() : null);
        Timestamp created = rs.getTimestamp("created_at");
        Timestamp updated = rs.getTimestamp("updated_at");
        l.setCreatedAt(created != null ? created.toLocalDateTime() : null);
        l.setUpdatedAt(updated != null ? updated.toLocalDateTime() : null);
        return l;
    }
}
