package com.library.service;

import com.library.exception.ServiceException;
import com.library.model.Author;

import java.util.Optional;

public interface BasicAuthorService extends Service<Author> {

    Optional<Author> create(String firstName, String lastName) throws ServiceException;

    Optional<Author> update(Long id, String firstName, String lastName) throws ServiceException;

    static BasicAuthorService getInstance() {
        return AuthorService.getInstance();
    }
}
