package com.webank.plugins.wecmdb.support.cmdb;

import com.webank.plugins.wecmdb.support.RemoteCallException;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponse;

public class CmdbRemoteCallException extends RemoteCallException {

    private static final long serialVersionUID = -8434149073950278847L;

    private transient CmdbResponse cmdbResponse;

    public CmdbRemoteCallException(String message) {
        super(message);
    }

    public CmdbRemoteCallException(String message, CmdbResponse cmdbResponse) {
        super(message);
        this.cmdbResponse = cmdbResponse;
    }

    public CmdbRemoteCallException(String message, CmdbResponse cmdbResponse, Throwable cause) {
        super(message, cause);
        this.cmdbResponse = cmdbResponse;
    }

    public CmdbResponse getCmdbResponse() {
        return cmdbResponse;
    }

    @Override
    public String getErrorMessage() {
        return String.format("%s (CMDB_ERROR_CODE: %s)", this.getMessage(), getStatusCode(cmdbResponse));
    }

    @Override
    public Object getErrorData() {
        return cmdbResponse == null ? null : cmdbResponse.getData();
    }

    private String getStatusCode(CmdbResponse cmdbResponse) {
        return cmdbResponse == null ? null : cmdbResponse.getStatusCode();
    }
}
