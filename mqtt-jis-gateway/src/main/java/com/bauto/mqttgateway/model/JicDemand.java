package com.bauto.mqttgateway.model;

public class JicDemand {
    private String demandId;
    private String demandName;
    private String demandType;

    @Override
    public String toString() {
        return "JicDemand {demandId:" + demandId + ", demandName:" + demandName + ", demandType:" + demandType
                + ", partNumber:" + partNumber + ", locationCode:" + locationCode + ", leadTime:" + leadTime
                + ", quantity:" + quantity + ", addNote:" + addNote + "}";
    }

    private String partNumber;
    private String locationCode;
    private String leadTime;
    private int quantity;
    private String addNote;

    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    public String getDemandName() {
        return demandName;
    }

    public void setDemandName(String demandName) {
        this.demandName = demandName;
    }

    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAddNote() {
        return addNote;
    }

    public void setAddNote(String addNote) {
        this.addNote = addNote;
    }
}
