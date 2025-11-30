
package com.example.uni.validation;

public interface EntityValidator<T> {
    void validateForCreate(T entity);
    void validateForUpdate(T entity);
}
