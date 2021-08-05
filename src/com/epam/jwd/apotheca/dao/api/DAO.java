package com.epam.jwd.apotheca.dao.api;

import java.util.List;

import com.epam.jwd.apotheca.model.Entity;

public interface DAO<T extends Entity> {

    T save(T entity);

    List<T> findAll();
//
//    List<T> findAllById(Integer id);

    T update(T entity);

    boolean delete(Integer id);

}
