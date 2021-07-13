package com.epam.jwd.apotheca.dao.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.epam.jwd.apotheca.model.Entity;
import com.epam.jwd.apotheca.model.Order;

public interface DAO<T extends Entity> {

    T save(T entity);

    List<T> findAll();

    List<T> findAllById(Integer id);

    T update(T entity);

    boolean delete(Integer id);

}
