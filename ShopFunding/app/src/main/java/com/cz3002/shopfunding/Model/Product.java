package com.cz3002.shopfunding.Model;
import java.util.ArrayList;
import com.cz3002.shopfunding.Model.ProductModel;

public class Product {
    private String name;
    private int itemId;
    private int shopId;
    private ArrayList<String> imageLinks;
    private ArrayList<ProductModel> models;
    private double minPrice;
    private double shippingFee;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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

    public ArrayList<ProductModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<ProductModel> models) {
        this.models = models;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;


}
