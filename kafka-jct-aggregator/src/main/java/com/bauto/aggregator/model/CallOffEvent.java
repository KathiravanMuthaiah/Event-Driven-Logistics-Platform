package com.bauto.aggregator.model;

public class CallOffEvent {
    private String callOffId;
    private String partNumber;
    private String locationCode;
    private String leadTime;
    private int tad;
    private int quantity;
    private String demandType; // JIT, JIS, JIC
    private String additionalNotes;

    

    @Override
    public String toString() {
        return "CallOffEvent{callOffId:" + callOffId + ", partNumber:" + partNumber + ", locationCode:" + locationCode
                + ", leadTime:" + leadTime + ", tad:" + tad + ", quantity:" + quantity + ", demandType:" + demandType
                + ", additionalNotes:" + additionalNotes + "}";
    }
    public String getCallOffId() {
        return callOffId;
    }
    public void setCallOffId(String callOffId) {
        this.callOffId = callOffId;
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
    public int getTad() {
        return tad;
    }
    public void setTad(int tad) {
        this.tad = tad;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getDemandType() {
        return demandType;
    }
    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }
    public String getAdditionalNotes() {
        return additionalNotes;
    }
    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }
    
}
