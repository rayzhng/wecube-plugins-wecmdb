package com.webank.plugins.wecmdb.controller;

import static com.webank.plugins.wecmdb.dto.JsonResponse.okayWithData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.plugins.wecmdb.dto.JsonResponse;
import com.webank.plugins.wecmdb.dto.OperateCiDto;
import com.webank.plugins.wecmdb.support.cmdb.CmdbServiceV2Stub;

@RestController
@RequestMapping("/wecmdb")
public class ApiController {

    @Autowired
    private CmdbServiceV2Stub cmdbServiceV2Stub;

    @PostMapping("/ci/retrieve")
    @ResponseBody
    public JsonResponse retrieveCiData(@RequestParam(value = "ci-type-id") Integer ciTypeId, @RequestParam(value = "guid") String guid) {
        return okayWithData(cmdbServiceV2Stub.getCiDataByGuid(ciTypeId, guid));
    }
    
    @PostMapping("/ci/confirm")
    @ResponseBody
    public JsonResponse implementCiType(@RequestBody List<OperateCiDto> operateCiDtos) {
        return okayWithData(cmdbServiceV2Stub.confirmCis(operateCiDtos));
    }
}
