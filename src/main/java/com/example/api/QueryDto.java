package com.example.api;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QueryDto {
    Map<String, Map<FilterOperation, String>> filters;
    List<String> sort;
    Pagination pagination;
}
