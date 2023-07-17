package com.example.api.dto;

import java.util.Map;
import java.util.LinkedHashMap;

import com.example.api.FilterOperation;
import com.example.api.qs.Parser;
import com.example.api.qs.Parser.Options;

import lombok.Data;

@Data
public class QueryDto {
    Map<String, Map<FilterOperation, String>> filters;

    public QueryDto(String queryString) throws Exception {
        Map<String, Object> obj = Parser.parse(queryString, new Options());
        objToQueryDto(obj);
    }

    public QueryDto(String queryString, QueryDto defaultQueryDto) throws Exception {
        this(queryString);
        if (this.filters != null)
            this.filters.forEach((k, v) -> defaultQueryDto.filters.merge(k, v, (k1, v1) -> v1));
        else
            this.filters = defaultQueryDto.filters;
    }

    public QueryDto(Map<String, Map<FilterOperation, String>> filters) {
        this.filters = filters;
    }

    public void objToQueryDto(Map<String, Object> obj) {
        Object filtersObj = obj.get("filters");
        if (filtersObj != null) {
            this.filters = convertFilters(filtersObj);
        }
    }
    
    private Map<String, Map<FilterOperation, String>> convertFilters(Object obj) {
        Map<String, Object> fieldsObj = (LinkedHashMap<String, Object>) obj;
        Map<String, Map<FilterOperation, String>> fields = new LinkedHashMap<String, Map<FilterOperation, String>>();
        for (Map.Entry<String, Object> field : fieldsObj.entrySet()) {
            Map<String, String> opsObj = (LinkedHashMap<String, String>) field.getValue();
            Map<FilterOperation, String> ops = new LinkedHashMap<FilterOperation, String>();
            for(Map.Entry<String, String> op : opsObj.entrySet()) {
                ops.put(FilterOperation.getSimpleOperation(op.getKey()), op.getValue());
            }
            fields.put(field.getKey(), ops);
        };
        return fields;
    }
}
