package com.webank.plugins.wecmdb.support.cmdb.dto;

import java.util.List;

public class CmdbResponse<DATATYPE> {
    public static final String STATUS_CODE_OK = "OK";

    private String statusCode;
    private String statusMessage;
    private DATATYPE data;

    public static class DefaultCmdbResponse extends CmdbResponse<Object> {}

    public static class IntegerCmdbResponse extends CmdbResponse<Integer> {}

    public static class ListDataResponse extends CmdbResponse<List> {}

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public DATATYPE getData() {
        return data;
    }

    public void setData(DATATYPE data) {
        this.data = data;
    }

}
