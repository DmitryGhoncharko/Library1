package com.library.model;

import java.sql.Date;
import java.util.Objects;

public class BookOrder implements Entity{

    private final Long id;
    private final AccountDetails details;
    private final Book book;
    private final OrderType type;
    private final Date dateCreate;
    private final Date dateIssue;
    private final Date dateReturn;
    private final OrderStatus status;


    private BookOrder(Long id, AccountDetails details, Book book,
                     OrderType type, Date dateCreate, Date dateIssue,
                     Date dateReturn, OrderStatus status) {
        this.id = id;
        this.details = details;
        this.book = book;
        this.type = type;
        this.dateCreate = dateCreate;
        this.dateIssue = dateIssue;
        this.dateReturn = dateReturn;
        this.status = status;
    }

    private BookOrder(AccountDetails details, Book book, OrderType type, Date dateCreate,
                      Date dateIssue, Date dateReturn, OrderStatus status) {
        this(null, details, book, type, dateCreate, dateIssue, dateReturn, status);
    }

    @Override
    public Long getId() {
        return id;
    }

    public AccountDetails getDetails() {
        return details;
    }

    public Book getBook() {
        return book;
    }

    public OrderType getType() {
        return type;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public Date getDateIssue() {
        return dateIssue;
    }

    public Date getDateReturn() {
        return dateReturn;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public static Builder with() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookOrder bookOrder = (BookOrder) o;
        return Objects.equals(id, bookOrder.id)
                && Objects.equals(details, bookOrder.details) &&
                Objects.equals(book, bookOrder.book)
                && type == bookOrder.type
                && Objects.equals(dateCreate, bookOrder.dateCreate)
                && Objects.equals(dateIssue, bookOrder.dateIssue)
                && Objects.equals(dateReturn, bookOrder.dateReturn)
                && status == bookOrder.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, details, book, type,
                dateCreate, dateIssue, dateReturn, status);
    }

    @Override
    public String toString() {
        return "BookOrder{" +
                "id=" + id +
                ", details=" + details +
                ", book=" + book +
                ", type=" + type +
                ", dateCreate=" + dateCreate +
                ", dateIssue=" + dateIssue +
                ", dateReturn=" + dateReturn +
                ", status=" + status +
                '}';
    }

    public static final class Builder {

        private Long id;
        private AccountDetails details;
        private Book book;
        private OrderType type;
        private Date dateCreate;
        private Date dateIssue;
        private Date dateReturn;
        private OrderStatus status;

        private Builder() {}

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder details(AccountDetails details) {
            this.details = details;
            return this;
        }

        public Builder book(Book book) {
            this.book = book;
            return this;
        }

        public Builder type(OrderType type) {
            this.type = type;
            return this;
        }

        public Builder dateCreate(Date dateCreate) {
            this.dateCreate = dateCreate;
            return this;
        }

        public Builder dateIssue(Date dateIssue) {
            this.dateIssue = dateIssue;
            return this;
        }

        public Builder dateReturn(Date dateReturn) {
            this.dateReturn = dateReturn;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public BookOrder create() {
            return new BookOrder(id, details, book, type,
                    dateCreate, dateIssue, dateReturn, status);
        }

        public BookOrder createWithoutId() {
            return new BookOrder(details, book, type,
                    dateCreate, dateIssue, dateReturn, status);
        }
    }
}
