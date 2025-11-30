
package com.example.uni.validation;

import com.example.uni.domain.Course;

public class CourseValidator implements EntityValidator<Course> {
    @Override
    public void validateForCreate(Course c) {
        if (c.getName() == null || c.getName().isBlank()) throw new IllegalArgumentException("name é obrigatório");
        if (c.getCode() == null || c.getCode().isBlank()) throw new IllegalArgumentException("code é obrigatório");
        if (c.getStatus() == null) throw new IllegalArgumentException("status é obrigatório");
    }
    @Override
    public void validateForUpdate(Course c) {
        validateForCreate(c);
    }
}
