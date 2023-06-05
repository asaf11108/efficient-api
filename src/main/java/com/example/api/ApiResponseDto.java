package com.example.api;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseDto {
    Map<String, ?> filters;
    Pageable pageable;
}
