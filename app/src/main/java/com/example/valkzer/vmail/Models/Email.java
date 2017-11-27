package com.example.valkzer.vmail.Models;

public class Email {
    private Integer id;
    private String subject;
    private String to;
    private String from;
    private String body;
    private Boolean read;

    public Email() {

    }

    public Email(Integer id, String subject, String to, String from, String body, Boolean read) {
        this.id = id;
        this.subject = subject;
        this.to = to;
        this.from = from;
        this.body = body;
        this.read = read;
    }

    public Integer getId() {
        return id;
    }

    public Email setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Email setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getTo() {
        return to;
    }

    public Email setTo(String to) {
        this.to = to;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public Email setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Email setBody(String body) {
        this.body = body;
        return this;
    }

    public Boolean getRead() {
        return read;
    }

    public Email setRead(Boolean read) {
        this.read = read;
        return this;
    }
}
