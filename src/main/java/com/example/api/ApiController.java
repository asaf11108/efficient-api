package com.example.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> options = new HashMap<>();
        // Enable CommonJS experimental support.
        options.put("js.commonjs-require", "true");
        options.put("js.commonjs-require-cwd", "C:/projects/Shield/api/target/jnpm/META-INF/resources/webjars/qs/6.5.3/dist");
        
        Context cx = Context.newBuilder("js")
                            .allowExperimentalOptions(true)
                            .allowIO(true)
                            .options(options)
                            .build();

        String src = "import {parse} from 'C:/projects/Shield/api/target/jnpm/META-INF/resources/webjars/qs/6.5.3/dist/qs.js';" +
                "parse('"+ request.getQueryString() +"');";
        System.out.println(cx.eval(Source.newBuilder("js", src, "test.mjs").build()));
        return request.getQueryString();
    }
}
