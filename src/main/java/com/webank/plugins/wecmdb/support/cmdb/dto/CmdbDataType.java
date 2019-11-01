package com.webank.plugins.wecmdb.support.cmdb.dto;

public enum CmdbDataType {
    None("none"), Varchar("varchar"), Int("int"), DateTime("datetime"), Date("date");

    private String code;

    private CmdbDataType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    static public CmdbDataType fromCode(String code) {
        for (CmdbDataType type : values()) {
            if (None.equals(type))
                continue;

            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return None;
    }
}
