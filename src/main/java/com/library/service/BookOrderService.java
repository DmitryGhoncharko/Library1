package com.library.service;

import com.library.connection.ConnectionPool;
import com.library.dao.BookDao;
import com.library.dao.BookOrderDao;
import com.library.exception.BookDaoException;
import com.library.exception.BookOrderDaoException;
import com.library.exception.ServiceException;
import com.epam.jwd.library.model.*;
import com.library.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookOrderService implements BasicBookOrderService{

    private static final Logger LOG = LogManager.getLogger(BookOrderService.class);

    private final BookOrderDao bookOrderDao;

    private BookOrderService(BookOrderDao bookOrderDao) {
        this.bookOrderDao = bookOrderDao;
    }

    @Override
    public Optional<BookOrder> findById(Long id) throws ServiceException {
        try {
            return bookOrderDao.read(id);
        } catch (BookOrderDaoException e) {
            LOG.error("could not read book order", e);
            throw new ServiceException("could not read book order");
        }
    }

    @Override
    public List<BookOrder> findAll() throws ServiceException {
        try {
            return bookOrderDao.readAll();
        } catch (BookOrderDaoException e) {
            LOG.error("could not read all book orders by id", e);
            throw new ServiceException("could not read all book orders by id");
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        try {
            return bookOrderDao.delete(id);
        } catch (BookOrderDaoException e) {
            LOG.error("could not delete book order", e);
            throw new ServiceException("could not delete book order");
        }
    }

    @Override
    public Optional<BookOrder> createBookOrder(Account account, Long idBook, String orderType) throws ServiceException {
        Optional<BookOrder> createBookOrder = Optional.empty();
        Connection connection = ConnectionPool.lockingPool().takeConnection();
        try {
            connection.setAutoCommit(false);
            BookOrderDao bookOrderDao = BookOrderDao.getInstance();
            BookDao bookDao = BookDao.getInstance();
            Book book = bookDao.read(idBook).orElseThrow(() -> new BookOrderDaoException("could not read book"));
            final AccountDetails details = account.getDetails();
            final OrderType type = OrderType.valueOf(orderType);
            final LocalDate date = LocalDate.now();
            Date sqlDateCreate = Date.valueOf(date);
            BookOrder bookOrder = BookOrder.with()
                    .details(details)
                    .book(book)
                    .type(type)
                    .dateCreate(sqlDateCreate)
                    .dateIssue(null)
                    .dateReturn(null)
                    .status(OrderStatus.CLAIMED)
                    .createWithoutId();
            if (isAccountWithOrderStatusIssue(account.getId())
                    && isRepeatedBookInNoEndedBookOrders(account.getId(), idBook)
                    && bookOrderDao.readByIdAccount(account.getId()).size() > 5) {
                connection.rollback();
                return createBookOrder;
            }
            createBookOrder = bookOrderDao.create(bookOrder);
            connection.commit();
        } catch (BookOrderDaoException | BookDaoException e) {
            LOG.error("could not create book order", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.error("Database access error occurs connection rollback", ex);
            }
            throw new ServiceException("could not create book order");
        } catch (SQLException e) {
            LOG.error("Database access error occurs", e);
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException ex) {
                LOG.error("Database access error occurs connection close", ex);
            }
        }
        return createBookOrder;
    }

    @Override
    public List<BookOrder> findOrdersByIdAccount(Long idAccount) throws ServiceException {
        try {
            return bookOrderDao.readByIdAccount(idAccount);
        } catch (BookOrderDaoException e) {
            LOG.error("could not read book order by id", e);
            throw new ServiceException("could not read book order by id");
        }
    }

    @Override
    public List<BookOrder> findAllUncompleted() throws ServiceException {
        try {
            return bookOrderDao.readAllUncompleted();
        } catch (BookOrderDaoException e) {
            LOG.error("could not read all book orders by id", e);
            throw new ServiceException("could not read all book orders by id");
        }
    }

    @Override
    public boolean changeStatusBookOrderOnIssued(Long idBookOrder) throws ServiceException {
        boolean changedStatusBookOrder = false;
        Connection connection = ConnectionPool.lockingPool().takeConnection();
        try {
            connection.setAutoCommit(false);
            BookOrderDao bookOrderDao = BookOrderDao.getInstance();
            BookDao bookDao = BookDao.getInstance();
            final boolean ChangedStatusOnIssue = bookOrderDao.updateStatusOnIssuedById(idBookOrder);
            final boolean RegisteredDateIssue = isRegisteredDateIssue(idBookOrder, bookOrderDao);
            final BookOrder bookOrder = bookOrderDao.read(idBookOrder).orElseThrow(() -> new BookOrderDaoException("could not get BookOrder"));
            final Book book = bookOrder.getBook();
            final Long id = book.getId();
            final Integer amountOfLeft = book.getAmountOfLeft();
            if (amountOfLeft < 1) {
                throw new BookOrderDaoException("Amount of left < 1 ");
            }
            final boolean DecreasedAmountOfLeftInBook = bookDao.decreaseAmountOfLeft(id,  amountOfLeft - 1);
            if (!ChangedStatusOnIssue || !RegisteredDateIssue || !DecreasedAmountOfLeftInBook) {
                throw new BookOrderDaoException("could not changed status, date issue, amount of left in book");
            }
            bookOrderDao.deleteClaimedFromAccount(bookOrder.getDetails().getId());
            changedStatusBookOrder = true;
            connection.commit();
        } catch (BookOrderDaoException | BookDaoException e) {
            LOG.error("could not create book order", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.error("Database access error occurs connection rollback", ex);
            }
            throw new ServiceException("could not create book order");
        }catch (SQLException e) {
            LOG.error("Database access error connection commit", e);
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                LOG.error("Database access error occurs connection close", e);
            }

        }
        return changedStatusBookOrder;
    }

    private boolean isRegisteredDateIssue(Long idBookOrder, BookOrderDao bookOrderDao) throws ServiceException {
        final LocalDate date = LocalDate.now();
        Date sqlDateIssue = Date.valueOf(date);
        try {
            return bookOrderDao.registerDateOfIssueById(idBookOrder, sqlDateIssue);
        } catch (BookOrderDaoException e) {
            LOG.error("could not check registered date issued", e);
            throw new ServiceException("could not check registered date issued");
        }
    }

    @Override
    public boolean changeStatusBookOrderOnEnded(Long idBookOrder) throws ServiceException {
        Connection connection = ConnectionPool.lockingPool().takeConnection();
        try {
            connection.setAutoCommit(false);
            BookOrderDao bookOrderDao = BookOrderDao.getInstance();
            BookDao bookDao = BookDao.getInstance();
            final boolean isChangedStatusOnEnded = bookOrderDao.updateStatusOnEndedById(idBookOrder);
            final LocalDate date = LocalDate.now();
            Date sqlDateReturn = Date.valueOf(date);
            final boolean isRegisteredDateEnded = bookOrderDao.registerDateOfEndedById(idBookOrder, sqlDateReturn);
            final Book book = bookOrderDao.read(idBookOrder).orElseThrow(() -> new BookOrderDaoException("could not get BookOrder"))
                    .getBook();
            final Long idBook = book.getId();
            final Integer amountOfLeftBook = book.getAmountOfLeft();
            final boolean isIncreasedAmountOfLeftInBook = bookDao.decreaseAmountOfLeft(idBook,  amountOfLeftBook + 1);
            if (!isChangedStatusOnEnded || !isRegisteredDateEnded || !isIncreasedAmountOfLeftInBook) {
                connection.rollback();
                return false;
            }
            connection.commit();
        }  catch (BookOrderDaoException | BookDaoException e) {
            LOG.error("could not change status book order on ended", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.error("Database access error occurs connection rollback", ex);
            }
            throw new ServiceException("could not change status book order on ended");
        }catch (SQLException e) {
            LOG.error("Database access error connection commit", e);
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                LOG.error("Database access error occurs connection close", e);
            }
        }
        return true;
    }

    private boolean isAccountWithOrderStatusIssue(Long idAccount) {
        try {
            if (bookOrderDao.readByAccountWithOrderStatusIssue(idAccount).isPresent()) {
                return true;
            }
        } catch (BookOrderDaoException e) {
            LOG.error("could not check account with order status issued", e);
            return false;
        }
        return false;
    }

    private boolean isRepeatedBookInNoEndedBookOrders(Long idAccount, Long idBook){
        try {
            if (bookOrderDao.readRepeatedBook(idAccount, idBook).isPresent()) {
                return true;
            }
        } catch (BookOrderDaoException e) {
            LOG.error("could not check repeated book", e);
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteClaimedBookOrders(Long id){
        try {
            return bookOrderDao.deleteClaimedFromAccount(id);
        } catch (BookOrderDaoException e) {
            LOG.error("could not delete claimed books", e);
            return false;
        }
    }

    static BookOrderService getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final BookOrderService INSTANCE = new BookOrderService(BookOrderDao.getInstance());
    }
}
