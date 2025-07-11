package com.bauto.postgreswriter.model;

import java.time.LocalDateTime;

public class CallOffEvent {
    private String callOffId;
    private String supplierId;
    private String partNumber;
    private int quantity;
    private String destinationLocation;
    private LocalDateTime plannedDeliveryTime;
    private String status;

    // Getters and Setters

    public String getCallOffId() {
        return callOffId;
    }

    public void setCallOffId(String callOffId) {
        this.callOffId = callOffId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public LocalDateTime getPlannedDeliveryTime() {
        return plannedDeliveryTime;
    }

    public void setPlannedDeliveryTime(LocalDateTime plannedDeliveryTime) {
        this.plannedDeliveryTime = plannedDeliveryTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

