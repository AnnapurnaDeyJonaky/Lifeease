package com.example.lifeease;

public class CartItem {
    private String productName;
    private String quantity;
    private float totalCost;

    public CartItem(String productName, String quantity, float totalCost) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }

    public String getProductName() {
        return productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public float getTotalCost() {
        return totalCost;
    }
}
