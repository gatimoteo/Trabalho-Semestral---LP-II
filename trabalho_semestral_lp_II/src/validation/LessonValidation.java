
package com.example.uni.validation;

import com.example.uni.domain.Lesson;

public class LessonValidator implements EntityValidator<Lesson> {
    @Override
    public void validateForCreate(Lesson l) {
        if (l.getCourseId() == null) throw new IllegalArgumentException("courseId é obrigatório");
        if (l.getTitle() == null || l.getTitle().isBlank()) throw new IllegalArgumentException("title é obrigatório");
        if (l.getDurationMinutes() != null && l.getDurationMinutes() < 0) throw new IllegalArgumentException("durationMinutes inválido");
    }
    @Override
    public void validateForUpdate(Lesson l) {
        validateForCreate(l);
    }
}
