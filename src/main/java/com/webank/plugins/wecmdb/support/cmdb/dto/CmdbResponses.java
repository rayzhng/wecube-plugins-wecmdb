package com.webank.plugins.wecmdb.support.cmdb.dto;

import java.util.Map;

public class CmdbResponses {
    public static class CiDataQueryResultResponse extends CmdbResponse<PaginationQueryResult<Map>> {
    }

    public static class CiTypeQueryResultResponse extends CmdbResponse<PaginationQueryResult<CiTypeDto>> {
    }

    public static class CiTypeAttrQueryResultResponse extends CmdbResponse<PaginationQueryResult<CiTypeAttrDto>> {
    }
}
