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

    @PostMapping("/entities/{entity-name}/{guid}")
    @ResponseBody
    public JsonResponse retrieveCiData(@PathVariable(value = "entity-name") String entityName, @PathVariable(value = "guid") String guid) {
        return okayWithData(cmdbServiceV2Stub.getCiDataByGuid(entityName, guid));
    }

    @PostMapping("/entities/{entity-name}/{guid}/confirm")
    @ResponseBody
    public JsonResponse confirmCiData(@PathVariable(value = "entity-name") String entityName, @PathVariable(value = "guid") String guid) {
        return okayWithData(cmdbServiceV2Stub.confirmCiData(entityName, guid));
    }
    
    @GetMapping("/data-model")
    @ResponseBody
    public JsonResponse getDataModel() {
        return okayWithData(cmdbServiceV2Stub.getDataModel());
    }
}