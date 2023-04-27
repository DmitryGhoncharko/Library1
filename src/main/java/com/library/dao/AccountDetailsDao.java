package com.library.dao;

import com.library.connection.ConnectionPool;
import com.library.exception.AccountDetailsDaoException;
import com.library.model.AccountDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class AccountDetailsDao extends AbstractDao<AccountDetails>{

    private static final Logger LOG = LogManager.getLogger(AccountDetailsDao.class);

    private static final String SELECT_ACCOUNT_DETAILS_BY_ID = "select account_id as account_id, " +
            "ad_first_name as ad_f_name, ad_last_name as ad_l_name from account_details where account_id=?";

    private static final String INSERT_NEW_ACCOUNT_DETAILS = "insert into account_details (account_id, ad_first_name, ad_last_name) values (?,?,?)";
    private static final String ACCOUNT_ID_COLUMN_NAME = "account_id";
    private static final String AD_F_NAME_COLUMN_NAME = "ad_f_name";
    private static final String AD_L_NAME_COLUMN_NAME = "ad_l_name";

    private AccountDetailsDao(ConnectionPool pool) {
        super(pool, LOG);
    }


    @Override
    public Optional<AccountDetails> create(AccountDetails accountDetails) {
        LOG.trace("start create in account details");
        Optional<AccountDetails> createdAccountDetails = Optional.empty();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_ACCOUNT_DETAILS)) {
            preparedStatement.setLong(1, accountDetails.getId());
            preparedStatement.setString(2, accountDetails.getFirstName());
            preparedStatement.setString(3, accountDetails.getLastName());
            final int numberChangedLines = preparedStatement.executeUpdate();
            if (numberChangedLines != 0) {
                createdAccountDetails = Optional.of(accountDetails);
            } else
                throw new AccountDetailsDaoException("could not change lines for create account details");
        } catch (SQLException e) {
            LOG.error("sql error, could not create account details", e);
        } catch (AccountDetailsDaoException e) {
            LOG.error("could not create new account details", e);
        }
        return createdAccountDetails;
    }

    @Override
    public Optional<AccountDetails> read(Long id) {
        LOG.trace("start read account details (read by id)");
        Optional<AccountDetails> accountDetails = Optional.empty();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_DETAILS_BY_ID)) {
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final AccountDetails executedAccountDetails = executeAccountDetails(resultSet).orElseThrow(()
                        -> new AccountDetailsDaoException("could not extract account details"));
                accountDetails = Optional.of(executedAccountDetails);
            }
            return accountDetails;
        } catch (SQLException e) {
            LOG.error("sql error, could not found an account details", e);
        } catch (AccountDetailsDaoException e) {
            LOG.error("could not found an account details", e);
        }
        return accountDetails;
    }

    @Override
    public List<AccountDetails> readAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<AccountDetails> update(AccountDetails entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException();
    }

    private Optional<AccountDetails> executeAccountDetails(ResultSet resultSet) {
        try {
            final AccountDetails accountDetails = new AccountDetails(resultSet.getLong(ACCOUNT_ID_COLUMN_NAME),
                    resultSet.getString(AD_F_NAME_COLUMN_NAME), resultSet.getString(AD_L_NAME_COLUMN_NAME));
            return Optional.of(accountDetails);
        } catch (SQLException e) {
            LOG.error("could not extract account details from executeAccountDetails", e);
            return Optional.empty();
        }
    }

    public static AccountDetailsDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final AccountDetailsDao INSTANCE = new AccountDetailsDao(ConnectionPool.lockingPool());
    }
}
