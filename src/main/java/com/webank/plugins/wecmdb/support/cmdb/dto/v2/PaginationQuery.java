package com.webank.plugins.wecmdb.support.cmdb.dto.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaginationQuery {
    private boolean paging;
    private Pageable pageable;
    private String filterRs;
    private List<Filter> filters = new ArrayList<>();
    private Sorting sorting;
    private List<String> groupBys = new ArrayList<>();
    private List<String> refResources = new ArrayList<>();
    private List<String> resultColumns;
    private Dialect dialect = new Dialect();

    public PaginationQuery() {
    }

    public PaginationQuery(boolean paging, Pageable pageable, String filterRs, List<Filter> filters, Sorting sorting, List<String> groupBys, List<String> refResources, List<String> resultColumns, Dialect dialect) {
        super();
        this.paging = paging;
        this.pageable = pageable;
        this.filterRs = filterRs;
        this.filters = filters;
        this.sorting = sorting;
        this.groupBys = groupBys;
        this.refResources = refResources;
        this.resultColumns = resultColumns;
        this.dialect = dialect;
    }

    public static class Dialect {
        private boolean showCiHistory = false;
        private Map<String, Object> data;

        public boolean getShowCiHistory() {
            return showCiHistory;
        }

        public void setShowCiHistory(boolean showCiHistory) {
            this.showCiHistory = showCiHistory;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }

    }

    public PaginationQuery addEqualsFilter(String name, Object value) {
        filters.add(new Filter(name, "eq", value));
        return this;
    }

    public PaginationQuery setFiltersRelationship(String relationship) {
        setFilterRs(relationship);
        return this;
    }

    public PaginationQuery addEqualsFilters(Map<String, Object> filterObject) {
        filterObject.entrySet().forEach(entry -> filters.add(new Filter(entry.getKey(), "eq", entry.getValue())));
        return this;
    }

    public PaginationQuery addInFilters(Map<String, Object> filterObject) {
        filterObject.entrySet().forEach(entry -> filters.add(new Filter(entry.getKey(), "in", entry.getValue())));
        return this;
    }

    public PaginationQuery addNotEqualsFilter(String name, Object value) {
        filters.add(new Filter(name, "ne", value));
        return this;
    }

    public PaginationQuery addInFilter(String name, List values) {
        filters.add(new Filter(name, "in", values));
        return this;
    }

    public PaginationQuery addContainsFilter(String name, String value) {
        filters.add(new Filter(name, "contains", value));
        return this;
    }

    public PaginationQuery addNotNullFilter(String name) {
        filters.add(new Filter(name, "notNull", null));
        return this;
    }

    public PaginationQuery addNullFilter(String name) {
        filters.add(new Filter(name, "null", null));
        return this;
    }

    public PaginationQuery ascendingSortBy(String field) {
        sorting = new Sorting(true, field);
        return this;
    }

    public PaginationQuery descendingSortBy(String field) {
        sorting = new Sorting(false, field);
        return this;
    }

    public PaginationQuery addReferenceResource(String refResource) {
        refResources.add(refResource);
        return this;
    }

    public PaginationQuery withResultColumns(List<String> resultColumns) {
        this.resultColumns = resultColumns;
        return this;
    }

    public static class Filter {
        public Filter(String name, String operator, Object value) {
            this.name = name;
            this.operator = operator;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        private String name;
        private String operator;
        private Object value;
    }

    public static class Sorting {
        public boolean isAsc() {
            return asc;
        }

        public void setAsc(boolean asc) {
            this.asc = asc;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Sorting(boolean asc, String field) {
            super();
            this.asc = asc;
            this.field = field;
        }

        private boolean asc;
        private String field;
    }

    public static class Pageable {
        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public Pageable(int startIndex, int pageSize) {
            super();
            this.startIndex = startIndex;
            this.pageSize = pageSize;
        }

        private int startIndex;
        private int pageSize;
    }

    public static PaginationQuery defaultQueryObject() {
        return new PaginationQuery();
    }

    public static PaginationQuery defaultQueryObject(String name, Object value) {
        return defaultQueryObject().addEqualsFilter(name, value);
    }

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public String getFilterRs() {
        return filterRs;
    }

    public void setFilterRs(String filterRs) {
        this.filterRs = filterRs;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public Sorting getSorting() {
        return sorting;
    }

    public void setSorting(Sorting sorting) {
        this.sorting = sorting;
    }

    public List<String> getGroupBys() {
        return groupBys;
    }

    public void setGroupBys(List<String> groupBys) {
        this.groupBys = groupBys;
    }

    public List<String> getRefResources() {
        return refResources;
    }

    public void setRefResources(List<String> refResources) {
        this.refResources = refResources;
    }

    public List<String> getResultColumns() {
        return resultColumns;
    }

    public void setResultColumns(List<String> resultColumns) {
        this.resultColumns = resultColumns;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public String toString() {
        return "PaginationQuery [paging=" + paging + ", pageable=" + pageable + ", filters=" + filters + ", sorting="
                + sorting + ", groupBys=" + groupBys + ", refResources=" + refResources + ", resultColumns="
                + resultColumns + "]";
    }
}
