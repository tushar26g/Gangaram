package com.example.gangaram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class GangaramApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(GangaramApplication.class, args);

    }
}
