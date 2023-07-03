package com.example.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.qs.Parser;
import com.example.api.qs.Parser.Options;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "api")
public class ApiController {

    @GetMapping
    // Other option to get raw query params
    // @Context UriInfo uriInfo
    // uriInfo.getRequestUri().getQuery()
    public String getApi(HttpServletRequest request) throws Exception {
        Map<String, Object> obj = Parser.parse(request.getQueryString(), new Options());
        QueryDto queryDto = QueryMapper.objToQueryDto(obj);
        return "";
    }
}
