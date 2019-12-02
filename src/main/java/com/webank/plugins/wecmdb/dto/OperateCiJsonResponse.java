package com.webank.plugins.wecmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperateCiJsonResponse extends JsonResponse{
    public final static String STATUS_OK = "OK";
    public final static String STATUS_ERROR = "ERROR";

    @JsonProperty(value = "result_code")
    private String status;
    @JsonProperty(value = "result_message")
    private String message;
    @JsonProperty(value = "results")
    private Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public OperateCiJsonResponse withData(Object data) {
        this.data = data;
        return this;
    }

    public static OperateCiJsonResponse okay() {
        OperateCiJsonResponse result = new OperateCiJsonResponse();
        result.setStatus(STATUS_OK);
        result.setMessage("Success");
        return result;
    }

    public static OperateCiJsonResponse okayWithData(Object data) {
        return okay().withData(data);
    }

    public static OperateCiJsonResponse error(String errorMessage) {
        OperateCiJsonResponse result = new OperateCiJsonResponse();
        result.setStatus(STATUS_ERROR);
        result.setMessage(errorMessage);
        return result;
    }
}
