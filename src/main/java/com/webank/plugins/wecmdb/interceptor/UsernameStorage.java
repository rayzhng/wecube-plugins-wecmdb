package com.webank.plugins.wecmdb.interceptor;

public class UsernameStorage extends ThreadLocal<String> {
    private static UsernameStorage instance = new UsernameStorage();

    public static UsernameStorage getIntance() {
        return instance;
    }
}