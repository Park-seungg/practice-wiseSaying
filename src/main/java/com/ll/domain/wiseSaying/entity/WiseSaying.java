package com.ll.domain.wiseSaying.entity;

public class WiseSaying {
    private long id;
    private String content;
    private String author;

    public WiseSaying() {}

    public WiseSaying(long id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}