package com.hc.hicareservices.data.model.slots;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SlotResponse {

    @SerializedName("RequestData")
    @Expose
    private String requestData;
    @SerializedName("ResponseData")
    @Expose
    private String responseData;
    @SerializedName("ErrorData")
    @Expose
    private Object errorData;
    @SerializedName("TimeSlot")
    @Expose
    private List<TimeSlot> timeSlot = null;

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }

    public List<TimeSlot> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(List<TimeSlot> timeSlot) {
        this.timeSlot = timeSlot;
    }
}
