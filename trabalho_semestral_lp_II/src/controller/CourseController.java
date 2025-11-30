
package com.example.uni.controller;

import com.example.uni.domain.Course;
import com.example.uni.domain.CourseStatus;
import com.example.uni.http.HttpUtil;
import com.example.uni.service.CourseService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CourseController {
    private final CourseService service = new CourseService();

    public void list(HttpExchange ex, Map<String, String> p) throws IOException {
        List<Course> courses = service.list();
        HttpUtil.sendJson(ex, 200, courses);
    }

    public void get(HttpExchange ex, Map<String, String> p) throws IOException {
        Long id = Long.valueOf(p.get("id"));
        Course c = service.get(id);
        HttpUtil.sendJson(ex, 200, c);
    }

    public void create(HttpExchange ex, Map<String, String> p) throws IOException {
        Course c = HttpUtil.readJson(ex, Course.class);
        // Converte status string para enum (se vier em minúsculo)
        if (c.getStatus() == null && c instanceof Course) {
            // nada aqui, Jackson deve mapear se vier "ACTIVE"/"INACTIVE"
        }
        HttpUtil.sendJson(ex, 201, service.create(c));
    }

    public void update(HttpExchange ex, Map<String, String> p) throws IOException {
        Long id = Long.valueOf(p.get("id"));
        Course c = HttpUtil.readJson(ex, Course.class);
        HttpUtil.sendJson(ex, 200, service.update(id, c));
    }

    public void delete(HttpExchange ex, Map<String, String> p) throws IOException {
        Long id = Long.valueOf(p.get("id"));
        boolean ok = service.delete(id);
        HttpUtil.sendText(ex, ok ? 204 : 404, ok ? "" : "Course não encontrado");
    }
}
