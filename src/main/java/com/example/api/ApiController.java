package com.example.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.qs.Parser;
import com.example.api.qs.Parser.Options;
import com.example.api.repository.PersonRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiController {
    private final PersonRepository personRepository;

    @GetMapping
    // Other option to get raw query params
    // @Context UriInfo uriInfo
    // uriInfo.getRequestUri().getQuery()
    public List<Person> getApi(HttpServletRequest request) throws Exception {
        Map<String, Object> obj = Parser.parse(request.getQueryString(), new Options());
        QueryDto queryDto = QueryMapper.objToQueryDto(obj);
        Specification spec = CustomSpecificationBuilder.build(queryDto.getFilters());
        return personRepository.findAll();
    }
}
