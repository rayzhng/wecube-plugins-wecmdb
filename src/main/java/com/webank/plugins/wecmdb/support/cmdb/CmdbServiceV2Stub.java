package com.webank.plugins.wecmdb.support.cmdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbOperateCiDto;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponse.DefaultCmdbResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponses.CiDataQueryResultResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponses.CiTypeAttrQueryResultResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.CmdbResponses.CiTypeQueryResultResponse;
import com.webank.plugins.wecmdb.support.cmdb.dto.PaginationQuery;
import com.webank.plugins.wecmdb.support.cmdb.dto.PaginationQueryResult;

@Service
public class CmdbServiceV2Stub {

    private static final String SORTING_DESC = "desc";
    private static final String SORTING_ASC = "asc";
    private static final String ID = "id";
    private static final String GUID = "guid";
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

    public Object confirmBatchCiData(List<OperateCiDto> operateCiDtos) {
        validate(operateCiDtos);
        List<CmdbOperateCiDto> cmdbOperateCiDtos = new ArrayList<>();
        operateCiDtos.forEach(operateCiDto -> {
            cmdbOperateCiDtos.add(new CmdbOperateCiDto(operateCiDto.getGuid(), retrieveCiTypeIdByTableName(operateCiDto.getEntityName())));
        });
        return template.postForResponse(asCmdbUrl(CIDATA_STATE_OPERATE), cmdbOperateCiDtos, DefaultCmdbResponse.class);
    }

    private void validate(List<OperateCiDto> operateCiDtos) {
        operateCiDtos.forEach(operateCiDto -> {
            if (StringUtils.isBlank(operateCiDto.getGuid())) {
                throw new PluginException("Field 'guid' is required for ci data confirmation.");
            }

            if (StringUtils.isBlank(operateCiDto.getEntityName())) {
                throw new PluginException("Field 'entityName' is required for ci data confirmation.");
            }
        });

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
                        attributeDto.setName(GUID.equals(ciTypeAttrDto.getPropertyName()) ? ID : ciTypeAttrDto.getPropertyName());
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

    public Object getCiDataWithConditions(String entityName, String filter, String sorting, String selectAttrs) {
        PaginationQuery queryObject = PaginationQuery.defaultQueryObject();

        applyFiltering(filter, queryObject);
        applySorting(sorting, queryObject);
        applySelectAttrs(selectAttrs, queryObject);

        return convertCiData(queryObject, retrieveCiTypeIdByTableName(entityName));
    }

    private void applySelectAttrs(String selectAttrs, PaginationQuery queryObject) {
        if (!StringUtils.isBlank(selectAttrs)) {
            String[] attrs = selectAttrs.split(",");
            List<String> resultColumns = new ArrayList<>();
            for (String attr : attrs) {
                resultColumns.add(ID.equals(attr) ? GUID : attr.trim());
            }
            queryObject.setResultColumns(resultColumns);
        }
    }

    private void applySorting(String sorting, PaginationQuery queryObject) {
        if (!StringUtils.isBlank(sorting)) {
            if (sorting.split(",").length != 2) {
                throw new PluginException("The given parameter 'sorting' must be format 'key," + SORTING_ASC + "/" + SORTING_DESC + "'");
            }
            String sortingAttr = sorting.split(",")[0].trim();
            String sortingValue = sorting.split(",")[1].trim();

            if (SORTING_ASC.equals(sortingValue) || SORTING_DESC.equals(sortingValue)) {
                queryObject.setSorting(new PaginationQuery.Sorting(sortingValue.equals(SORTING_ASC) ? true : false, ID.equals(sortingAttr) ? GUID : sortingAttr));
            } else {
                throw new PluginException("The given value of 'sorting' must be " + SORTING_ASC + " or " + SORTING_DESC + "'");
            }
        }
    }

    private void applyFiltering(String filter, PaginationQuery queryObject) {
        if (!StringUtils.isBlank(filter)) {
            if (filter.split(",").length != 2) {
                throw new PluginException("The given parameter 'filter' must be format 'key,value'");
            }

            String filterAttr = filter.split(",")[0].trim();
            String filterValue = filter.split(",")[1].trim();
            queryObject.addEqualsFilter(ID.equals(filterAttr) ? GUID : filterAttr, filterValue);
        }
    }

    private List<Map<String, Object>> convertCiData(PaginationQuery queryObject, Integer ciTypeId) {
        List<Map<String, Object>> convertedCiData = new ArrayList<>();

        PaginationQueryResult<Map> ciDataResult = query(formatString(CIDATA_QUERY, ciTypeId), queryObject, CiDataQueryResultResponse.class);
        List<CiTypeAttrDto> ciTypeAttrDtos = getCiTypeAttrs(ciTypeId);
        if (ciDataResult != null && !ciDataResult.getContents().isEmpty()) {
            List<Map> cis = ciDataResult.getContents();
            cis.forEach(ci -> {
                Map<String, Object> convertedMap = new HashMap<>();
                Map<String, Object> originCiData = (Map) ci.get("data");

                originCiData.forEach((name, value) -> {
                    if (queryObject.getResultColumns() == null || queryObject.getResultColumns().contains(name)) {
                        populateSelectedAttrs(ciTypeAttrDtos, convertedMap, name, value);
                    }
                });
                convertedCiData.add(convertedMap);
            });
        }
        return convertedCiData;
    }

    private void populateSelectedAttrs(List<CiTypeAttrDto> ciTypeAttrDtos, Map<String, Object> convertedMap, String dataAttrName, Object value) {
        ciTypeAttrDtos.forEach(attr -> {
            String convertedAttrName = GUID.equals(attr.getPropertyName()) ? ID : attr.getPropertyName();
            String convertedDataAttrName = GUID.equals(dataAttrName) ? ID : dataAttrName;
            if (convertedAttrName.equals(convertedDataAttrName)) {
                if (value == null || (value instanceof String && "".equals(value))) {
                    convertedMap.put(convertedDataAttrName, value);
                } else if (CmdbInputType.fromCode(attr.getInputType()) == CmdbInputType.Droplist || CmdbInputType.fromCode(attr.getInputType()) == CmdbInputType.MultSelDroplist) {
                    Map map = (Map) value;
                    convertedMap.put(convertedDataAttrName, map.get("codeId"));
                } else if (convertedAttrName.equals(dataAttrName) && (CmdbInputType.fromCode(attr.getInputType()) == CmdbInputType.Reference)) {
                    Map singleRefObject = (Map) value;
                    convertedMap.put(convertedDataAttrName, value != null ? singleRefObject.get(GUID) : value);
                } else if (convertedAttrName.equals(convertedDataAttrName) && (CmdbInputType.fromCode(attr.getInputType()) == CmdbInputType.MultRef)) {
                    List multRefObjects = (List) value;
                    List<String> guids = new ArrayList<>();
                    multRefObjects.forEach(object -> {
                        guids.add(((Map<String, Object>) object).get(GUID).toString());
                    });
                    convertedMap.put(convertedDataAttrName, String.join(",", guids.toArray(new String[guids.size()])));
                } else {
                    convertedMap.put(convertedDataAttrName, value);
                }
            }
        });
    }

    private List<CiTypeAttrDto> getCiTypeAttrs(Integer ciTypeId) {
        PaginationQueryResult<CiTypeAttrDto> ciTypeAttrResult = queryCiTypeAttrs(PaginationQuery.defaultQueryObject().addEqualsFilter("ciTypeId", ciTypeId));
        if (ciTypeAttrResult != null && ciTypeAttrResult.getContents() != null && !ciTypeAttrResult.getContents().isEmpty()) {
            return ciTypeAttrResult.getContents();
        } else {
            throw new PluginException(String.format("Can not find attrs for ciType [%s]", ciTypeId));
        }
    }
}
