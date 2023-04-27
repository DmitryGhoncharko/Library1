package com.library.service;

import com.library.exception.ServiceException;
import com.library.model.Entity;

import java.util.List;
import java.util.Optional;

public interface Service<T extends Entity> {

    Optional<T> findById(Long id) throws ServiceException;

    List<T> findAll() throws ServiceException;

    boolean delete(Long id) throws ServiceException;

}
