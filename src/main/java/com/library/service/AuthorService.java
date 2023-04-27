package com.library.service;

import com.library.dao.AuthorDao;
import com.library.exception.AuthorDaoException;
import com.library.exception.ServiceException;
import com.library.model.Author;
import com.library.validation.FirstLastNameValidator;
import com.library.validation.ProtectJSInjection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AuthorService implements BasicAuthorService{

    private static final Logger LOG = LogManager.getLogger(AuthorService.class);

    private final AuthorDao authorDao;

    private AuthorService(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public Optional<Author> create(String firstName, String lastName) throws ServiceException {
        try {
            final Author safeAuthor = createSafeAuthor(firstName, lastName);
            if (!checkDuplication(safeAuthor) && checkAuthorData(safeAuthor.getFirstName(), safeAuthor.getLastName())) {
                return authorDao.create(safeAuthor);
            }
        } catch (AuthorDaoException e) {
            LOG.error("dao error, could not create author", e);
            throw new ServiceException("Could not create author");
        }
        return Optional.empty();
    }

    @Override
    public Optional<Author> findById(Long id) throws ServiceException {
        try {
            return authorDao.read(id);
        } catch (AuthorDaoException e) {
            LOG.error("dao error, could not read author", e);
            throw new ServiceException("Could not find author");
        }
    }

    @Override
    public List<Author> findAll() throws ServiceException {
        try {
            return authorDao.readAll();
        } catch (AuthorDaoException e) {
            LOG.error("dao error, could not read all authors", e);
            throw new ServiceException("Could not find all authors");
        }
    }

    @Override
    public Optional<Author> update(Long id, String firstName, String lastName) throws ServiceException {
        try {
            checkAuthorData(firstName, lastName);
            Author author = new Author(id, firstName, lastName);
            return authorDao.update(author);
        } catch (AuthorDaoException e) {
            LOG.error("Dao error, could not update author", e);
            throw new ServiceException("Could not update author");
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        try {
            return authorDao.delete(id);
        } catch (AuthorDaoException e) {
            LOG.error("dao error, could not delete author", e);
            throw new ServiceException("Could not delete author");
        }
    }

    private Author createSafeAuthor(String firstName, String lastName) {
        final String safeFirstName = ProtectJSInjection.getInstance().protectInjection(firstName);
        final String safeLastName = ProtectJSInjection.getInstance().protectInjection(lastName);
        return new Author(safeFirstName, safeLastName);
    }

    private boolean checkAuthorData(String firstName, String lastName) {
        if (FirstLastNameValidator.getInstance().validate(firstName, lastName)) {
            return true;
        }
        return false;
    }

    private boolean checkDuplication(Author author) throws AuthorDaoException {
        if (authorDao.readAuthorByFirstLastName(author).isPresent()) {
            return true;
        }
        return false;
    }

    static AuthorService getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final AuthorService INSTANCE = new AuthorService(AuthorDao.getInstance());
    }
}