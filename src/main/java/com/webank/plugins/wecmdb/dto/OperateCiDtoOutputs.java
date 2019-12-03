package com.webank.plugins.wecmdb.dto;

import java.util.List;

public class OperateCiDtoOutputs {
    private List<Object> outputs;

    public List<Object> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Object> outputs) {
        this.outputs = outputs;
    }

    @Override
    public String toString() {
        return "OperateCiDtoOutputs [outputs=" + outputs + "]";
    }
    
}
