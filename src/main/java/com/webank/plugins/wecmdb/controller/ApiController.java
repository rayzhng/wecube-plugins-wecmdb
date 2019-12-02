package com.webank.plugins.wecmdb.controller;

import static com.webank.plugins.wecmdb.dto.JsonResponse.okay;
import static com.webank.plugins.wecmdb.dto.JsonResponse.okayWithData;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.plugins.wecmdb.dto.JsonResponse;
import com.webank.plugins.wecmdb.dto.OperateCiDto;
import com.webank.plugins.wecmdb.dto.OperateCiJsonResponse;
import com.webank.plugins.wecmdb.exception.OperationCiException;
import com.webank.plugins.wecmdb.support.cmdb.CmdbServiceV2Stub;

@RestController
public class ApiController {

    @Autowired
    private CmdbServiceV2Stub cmdbServiceV2Stub;

    @GetMapping("/entities/{entity-name}")
    @ResponseBody
    public JsonResponse retrieveCiData(@PathVariable(value = "entity-name") String entityName,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "sorting", required = false) String sorting,
            @RequestParam(value = "select", required = false) String select) {
        return okayWithData(cmdbServiceV2Stub.getCiDataWithConditions(entityName, filter, sorting, select));
    }

    @PostMapping("/entities/{entity-name}/create")
    public JsonResponse createCiData(@PathVariable("entity-name") String entityName, @RequestBody List<Map<String, Object>> request) {
        return okayWithData(cmdbServiceV2Stub.createCiData(entityName, request));
    }

    @PostMapping("/entities/{entity-name}/update")
    public JsonResponse updateCiData(@PathVariable("entity-name") String entityName, @RequestBody List<Map<String, Object>> request) {
        return okayWithData(cmdbServiceV2Stub.updateCiData(entityName, request));
    }

    @PostMapping("/entities/{entity-name}/delete")
    public JsonResponse deleteCiData(@PathVariable("entity-name") String entityName, @RequestBody List<Map<String, Object>> request) {
        cmdbServiceV2Stub.deleteCiData(entityName, request);
        return okay();
    }

    @PostMapping("/data/confirm")
    @ResponseBody
    public OperateCiJsonResponse confirmBatchCiData(@RequestBody List<OperateCiDto> operateCiDtos) {
        OperateCiJsonResponse response = null;
        try {
            response = OperateCiJsonResponse.okayWithData(cmdbServiceV2Stub.confirmBatchCiData(operateCiDtos));
        } catch (Exception e) {
            throw new OperationCiException(e.getMessage(),e);
        }

        return response;
    }

    @GetMapping("/data-model")
    @ResponseBody
    public JsonResponse getDataModel() {
        return okayWithData(cmdbServiceV2Stub.getDataModel());
    }
}