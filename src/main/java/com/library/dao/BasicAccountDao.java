package com.library.dao;

import com.library.exception.AccountDaoException;

import java.util.Optional;

public interface BasicAccountDao<T> {

    Optional<T> readByLogin(String login) throws AccountDaoException;

    boolean updateRole(Long id, Integer idRole) throws AccountDaoException;

}
