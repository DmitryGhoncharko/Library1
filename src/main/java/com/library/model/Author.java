package com.library.model;

import java.util.Objects;

public class Author implements Entity{

    private final Long id;
    private final String firstName;
    private final String lastName;

    public Author(Long id, String firstName, String lastName ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Author(String first_name, String last_name) {
        this(null, first_name, last_name);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id)
                && Objects.equals(firstName, author.firstName)
                && Objects.equals(lastName, author.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                '}';
    }
}
