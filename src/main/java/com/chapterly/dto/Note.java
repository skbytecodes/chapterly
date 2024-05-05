package com.chapterly.dto;

public class Note {
    private String title;
    private String price;
    private String count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
