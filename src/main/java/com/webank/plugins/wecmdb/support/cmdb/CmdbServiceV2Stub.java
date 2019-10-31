package com.webank.plugins.wecmdb.support.cmdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webank.plugins.wecmdb.ApplicationProperties;
import com.webank.plugins.wecmdb.dto.AttributeDto;
import com.webank.plugins.wecmdb.dto.DataType;
import com.webank.plugins.wecmdb.dto.EntityDto;
import com.webank.plugins.wecmdb.dto.OperateCiDto;
import com.webank.plugins.wecmdb.exception.PluginException;
import com.webank.plugins.wecmdb.support.cmdb.dto.CiTypeAttrDto;
import com.webank.plugins.wecmdb.support.cmdb.dto.CiTypeDto;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbDataType;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbInputType;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponse.DefaultCmdbResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponses.CiDataQueryResultResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponses.CiTypeAttrQueryResultResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponses.CiTypeQueryResultResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.PaginationQuery;
import com.webank.plugins.wecmdb.support.cmdb.dto.PaginationQueryResult;

@Service
public class CmdbServiceV2Stub {

    private static final String CITYPE_QUERY = "/ciTypes/retrieve";
    private static final String CITYYP_ATTR_QUERY = "/ciTypeAttrs/retrieve";
    private static final String CIDATA_QUERY = "/ci/%d/retrieve";
    private static final String CIDATA_STATE_OPERATE = "/ci/state/operate?operation=confirm";
    private static final String API_VERSION = "/api/v2";
    private static final Map<String, String> dataTypeMapping = new HashMap<>();
    static {
        dataTypeMapping.put(CmdbDataType.Varchar.getCode(), DataType.String.getCode());
        dataTypeMapping.put(CmdbDataType.Int.getCode(), DataType.Integer.getCode());
        dataTypeMapping.put(CmdbDataType.Date.getCode(), DataType.Timestamp.getCode());
        dataTypeMapping.put(CmdbDataType.DateTime.getCode(), DataType.Timestamp.getCode());
    }

    @Autowired
    private CmdbRestTemplate template;

    @Autowired
    private ApplicationProperties applicationProperties;

    public List<Object> getCiDataByGuid(String ciTypeTableName, String guid) {
        PaginationQueryResult<Object> ciDataResult = query(formatString(CIDATA_QUERY, retrieveCiTypeIdByTableName(ciTypeTableName)), PaginationQuery.defaultQueryObject().addEqualsFilter("guid", guid), CiDataQueryResultResponse.class);
        return ciDataResult.getContents();
    }

    private Integer retrieveCiTypeIdByTableName(String ciTypeTableName) {
        PaginationQuery queryObject = PaginationQuery.defaultQueryObject().addEqualsFilter("tableName", ciTypeTableName);
        PaginationQueryResult<CiTypeDto> ciTypes = queryCiTypes(queryObject);
        if (ciTypes != null && ciTypes.getContents() != null && !ciTypes.getContents().isEmpty()) {
            return ciTypes.getContents().get(0).getCiTypeId();
        } else {
            throw new PluginException(String.format("Can not retrieve ciType with name [%s]", ciTypeTableName));
        }
    }

    public PaginationQueryResult<CiTypeDto> queryCiTypes(PaginationQuery queryObject) {
        return query(CITYPE_QUERY, queryObject, CiTypeQueryResultResponse.class);
    }

    public PaginationQueryResult<CiTypeAttrDto> queryCiTypeAttrs(PaginationQuery queryObject) {
        return query(CITYYP_ATTR_QUERY, queryObject, CiTypeAttrQueryResultResponse.class);
    }

    public Object confirmCi(String ciTypeTableName, String guid) {
        List<OperateCiDto> operateCiDtos = new ArrayList<>();
        operateCiDtos.add(new OperateCiDto(guid, retrieveCiTypeIdByTableName(ciTypeTableName)));
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

    public List<EntityDto> getDataModel() {
        PaginationQueryResult<CiTypeDto> result = queryCiTypes(PaginationQuery.defaultQueryObject());
        return convertDataModel(result);
    }

    private List<EntityDto> convertDataModel(PaginationQueryResult<CiTypeDto> ciTypeDtoResponse) {
        List<EntityDto> entityDtos = new ArrayList<EntityDto>();
        if (ciTypeDtoResponse != null && ciTypeDtoResponse.getContents() != null && !ciTypeDtoResponse.getContents().isEmpty()) {
            List<CiTypeDto> ciTypeDtos = ciTypeDtoResponse.getContents();
            ciTypeDtos.forEach(ciTypeDto -> {
                EntityDto entityDto = new EntityDto();
                entityDto.setName(ciTypeDto.getTableName());
                entityDto.setDisplayName(ciTypeDto.getName());
                entityDto.setDescription(ciTypeDto.getDescription());

                PaginationQueryResult<CiTypeAttrDto> ciTypeAttrResponse = queryCiTypeAttrs(PaginationQuery.defaultQueryObject().addEqualsFilter("ciTypeId", ciTypeDto.getCiTypeId()));
                if (ciTypeAttrResponse != null && ciTypeAttrResponse.getContents() != null && !ciTypeAttrResponse.getContents().isEmpty()) {
                    List<CiTypeAttrDto> ciTypeAttrDtos = ciTypeAttrResponse.getContents();
                    List<AttributeDto> attributeDtos = new ArrayList<>();
                    ciTypeAttrDtos.forEach(ciTypeAttrDto -> {
                        AttributeDto attributeDto = new AttributeDto();
                        attributeDto.setEntityName(ciTypeDto.getTableName());
                        attributeDto.setDescription(ciTypeAttrDto.getDescription());
                        attributeDto.setName(ciTypeAttrDto.getPropertyName());
                        switch (CmdbInputType.fromCode(ciTypeAttrDto.getInputType())) {
                        case Reference:
                        case MultRef: {
                            attributeDto.setDataType(DataType.Ref.getCode());
                            attributeDto.setRefPackageName(applicationProperties.getPackageName());
                            attributeDto.setRefEntityName(ciTypeDto.getTableName());
                            attributeDto.setRefAttributeName(ciTypeAttrDto.getPropertyName());
                            break;
                        }
                        default:
                            attributeDto.setDataType(mapToWecubeDataType(ciTypeAttrDto));
                            break;
                        }
                        attributeDtos.add(attributeDto);
                    });
                    entityDto.setAttributes(attributeDtos);
                }
                entityDtos.add(entityDto);
            });
        }
        return entityDtos;
    }

    private String mapToWecubeDataType(CiTypeAttrDto ciTypeAttrDto) {
        return dataTypeMapping.get(ciTypeAttrDto.getPropertyType());
    }
}
