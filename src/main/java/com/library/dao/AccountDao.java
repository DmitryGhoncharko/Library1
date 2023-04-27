package com.library.dao;

import com.library.connection.ConnectionPool;
import com.library.exception.AccountDaoException;
import com.library.model.Account;
import com.library.model.AccountDetails;
import com.library.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDao extends AbstractDao<Account> implements BasicAccountDao<Account> {

    private static final Logger LOG = LogManager.getLogger(AccountDao.class);

    private static final String INSERT_NEW_ACCOUNT = "insert into l_account (a_login, a_password) values (?,?)";

    private static final String UPDATE_ACCOUNT_ROLE = "update l_account set a_role_id=? where a_id=?";

    private static final String SELECT_ALL_ACCOUNTS = "select a_id as id, a_login as login, a_password as password, " +
            "a_role.role_name as role_name, ad.account_id as account_id, ad.ad_first_name as ad_f_name, ad.ad_last_name as ad_l_name " +
            "from l_account join a_role  on a_role.role_id = l_account.a_role_id " +
            "join account_details ad on l_account.a_id = ad.account_id";

    private static final String SELECT_BY_LOGIN = "select a_id as id, a_login as login, a_password as password, " +
            "a_role.role_name as role_name, ad.account_id as account_id, ad.ad_first_name as ad_f_name, " +
            "ad.ad_last_name as ad_l_name from l_account join a_role  on a_role.role_id = l_account.a_role_id " +
            "join account_details ad on l_account.a_id = ad.account_id where a_login=?";

    private static final String SELECT_ACCOUNT_BY_ID = "select a_id as id, a_login as login, a_password as password, " +
            "a_role.role_name as role_name, ad.account_id as account_id, ad.ad_first_name as ad_f_name, " +
            "ad.ad_last_name as ad_l_name from l_account join a_role  on a_role.role_id = l_account.a_role_id " +
            "join account_details ad on l_account.a_id = ad.account_id where a_id=?";

    private static final String DELETE_ACCOUNT = "delete from l_account where a_id=?";

    private static final String ID_COLUMN_NAME = "id";
    private static final String LOGIN_COLUMN_NAME = "login";
    private static final String PASSWORD_COLUMN_NAME = "password";
    private static final String ROLE_NAME_COLUMN_NAME = "role_name";
    private static final String ACCOUNT_DETAILS_FIRST_NAME_COLUMN_NAME = "ad_f_name";
    private static final String ACCOUNT_DETAILS_LAST_NAME_COLUMN_NAME = "ad_l_name";
    private static final String ACCOUNT_ID_COLUMN_NAME = "account_id";

    private AccountDao(ConnectionPool pool) {
        super(pool, LOG);
    }

    @Override
    public Optional<Account> create(Account account) throws AccountDaoException {
        LOG.trace("start create account");
        Optional<Account> createdAccount = Optional.empty();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, account.getLogin());
            preparedStatement.setString(2, account.getPassword());
            final int numberChangedLines = preparedStatement.executeUpdate();
            if (numberChangedLines != 0) {
                final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long key = generatedKeys.getLong(1);
                    createdAccount = Optional.of(new Account(key, account.getLogin(), account.getPassword(), Role.READER));
                }
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not create account", e);
            throw new AccountDaoException("could not create new account");
        }
        return createdAccount;
    }

    @Override
    public Optional<Account> read(Long id) throws AccountDaoException {
        LOG.trace("start read account");
        Optional<Account> readAccount = Optional.empty();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_ID)) {
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final Account account = executeAccount(resultSet).orElseThrow(() -> new AccountDaoException("could not extract account"));
                return Optional.of(account);
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not find account", e);
            throw new AccountDaoException("could not find account");
        }
        return readAccount;
    }

    @Override
    public List<Account> readAll() throws AccountDaoException {
        LOG.trace("start read all accounts");
        List<Account> accounts = new ArrayList<>();
        try (final Connection connection = pool.takeConnection();
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(SELECT_ALL_ACCOUNTS)) {
            while (resultSet.next()) {
                final Account account = executeAccount(resultSet).orElseThrow(() -> new AccountDaoException("could not extract author"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not found accounts", e);
            throw new AccountDaoException("did not found accounts");
        }
        return accounts;
    }

    @Override
    public Optional<Account> update(Account entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) throws AccountDaoException {
        LOG.trace("start delete account");
        boolean deleteAccount = false;
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACCOUNT)) {
            preparedStatement.setLong(1, id);
            final int numberChangedLines = preparedStatement.executeUpdate();
            if (numberChangedLines != 0) {
                deleteAccount = true;
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not delete account", e);
            throw new AccountDaoException("could not delete account");
        }
        return deleteAccount;
    }

    public Optional<Account> readByLogin(String login) throws AccountDaoException {
        LOG.trace("start read account by login");
        Optional<Account> account = Optional.empty();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final Account executedAccount = executeAccount(resultSet).orElseThrow(()
                        -> new AccountDaoException("could not extract account"));
                account = Optional.of(executedAccount);
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not found a account", e);
            throw new AccountDaoException("could not found a account");
        }
        return account;
    }

    @Override
    public boolean updateRole(Long id, Integer idRole) throws AccountDaoException {
        LOG.trace("start update role");
        boolean updatedAccount = false;
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_ROLE)) {
            preparedStatement.setInt(1, idRole);
            preparedStatement.setLong(2, id);
            final int numberChangedLines = preparedStatement.executeUpdate();
            if (numberChangedLines > 0) {
                updatedAccount = true;
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not update author", e);
            throw new AccountDaoException("could not update author");
        }
        return updatedAccount;
    }

    private Optional<Account> executeAccount(ResultSet resultSet) {
        try {
            return Optional.of(new Account(resultSet.getLong(ID_COLUMN_NAME),
                    resultSet.getString(LOGIN_COLUMN_NAME), resultSet.getString(PASSWORD_COLUMN_NAME),
                    Role.valueOf(resultSet.getString(ROLE_NAME_COLUMN_NAME).toUpperCase()),
                    new AccountDetails(resultSet.getLong(ACCOUNT_ID_COLUMN_NAME), resultSet.getString(ACCOUNT_DETAILS_FIRST_NAME_COLUMN_NAME),
                            resultSet.getString(ACCOUNT_DETAILS_LAST_NAME_COLUMN_NAME))));
        } catch (SQLException e) {
            LOG.error("could not extract account from executeBook", e);
        }
        return Optional.empty();
    }

    public static AccountDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final AccountDao INSTANCE = new AccountDao(ConnectionPool.lockingPool());
    }
}
