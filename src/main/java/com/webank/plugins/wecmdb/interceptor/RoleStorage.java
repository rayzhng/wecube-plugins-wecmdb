package com.webank.plugins.wecmdb.interceptor;

public class RoleStorage extends ThreadLocal<String> {
    private static RoleStorage instance = new RoleStorage();

    public static RoleStorage getIntance() {
        return instance;
    }
}