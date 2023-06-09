package com.example.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import org.graalvm.polyglot.*;

@RestController
@RequestMapping(path = "api")
public class ApiController {

    @GetMapping
    // Other option to get raw query params
    // @Context UriInfo uriInfo
    // uriInfo.getRequestUri().getQuery()
    public String getApi(HttpServletRequest request) throws ScriptException, IOException, NoSuchMethodException {
        // ScriptEngineManager manager = new ScriptEngineManager();
        // System.out.println(manager.getEngineFactories());
        // ScriptEngine engine = manager.getEngineByName("graal.js");
        //     // read script file
        // BufferedReader file = Files.newBufferedReader(Paths.get("C:/projects/Shield/api/target/jnpm/META-INF/resources/webjars/qs/6.5.3/dist/qs.js"), StandardCharsets.UTF_8);
        // // QS evelResult = ;
        // System.out.println(file.lines().toString());
        // Invocable inv = (Invocable) engine.eval(file);
        // // call function from script file
        // Object parseResult = inv.invokeFunction("parse", request.getQueryString());
        // // System.out.println();
        // return request.getQueryString();

        BufferedReader file = Files.newBufferedReader(Paths.get("C:/projects/Shield/api/target/jnpm/META-INF/resources/webjars/qs/6.5.3/dist/qs.js"), StandardCharsets.UTF_8);
        // String input
        Map<String, String> options = new HashMap<>();
// Enable CommonJS experimental support.
options.put("js.commonjs-require", "true");
options.put("js.commonjs-require-cwd", "C:/projects/Shield/api/target/jnpm/META-INF/resources/webjars/qs/6.5.3/dist");
        
    Context cx = Context.newBuilder("js")
                            .allowExperimentalOptions(true)
                            .allowIO(true)
                            .options(options)
                            .build();

    String src = "import {stringify} from 'C:/projects/Shield/api/target/jnpm/META-INF/resources/webjars/qs/6.5.3/dist/qs.js';" +
             "console.log(stringify({a: 42}));";

	cx.eval(Source.newBuilder("js", src, "test.mjs").build());
        return request.getQueryString();
    }
}

interface QS {
    Object parse(String params);
}
