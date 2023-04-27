package com.library.service;

import com.library.exception.ServiceException;
import com.library.model.Account;

import java.util.List;
import java.util.Optional;

public interface BasicAccountService extends Service<Account>{

    Optional<Account> create(String login, String password, String firstName, String LastName) throws ServiceException;

    List<Account> findAll() throws ServiceException;

    Optional<Account> authenticate(String login, String password) throws ServiceException;

    boolean delete(Long id) throws ServiceException;

    boolean changeRole(Long id, String name) throws ServiceException;

    static BasicAccountService getInstance() {
        return AccountService.getInstance();
    }

}
