package com.cz3002.shopfunding.Model;

import java.util.ArrayList;

public class SearchResult {
    private int itemId;
    private int shopId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public ArrayList<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ArrayList<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private ArrayList<String> imageLinks;
    private String name;
}
