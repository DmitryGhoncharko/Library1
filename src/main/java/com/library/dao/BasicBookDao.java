package com.library.dao;

import com.library.exception.BookDaoException;

import java.util.List;
import java.util.Optional;

public interface BasicBookDao<T> {

    Optional<T> readByTitle(String title) throws BookDaoException;

    boolean createBookInAuthorToBook(Long idBook, Long idAuthor) throws BookDaoException;

    Optional<T> readWithAuthors(Long id) throws BookDaoException;

    List<T> readAllWithAuthors() throws BookDaoException;

    boolean decreaseAmountOfLeft(Long idBook, Integer amountOfLeft) throws BookDaoException;

}
