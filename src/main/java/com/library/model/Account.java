package com.library.model;

import java.util.Objects;

public class Account implements Entity{

    private final Long id;
    private final String login;
    private final String password;
    private final Role role;
    private final AccountDetails details;

    public Account(Long id, String login, String password, Role role, AccountDetails details) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.details = details;
    }

    public Account(String login, String password) {
        this(null, login, password, Role.READER, null);
    }

    public Account(Long id, String login, String password, AccountDetails details) {
        this(id, login, password, Role.READER, details);
    }

    public Account(Long id, String login, String password, Role role) {
        this(id, login, password, role, null);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public AccountDetails getDetails() {
        return details;
    }

    public Account withPassword(String password) {
        return new Account(id, login, password, details);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(login, account.login)
                && Objects.equals(password, account.password) && role == account.role
                && Objects.equals(details, account.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, details);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", details=" + details +
                '}';
    }
}
