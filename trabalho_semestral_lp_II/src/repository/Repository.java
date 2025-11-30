
package com.example.uni.repository;

import com.example.uni.domain.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface Repository<T extends BaseEntity> {
    T insert(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    T update(Long id, T entity);
    boolean delete(Long id);
}
