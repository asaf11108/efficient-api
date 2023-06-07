package com.example.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "api")
public class ApiController {

    @GetMapping
    // Other option to get raw query params
    // @Context UriInfo uriInfo
    // uriInfo.getRequestUri().getQuery()
    public String getApi(HttpServletRequest request) {
        return request.getQueryString();
    }
}
