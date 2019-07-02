package com.switso.itap;

import java.util.HashMap;
import java.util.Map;

public class ShiftDays {

    private String shiftCodeId;
    private String shiftCode;
    private String description;
    private String isRestDay;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getShiftCodeId() {
        return shiftCodeId;
    }

    public void setShiftCodeId(String shiftCodeId) {
        this.shiftCodeId = shiftCodeId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsRestDay() {
        return isRestDay;
    }

    public void setIsRestDay(String isRestDay) {
        this.isRestDay = isRestDay;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}