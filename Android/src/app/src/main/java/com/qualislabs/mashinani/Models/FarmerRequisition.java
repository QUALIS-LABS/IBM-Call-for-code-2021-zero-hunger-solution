package com.qualislabs.mashinani.Models;

public class FarmerRequisition {
    private String productType, createdAt, specialInstructions, pickupLocation;
    private int id, quantity, creatorId;

    public FarmerRequisition() {
    }

    public FarmerRequisition(int id, int quantity, int creatorId, String productType, String createdAt, String specialInstructions, String pickupLocation) {
        this.id = id;
        this.quantity = quantity;
        this.creatorId = creatorId;
        this.productType = productType;
        this.createdAt = createdAt;
        this.specialInstructions = specialInstructions;
        this.pickupLocation = pickupLocation;

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

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
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
