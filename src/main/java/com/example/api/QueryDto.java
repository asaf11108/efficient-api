package com.example.api;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QueryDto {
    Map<String, Map<FilterOperation, String>> filters;
}
