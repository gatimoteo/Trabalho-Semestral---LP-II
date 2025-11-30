
package com.example.uni.service;

import com.example.uni.domain.Lesson;
import com.example.uni.repository.LessonRepository;
import com.example.uni.validation.LessonValidator;

import java.util.List;

public class LessonService {
    private final LessonRepository repo = new LessonRepository();
    private final LessonValidator validator = new LessonValidator();

    public Lesson create(Lesson l) {
        validator.validateForCreate(l);
        return repo.insert(l);
    }

    public Lesson update(Long id, Lesson l) {
        validator.validateForUpdate(l);
        return repo.update(id, l);
    }

    public List<Lesson> list() { return repo.findAll(); }

    public Lesson get(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found " + id));
    }

    public boolean delete(Long id) { return repo.delete(id); }

    public List<Lesson> listByCourse(Long courseId) { return repo.findByCourseId(courseId); }
}
