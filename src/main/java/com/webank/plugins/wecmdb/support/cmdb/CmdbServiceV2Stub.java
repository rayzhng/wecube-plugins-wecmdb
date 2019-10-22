package com.webank.plugins.wecmdb.support.cmdb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webank.plugins.wecmdb.ApplicationProperties;
import com.webank.plugins.wecmdb.dto.OperateCiDto;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponse.DefaultCmdbResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.v2.CmdbResponses.CiDataQueryResultResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.v2.PaginationQuery;
import com.webank.plugins.wecmdb.support.cmdb.dto.v2.PaginationQueryResult;

@Service
public class CmdbServiceV2Stub {

    private static final String CIDATA_QUERY = "/ci/%d/retrieve";
    private static final String CIDATA_STATE_OPERATE = "/ci/state/operate?operation=confirm";
    private static final String API_VERSION = "/api/v2";

    @Autowired
    private CmdbRestTemplate template;

    @Autowired
    private ApplicationProperties applicationProperties;

    public List<Object> getCiDataByGuid(Integer ciTypeId, String guid) {
        PaginationQueryResult<Object> ciDataResult = query(formatString(CIDATA_QUERY, ciTypeId), PaginationQuery.defaultQueryObject().addEqualsFilter("guid", guid), CiDataQueryResultResponse.class);
        return ciDataResult.getContents();
    }

    public Object confirmCis(List<OperateCiDto> operateCiDtos) {
        return template.postForResponse(asCmdbUrl(CIDATA_STATE_OPERATE), operateCiDtos, DefaultCmdbResponse.class);
    }

    private String formatString(String path, Object... pathVariables) {
        if (pathVariables != null && pathVariables.length > 0) {
            path = String.format(path, pathVariables);
        }
        return path;
    }

    private <D, R extends CmdbResponse> D query(String url, PaginationQuery queryObject, Class<R> responseType) {
        return template.postForResponse(asCmdbUrl(url), queryObject, responseType);
    }

    private String asCmdbUrl(String path, Object... pathVariables) {
        if (pathVariables != null && pathVariables.length > 0) {
            path = String.format(path, pathVariables);
        }
        return applicationProperties.getWecmdbServerUrl() + API_VERSION + path;
    }
}
