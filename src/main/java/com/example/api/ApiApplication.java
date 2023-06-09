package com.example.api;

import org.orienteer.jnpm.JNPMService;
import org.orienteer.jnpm.JNPMSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.File;
import java.io.IOException;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ApiApplication {

	public static void main(String[] args) {
		// c = Context.create("js");
        // try {
        //     // load output from WebPack for Validator Module - a single bundled JS file
        //     File validatorBundleJS = new File(
        //             getClass().getClassLoader().getResource("validator_bundled.js").getFile());
        //     c.eval(Source.newBuilder("js", validatorBundleJS).build());
        //     System.out.println("All functions available from Java (as loaded into Bindings) "
        //             + c.getBindings("js").getMemberKeys());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
		// // System.out.println(qs.parse("a=c"));
		// System.out.println(JNPMService.instance().search("qs").getObjects().get(0).getClass().getNestMembers());
		SpringApplication.run(ApiApplication.class, args);
	}

}
