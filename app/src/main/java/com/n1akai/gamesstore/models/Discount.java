package com.n1akai.gamesstore.models;

public class Discount {
    private String id;
    private String title;
    private String img;
    private String price;
    private String discount;
    private Game game;

    public Discount() {
    }

    public Discount(String id, String title, String img, String price, String discount, Game game) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.price = price;
        this.discount = discount;
        this.game = game;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public Game getGame() {
        return game;
    }
}
