package com.webank.plugins.wecmdb.support.cmdb.dto;

public enum CmdbInputType {
    None("none"), Text("text"), Date("date"), TextArea("textArea"), Droplist("select"), MultSelDroplist("multiSelect"), Reference("ref"), MultRef("multiRef"), Number("number"), OrchestrationMuliRef("orchestration_multi_ref"),
    Orchestration("orchestration_ref");

    private String code;

    private CmdbInputType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    static public CmdbInputType fromCode(String code) {
        for (CmdbInputType inputType : values()) {
            if (None.equals(inputType))
                continue;

            if (inputType.getCode().equals(code)) {
                return inputType;
            }
        }
        return None;
    }
}
