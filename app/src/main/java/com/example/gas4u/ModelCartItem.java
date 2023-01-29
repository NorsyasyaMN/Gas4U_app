package com.example.gas4u;

public class ModelCartItem {
    String price,  priceEach, productId, quantity, title;

    public ModelCartItem(){

    }
    public ModelCartItem(String price, String priceEach, String productId, String quantity, String title) {
        this.price = price;
        this.priceEach = priceEach;
        this.productId = productId;
        this.quantity = quantity;
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(String priceEach) {
        this.priceEach = priceEach;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

