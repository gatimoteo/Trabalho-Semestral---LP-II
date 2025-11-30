
package com.example.uni.controller;

import com.example.uni.domain.Lesson;
import com.example.uni.http.HttpUtil;
import com.example.uni.service.LessonService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LessonController {
    private final LessonService service = new LessonService();

    public void list(HttpExchange ex, Map<String, String> p) throws IOException {
        HttpUtil.sendJson(ex, 200, service.list());
    }

    public void get(HttpExchange ex, Map<String, String> p) throws IOException {
        Long id = Long.valueOf(p.get("id"));
        Lesson l = service.get(id);
        HttpUtil.sendJson(ex, 200, l);
    }

    public void create(HttpExchange ex, Map<String, String> p) throws IOException {
        Lesson l = HttpUtil.readJson(ex, Lesson.class);
        HttpUtil.sendJson(ex, 201, service.create(l));
    }

    public void update(HttpExchange ex, Map<String, String> p) throws IOException {
        Long id = Long.valueOf(p.get("id"));
        Lesson l = HttpUtil.readJson(ex, Lesson.class);
        HttpUtil.sendJson(ex, 200, service.update(id, l));
    }

    public void delete(HttpExchange ex, Map<String, String> p) throws IOException {
        Long id = Long.valueOf(p.get("id"));
        boolean ok = service.delete(id);
        HttpUtil.sendText(ex, ok ? 204 : 404, ok ? "" : "Lesson não encontrada");
    }

    public void listByCourse(HttpExchange ex, Map<String, String> p) throws IOException {
        Long courseId = Long.valueOf(p.get("courseId"));
        List<Lesson> list = service.listByCourse(courseId);
        HttpUtil.sendJson(ex, 200, list);
    }

    public void createUnderCourse(HttpExchange ex, Map<String, String> p) throws IOException {
        Long courseId = Long.valueOf(p.get("courseId"));
        Lesson l = HttpUtil.readJson(ex, Lesson.class);
        l.setCourseId(courseId);
        HttpUtil.sendJson(ex, 201, service.create(l));
    }
}
