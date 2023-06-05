package com.example.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api")
public class ApiController {

    @GetMapping
    public ApiResponseDto getApi(@RequestParam Map<String, ?> params, Pageable pageable) {
        return ApiResponseDto.builder().filters(params).pageable(pageable).build();
    }
}
