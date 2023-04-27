package com.library.service;

import com.library.exception.ServiceException;
import com.epam.jwd.library.model.*;
import com.library.model.Account;
import com.library.model.BookOrder;

import java.util.List;
import java.util.Optional;

public interface BasicBookOrderService extends Service<BookOrder> {

    Optional<BookOrder> createBookOrder(Account account, Long idBook, String orderType) throws ServiceException;

    List<BookOrder> findOrdersByIdAccount(Long idAccount) throws ServiceException;

    List<BookOrder> findAllUncompleted() throws ServiceException;

    boolean changeStatusBookOrderOnIssued(Long id) throws ServiceException;

    boolean changeStatusBookOrderOnEnded(Long id) throws ServiceException;

    boolean deleteClaimedBookOrders(Long id) throws ServiceException;

    static BasicBookOrderService getInstance() {
        return BookOrderService.getInstance();
    }
}
