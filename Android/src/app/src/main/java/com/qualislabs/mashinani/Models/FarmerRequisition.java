package com.qualislabs.mashinani.Models;

public class FarmerRequisition {
    private String productType, createdAt, specialInstructions;
    private int id, quantity, creatorId;

    public FarmerRequisition() {
    }

    public FarmerRequisition(String productType, String createdAt, String specialInstructions, int id, int quantity, int creatorId) {
        this.productType = productType;
        this.createdAt = createdAt;
        this.specialInstructions = specialInstructions;
        this.id = id;
        this.quantity = quantity;
        this.creatorId = creatorId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
}
