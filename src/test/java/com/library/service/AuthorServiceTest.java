package com.library.service;

import com.library.dao.AuthorDao;
import com.library.exception.AuthorDaoException;
import com.library.exception.ServiceException;
import com.library.model.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorDao authorDao;
    @InjectMocks
    private AuthorService authorService;


    @Test
    public void test_create_shouldEnterToAuthorDaoAuthor_whenEnterCorrectData() throws ServiceException, AuthorDaoException {
        final String firstName = "One";
        final String lastName = "Two";
        Author expected = new Author(firstName, lastName);

        authorService.create(firstName, lastName);

        verify(authorDao).create(expected);
    }

    @Test
    public void test_create_shouldReturnEmpty_whenEnterNullsData() throws ServiceException{
        final Optional<Object> expected = Optional.empty();

        final Optional<Author> author = authorService.create(null, null);

        Assertions.assertEquals(expected, author);
    }

}
