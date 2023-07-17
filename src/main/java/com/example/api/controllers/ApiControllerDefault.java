package com.example.api.controllers;

import java.util.LinkedHashMap;

import com.example.api.dto.QueryDto;
import com.example.api.FilterOperation;


public class ApiControllerDefault {
    public static final QueryDto queryDto = new QueryDto(new LinkedHashMap() {{
        put("first_name", new LinkedHashMap(){{
            put(FilterOperation.STARTS_WITH, "Tel A");
        }});
    }});
}
