package com.webank.plugins.wecmdb.support.cmdb.dto;

public class CmdbResponses {
    public static class CiDataQueryResultResponse extends CmdbResponse<PaginationQueryResult<Object>> {}
    public static class CiTypeQueryResultResponse extends CmdbResponse<PaginationQueryResult<CiTypeDto>> {}
    public static class CiTypeAttrQueryResultResponse extends CmdbResponse<PaginationQueryResult<CiTypeAttrDto>> {}
}
