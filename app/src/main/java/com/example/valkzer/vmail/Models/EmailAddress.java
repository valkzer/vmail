package com.example.valkzer.vmail.Models;

public class EmailAddress {

    private Integer id;
    private String email;

    public EmailAddress() {
    }

    public EmailAddress(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public EmailAddress setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public EmailAddress setEmail(String email) {
        this.email = email;
        return this;
    }
}
