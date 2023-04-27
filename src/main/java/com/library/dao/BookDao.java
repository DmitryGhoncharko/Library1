package com.library.dao;

import com.library.connection.ConnectionPool;
import com.library.exception.BookDaoException;
import com.library.model.Author;
import com.library.model.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class BookDao extends AbstractDao<Book> implements BasicBookDao<Book>{

    private static final Logger LOG = LogManager.getLogger(BookDao.class);

    private static final String INSERT_BOOK = "insert into book (b_title, b_date_published, b_amount_of_left) values (?,?,?)";

    private static final String INSERT_BOOK_IN_AUTHOR_TO_BOOK = "insert into author_to_book (book_id, author_id) values (?,?)";

    private static final String SELECT_BY_ID = "select b_id as id_book, b_title as book_title, b_date_published as book_date_published, b_amount_of_left as book_amount_of_left from book where b_id=?";

    private static final String SELECT_ALL_BOOKS = "select b_id as id_book, b_title as book_title, b_date_published as book_date_published, b_amount_of_left as book_amount_of_left from book";

    private static final String UPDATE_BOOK = "update book set b_title=?, b_date_published=?, b_amount_of_left=? where b_id=?";

    private static final String DECREASE_BOOK_AMOUNT_OF_LEFT = "update book set b_amount_of_left=? where b_id=?";

    private static final String DELETE_BOOK_BY_ID = "delete from book where b_id=?";

    private static final String SELECT_ALL_BOOKS_WITH_AUTHORS = "select b.b_id as id_book, b.b_title as book_title, " +
            "b.b_date_published as book_date_published, b.b_amount_of_left as book_amount_of_left, " +
            "a.id as id_author, a.first_name as author_f_name, a.last_name as author_l_name " +
            "from book b join author_to_book atb on b.b_id = atb.book_id join author a " +
            "on atb.author_id = a.id order by b.b_id";

    private static final String SELECT_BOOK_BY_ID_WITH_AUTHORS = "select book.b_id as id_book, author.id as id_author, " +
            "author.first_name as author_f_name, author.last_name as author_l_name, book.b_title as book_title," +
            " book.b_date_published as book_date_published, book.b_amount_of_left as book_amount_of_left from author join author_to_book atb" +
            " on author.id = atb.author_id join book on atb.book_id = book.b_id where book.b_id = ?";

    private static final String SELECT_BY_TITLE = "select book.b_id as id_book, author.id as id_author, " +
            "author.first_name as author_f_name, author.last_name as author_l_name, book.b_title as book_title, " +
            "book.b_date_published as book_date_published, book.b_amount_of_left as book_amount_of_left from author join author_to_book atb " +
            "on author.id = atb.author_id join book on atb.book_id = book.b_id where book.b_title = ?";

    private static final String ID_BOOK_COLUMN_NAME = "id_book";
    private static final String BOOK_TITLE_COLUMN_NAME = "book_title";
    private static final String BOOK_DATE_PUBLISHED_COLUMN_NAME = "book_date_published";
    private static final String BOOK_AMOUNT_OF_LEFT_COLUMN_NAME = "book_amount_of_left";
    private static final String ID_AUTHOR_COLUMN_NAME = "id_author";
    private static final String AUTHOR_LAST_NAME_COLUMN_NAME = "author_l_name";
    private static final String AUTHOR_FIRST_NAME_COLUMN_NAME = "author_f_name";

    private static Long idLastBook = 0L;

    private BookDao(ConnectionPool pool) {
        super(pool, LOG);
    }

    @Override
    public Optional<Book> create(Book book) throws BookDaoException {
        LOG.trace("start create book");
        Optional<Book> createdBook = Optional.empty();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setDate(2, (Date) book.getDatePublished());
            preparedStatement.setInt(3, book.getAmountOfLeft());
            final int numberChangedLines = preparedStatement.executeUpdate();
            if (numberChangedLines > 0) {
                final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long key = generatedKeys.getLong(1);
                    LOG.info("key = {}", key);
                    createdBook = Optional.of(new Book(key, book.getTitle(), book.getDatePublished(), book.getAmountOfLeft()));
                }
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not create book", e);
            throw new BookDaoException("could not create new book");
        }
        return createdBook;
    }

    @Override
    public Optional<Book> read(Long id) throws BookDaoException {
        LOG.trace("start read (read by id)");
        Optional<Book> book = Optional.empty();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final Book executedBook = executeBook(resultSet).orElseThrow(()
                        -> new BookDaoException("could not extract book"));
                book = Optional.of(executedBook);
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not found a book", e);
            throw new BookDaoException("could not found a book");
        }
        return book;
    }

    @Override
    public List<Book> readAll() throws BookDaoException {
        LOG.trace("start readAll");
        List<Book> books = new ArrayList<>();
        try (final Connection connection = pool.takeConnection();
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(SELECT_ALL_BOOKS)){
            while (resultSet.next()) {
                final Book book = executeBook(resultSet).orElseThrow(()
                        -> new BookDaoException("could not extract book"));
                books.add(book);
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not found books", e);
            throw new BookDaoException("could not found books");
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Book> update(Book book) throws BookDaoException {
        LOG.trace("start update book");
        Optional<Book> updatedBook = Optional.empty();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setDate(2, (Date) book.getDatePublished());
            preparedStatement.setInt(3, book.getAmountOfLeft());
            preparedStatement.setLong(4, book.getId());
            final int numberChangedLines = preparedStatement.executeUpdate();
            if (numberChangedLines > 0) {
                updatedBook = Optional.of(book);
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not update book", e);
            throw new BookDaoException("could not update book");
        }
        return updatedBook;
    }

    @Override
    public boolean delete(Long idBook) throws BookDaoException {
        LOG.trace("start delete book");
        boolean deleteBook = false;
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK_BY_ID)) {
            preparedStatement.setLong(1, idBook);
            final int numberChangedLines = preparedStatement.executeUpdate();
            if (numberChangedLines != 0) {
                deleteBook = true;
                LOG.info("deleted book with id: {}", idBook);
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not delete book", e);
            throw new BookDaoException("could not delete book");
        }
        return deleteBook;
    }

    public boolean createBookInAuthorToBook(Long idBook, Long idAuthor) throws BookDaoException {
        LOG.trace("start create book in author to book");
        boolean createBookInAuthorToBook = false;
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK_IN_AUTHOR_TO_BOOK)) {
            preparedStatement.setLong(1, idBook);
            preparedStatement.setLong(2, idAuthor);
            final int numberChangedLines = preparedStatement.executeUpdate();
            if (numberChangedLines > 0) {
                createBookInAuthorToBook = true;
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not create book in author to book", e);
            throw new BookDaoException("could not create in author_to_book");
        }
        return createBookInAuthorToBook;
    }

    public Optional<Book> readWithAuthors(Long id) throws BookDaoException {
        LOG.trace("start readWithAuthors (read by id)");
        LinkedList<Book> books = new LinkedList<>();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_ID_WITH_AUTHORS)) {
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final Book book = executeBookWithAuthors(resultSet).orElseThrow(()
                        -> new BookDaoException("could not extract book"));
                if (Objects.equals(idLastBook, book.getId()) && !books.isEmpty()) {
                    idLastBook = book.getId();
                    final Book lastBook = books.getLast();
                    books.removeLast();
                    final List<Author> authors = lastBook.getAuthors();
                    authors.add(book.getAuthors().get(book.getAuthors().size()-1));
                    books.add(book.getBookWithAuthors(authors));
                } else {
                    idLastBook = book.getId();
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not found a book", e);
            throw new BookDaoException("could not found a book");
        }
        return Optional.ofNullable(books.getFirst());
    }

    public List<Book> readAllWithAuthors() throws BookDaoException {
        LOG.trace("start readAllWithAuthors");
        List<Book> books = new ArrayList<>();
        try (final Connection connection = pool.takeConnection();
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(SELECT_ALL_BOOKS_WITH_AUTHORS)){
            while (resultSet.next()) {
                final Book book = executeBookWithAuthors(resultSet).orElseThrow(()
                        -> new BookDaoException("could not extract book"));

                    books.add(book);
                }

        } catch (SQLException e) {
            LOG.error("sql error, could not found books", e);
            throw new BookDaoException("could not found books");
        }
        return books;
    }

    public boolean decreaseAmountOfLeft(Long idBook, Integer amountOfLeft) throws BookDaoException {
        LOG.trace("start decrease amount of left");
        boolean createBookInAuthorToBook = false;
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(DECREASE_BOOK_AMOUNT_OF_LEFT)) {
            preparedStatement.setInt(1, amountOfLeft);
            preparedStatement.setLong(2, idBook);
            final int numberChangedLines = preparedStatement.executeUpdate();
            if (numberChangedLines > 0) {
                createBookInAuthorToBook = true;
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not create book in author to book", e);
            throw new BookDaoException("could not create in author_to_book");
        }
        return createBookInAuthorToBook;
    }

    public Optional<Book> readByTitle(String title) throws BookDaoException {
        Optional<Book> book = Optional.empty();
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_TITLE)) {
            preparedStatement.setString(1, title);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final Book executedBook = executeBookWithAuthors(resultSet).orElseThrow(()
                        -> new BookDaoException("could not extract book"));
                book = Optional.of(executedBook);
            }
        } catch (SQLException e) {
            LOG.error("sql error, could not found a book", e);
            throw new BookDaoException("could not found a book");
        }
        return book;
    }

    private Optional<Book> executeBook(ResultSet resultSet){
        try {
            return Optional.of(new Book(resultSet.getLong(ID_BOOK_COLUMN_NAME), resultSet.getString(BOOK_TITLE_COLUMN_NAME),
                    resultSet.getDate(BOOK_DATE_PUBLISHED_COLUMN_NAME), resultSet.getInt(BOOK_AMOUNT_OF_LEFT_COLUMN_NAME)));
        } catch (SQLException e) {
            LOG.error("could not extract book from executeBook", e);
        }
        return Optional.empty();
    }

    private Optional<Book> executeBookWithAuthors(ResultSet resultSet){
        try {
            List<Author> authors = new ArrayList<>();
            authors.add(new Author(resultSet.getLong(ID_AUTHOR_COLUMN_NAME),resultSet.getString(AUTHOR_FIRST_NAME_COLUMN_NAME),
                    resultSet.getString(AUTHOR_LAST_NAME_COLUMN_NAME)));
            return Optional.of(new Book(resultSet.getLong(ID_BOOK_COLUMN_NAME), resultSet.getString(BOOK_TITLE_COLUMN_NAME),
                    resultSet.getDate(BOOK_DATE_PUBLISHED_COLUMN_NAME), resultSet.getInt(BOOK_AMOUNT_OF_LEFT_COLUMN_NAME),
                    authors));
        } catch (SQLException e) {
            LOG.error("could not extract book from executeBook", e);
        }
        return Optional.empty();
    }

    public static BookDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final BookDao INSTANCE = new BookDao(ConnectionPool.lockingPool());
    }
}
