package com.bauto.aggregator.model;

import java.util.List;

public class JisDemand {
    private String demandId;
    private String demandName;
    private String demandType;
    private List<String> partNumbers;
    private String locationCode;
    private String leadTime;
    private int tad;
    private int quantity;
    private String lineDetails;
    private String addNote;

    

    @Override
    public String toString() {
        return "JisDemand {demandId:" + demandId + ", demandName:" + demandName + ", demandType:" + demandType
                + ", partNumbers:" + partNumbers + ", locationCode:" + locationCode + ", leadTime:" + leadTime
                + ", tad:" + tad + ", quantity:" + quantity + ", lineDetails:" + lineDetails + ", addNote:" + addNote
                + "}";
    }

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

    public List<String> getPartNumbers() {
        return partNumbers;
    }

    public void setPartNumbers(List<String> partNumbers) {
        this.partNumbers = partNumbers;
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

    public String getLineDetails() {
        return lineDetails;
    }

    public void setLineDetails(String lineDetails) {
        this.lineDetails = lineDetails;
    }

    public String getAddNote() {
        return addNote;
    }

    public void setAddNote(String addNote) {
        this.addNote = addNote;
    }
}
