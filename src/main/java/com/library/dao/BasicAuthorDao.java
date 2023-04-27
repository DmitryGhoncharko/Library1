package com.library.dao;

import com.library.exception.AuthorDaoException;

import java.util.Optional;

public interface BasicAuthorDao<T> {

    Optional<T> readAuthorByFirstLastName(T t) throws AuthorDaoException;

}
