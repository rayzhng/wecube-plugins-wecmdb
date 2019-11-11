package com.webank.plugins.wecmdb.support.cmdb.dto;

public class CmdbOperateCiDto {
    private String guid;
    private int ciTypeId;

    public CmdbOperateCiDto() {

    }

    public CmdbOperateCiDto(String guid, int ciTypeId) {
        this.setGuid(guid);
        this.setCiTypeId(ciTypeId);
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getCiTypeId() {
        return ciTypeId;
    }

    public void setCiTypeId(int ciTypeId) {
        this.ciTypeId = ciTypeId;
    }

}
