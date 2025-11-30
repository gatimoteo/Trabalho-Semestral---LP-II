
package com.example.uni.service;

import com.example.uni.domain.Course;
import com.example.uni.repository.CourseRepository;
import com.example.uni.validation.CourseValidator;

import java.util.List;

public class CourseService {
    private final CourseRepository repo = new CourseRepository();
    private final CourseValidator validator = new CourseValidator();

    public Course create(Course c) {
        validator.validateForCreate(c);
        return repo.insert(c);
    }

    public Course update(Long id, Course c) {
        validator.validateForUpdate(c);
        return repo.update(id, c);
    }

    public List<Course> list() { return repo.findAll(); }

    public Course get(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Course not found " + id));
    }

    public boolean delete(Long id) { return repo.delete(id); }
}
