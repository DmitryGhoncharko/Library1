package com.library.dao;

import com.library.exception.BookOrderDaoException;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface BasicBookOrderDao<T> {

    List<T> readAllUncompleted() throws BookOrderDaoException;

    Optional<T> readRepeatedBook(Long idAccount, Long idBook) throws BookOrderDaoException;

    List<T> readByIdAccount(Long idAccount) throws BookOrderDaoException;

    Optional<T> readByAccountWithOrderStatusIssue(Long idAccount) throws BookOrderDaoException;

    boolean updateStatusOnIssuedById(Long idBookOrder) throws BookOrderDaoException;

    boolean updateStatusOnEndedById(Long idBookOrder) throws BookOrderDaoException;

    boolean registerDateOfIssueById(Long idBookOrder, Date dateIssue) throws BookOrderDaoException;

    boolean registerDateOfEndedById(Long idBookOrder, Date dateReturn) throws BookOrderDaoException;

    boolean deleteClaimedFromAccount(Long idAccount) throws BookOrderDaoException;

}
