package com.integration;

import com.Application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class SimpleIntegration {
    @Autowired
    Environment environment;

    @Test
    public void indexHtmlIsAccessible() throws IOException, InterruptedException {
        System.out.println(environment.getProperty("local.server.port"));
        String port = environment.getProperty("local.server.port");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/auth"))
                .build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("=========indexHtmlIsAccessible=========");

        System.out.println(port);
        System.out.println(res.body());

        Assertions.assertTrue(res.body().contains("401"));
    }
}
