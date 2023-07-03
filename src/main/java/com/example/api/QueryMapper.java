package com.example.api;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryMapper {
 
    public static QueryDto objToQueryDto(Map<String, Object> obj) {
        QueryDto queryDto = QueryDto.builder().build();
        Object filtersObj = obj.get("filters");
        if (filtersObj != null) {
            queryDto.setFilters(convertFilters(filtersObj));
        }
        return queryDto;
    }
    
    private static Map<String, Map<FilterOperation, String>> convertFilters(Object obj) {
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
