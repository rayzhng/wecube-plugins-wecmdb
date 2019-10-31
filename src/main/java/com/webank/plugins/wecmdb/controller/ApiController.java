package com.webank.plugins.wecmdb.controller;

import static com.webank.plugins.wecmdb.dto.JsonResponse.okayWithData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.plugins.wecmdb.dto.JsonResponse;
import com.webank.plugins.wecmdb.support.cmdb.CmdbServiceV2Stub;

@RestController
@RequestMapping("/wecmdb")
public class ApiController {

    @Autowired
    private CmdbServiceV2Stub cmdbServiceV2Stub;

    @PostMapping("/cis/{ci-type-name}/datas/{guid}")
    @ResponseBody
    public JsonResponse retrieveCiData(@PathVariable(value = "ci-type-name") String ciTypeName, @PathVariable(value = "guid") String guid) {
        return okayWithData(cmdbServiceV2Stub.getCiDataByGuid(ciTypeName, guid));
    }

    @PostMapping("/cis/{ci-type-name}/datas/{guid}/confirm")
    @ResponseBody
    public JsonResponse implementCiType(@PathVariable(value = "ci-type-name") String ciTypeName, @PathVariable(value = "guid") String guid) {
        return okayWithData(cmdbServiceV2Stub.confirmCi(ciTypeName, guid));
    }
    
    @GetMapping("/data-model")
    @ResponseBody
    public JsonResponse getDataModel() {
        return okayWithData(cmdbServiceV2Stub.getDataModel());
    }
}