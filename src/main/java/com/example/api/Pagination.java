package com.example.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pagination {
    int page;
    int size;
}
