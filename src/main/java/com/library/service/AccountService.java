package com.library.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.library.connection.ConnectionPool;
import com.library.dao.AccountDao;
import com.library.dao.AccountDetailsDao;
import com.library.exception.AccountDaoException;
import com.library.exception.ServiceException;
import com.library.model.Account;
import com.library.model.AccountDetails;
import com.library.model.Role;
import com.library.validation.AccountValidator;
import com.library.validation.FirstLastNameValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountService implements BasicAccountService {

    private static final Logger LOG = LogManager.getLogger(AccountService.class);

    private static final char[] PASSWORD_FOR_FUN = "bcrypt".toCharArray();
    private static final int MIN_COST_FOR_HASHING = 4;
    private static final int NUMBER_CORRECT_CHANGE_ROLE = 1;

    private final AccountDao accountDao;

    private AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Optional<Account> create(String login, String password, String firstName, String lastName) throws ServiceException {
        Optional<Account> createAccountWithDetails;
        Connection connection = ConnectionPool.lockingPool().takeConnection();
        try{
            checkAccountData(login, password, firstName, lastName);
            final Account account = createAccountWithHashedPassword(login, password);
            connection.setAutoCommit(false);
            AccountDao accountDao = AccountDao.getInstance();
            AccountDetailsDao accountDetailsDao = AccountDetailsDao.getInstance();
            final Account createdAccount = accountDao.create(account).orElseThrow(() -> new AccountDaoException("could not create account"));
            final Long idAccount = createdAccount.getId();
            accountDetailsDao.create(new AccountDetails(idAccount, firstName, lastName));
            createAccountWithDetails = accountDao.readByLogin(login);
            if (createAccountWithDetails.isPresent()) {
                connection.commit();
                return createAccountWithDetails;
            } else {
                connection.rollback();
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOG.error("sql error, Database access error connection commit", e);
            throw new ServiceException("could not create new account");
        } catch (AccountDaoException e) {
            LOG.error("Account dao error, could not create new account", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.error("Database access error occurs connection rollback", ex);
            }
            throw new ServiceException("could not create new account");
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                LOG.error("Database access error occurs connection close", e);
            }
        }
    }

    private Account createAccountWithHashedPassword(String login, String password) {
        final String hashedPassword = BCrypt.withDefaults().hashToString(MIN_COST_FOR_HASHING, password.toCharArray());
        return new Account(login, hashedPassword);
    }

    private void checkAccountData(String login, String password, String firstName, String lastName) throws ServiceException {
        if (!AccountValidator.getInstance().validate(login, password)
                || !FirstLastNameValidator.getInstance().validate(firstName, lastName)) {
            throw new ServiceException("Account data are not valid");
        }
    }

    @Override
    public Optional<Account> findById(Long id) throws ServiceException {
        try {
            return accountDao.read(id);
        } catch (AccountDaoException e) {
            LOG.error("Dao error, could not read account", e);
            throw new ServiceException("could not find account");
        }
    }

    @Override
    public List<Account> findAll() throws ServiceException {
        try {
            return accountDao.readAll();
        } catch (AccountDaoException e) {
            LOG.error("Dao error, could not read all accounts", e);
            throw new ServiceException("could not read all accounts");
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        try {
            return accountDao.delete(id);
        } catch (AccountDaoException e) {
            LOG.error("Dao error, could not delete account", e);
            throw new ServiceException("could not delete account");
        }
    }

    @Override
    public Optional<Account> authenticate(String login, String password) throws ServiceException {
        if (!AccountValidator.getInstance().validate(login, password)) {
            return Optional.empty();
        }
        final Optional<Account> readAccount;
        try {
            readAccount = accountDao.readByLogin(login);
            if (readAccount.isPresent()) {
                return readAccount.filter(
                        account -> BCrypt.verifyer()
                                .verify(password.toCharArray(), account.getPassword()
                                        .toCharArray()).verified
                );
            } else {
                protectFromTimingAttack(password);
                return Optional.empty();
            }
        } catch (AccountDaoException e) {
            LOG.error("Dao error, could not read by login");
            throw new ServiceException("could not read by login]");
        }
    }

    @Override
    public boolean changeRole(Long id, String name) throws ServiceException {
        try {
            if (accountDao.read(id).isPresent()) {
                final Integer idRole = Role.valueOf(name).ordinal();
                if (accountDao.updateRole(id, idRole + NUMBER_CORRECT_CHANGE_ROLE)) {
                    return true;
                }
            }
        }catch (AccountDaoException e) {
            LOG.error("Dao error, could not change role");
            throw new ServiceException("could not change role");
        }
        return false;
    }

    private void protectFromTimingAttack(String password) {
        BCrypt.verifyer().verify(password.toCharArray(), PASSWORD_FOR_FUN);
    }

    static AccountService getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final AccountService INSTANCE = new AccountService(AccountDao.getInstance());
    }
}
