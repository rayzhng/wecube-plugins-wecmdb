package com.webank.plugins.wecmdb.support.cmdb;

public class CmdbDataNotFoundException extends CmdbRemoteCallException {
    private static final long serialVersionUID = 3575545630477842982L;

    public CmdbDataNotFoundException(String message) {
        super(message);
    }
}
