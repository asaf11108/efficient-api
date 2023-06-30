package com.example.api;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.qs.Parser;
import com.example.api.qs.Parser.Options;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oracle.truffle.js.builtins.JSONBuiltins.JSON;
import com.oracle.truffle.js.runtime.objects.Null;
import com.oracle.truffle.regex.tregex.util.json.Json;

import jakarta.servlet.http.HttpServletRequest;
import org.graalvm.polyglot.*;

@RestController
@RequestMapping(path = "api")
public class ApiController {

    @GetMapping
    // Other option to get raw query params
    // @Context UriInfo uriInfo
    // uriInfo.getRequestUri().getQuery()
    public String getApi(HttpServletRequest request) throws Exception {
        // Map<String, String> options = new HashMap<>();
        // // Enable CommonJS experimental support.
        // options.put("js.commonjs-require", "true");
        // options.put("js.commonjs-require-cwd", "C:/projects/Shield/api/target/jnpm/META-INF/resources/webjars/qs/6.5.3/dist");
        
        // Context cx = Context.newBuilder("js")
        //                     .allowExperimentalOptions(true)
        //                     .allowIO(true)
        //                     .options(options)
        //                     .allowHostAccess(HostAccess.ALL).
        //                     //allows access to all Java classes
        //                     .allowHostClassLookup(className -> true)
        //                     .build();

        // String src = "import {parse} from 'C:/projects/Shield/api/target/jnpm/META-INF/resources/webjars/qs/6.5.3/dist/qs.js';" +
        //         "parse('"+ request.getQueryString() +"');";
        // Object value = cx.eval(Source.newBuilder("js", src, "parse.mjs").build()).as(Object.class);
        // QueryDto query = QueryDto.class.cast(value);
        // Map<String, Map<FilterOperation, String>> filters = objectMapper.convertValue(query.filters, new TypeReference<Map<String, Map<FilterOperation, String>>>() {});
        
        System.out.println(Parser.parse(request.getQueryString(), new Options()));
        Map<String, Object> obj = Parser.parse(request.getQueryString(), new Options());
        return "";
    }
}
