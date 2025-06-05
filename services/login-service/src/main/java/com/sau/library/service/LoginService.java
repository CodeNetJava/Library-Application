package com.sau.library.service;

import com.sau.library.dto.RegisterRequest;
import com.sau.library.dto.TokenResponse;
import com.sau.library.dto.UserCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class LoginService {

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token-uri}")
    private String tokenUri;
    @Value("${keycloak.admin-token-uri}")
    private String adminTokenUri;

    @Value("${keycloak.register-user-uri}")
    private String registerUserUri;

    private final RestTemplate restTemplate;

    public LoginService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public TokenResponse autheticateUser(UserCredentials userCredentials) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","savita");
        body.add("password","test");
        body.add("client_secret",clientSecret);
        body.add("client_id",clientId);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(body,headers);

        ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(
                tokenUri,
                httpEntity,
                TokenResponse.class
        );

        return responseEntity.getBody();
    }

    public void registerUser(RegisterRequest registerRequest) {
        //Get Admin Access Token from Keycloak //we need this to register
        String token =  this.getAdminToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBearerAuth(token);

        Map<String,Object> body = new HashMap<>();
        body.put("username", registerRequest.getUsername());
        body.put("email", registerRequest.getEmail());
        body.put("enabled", true);
        body.put("credentials", List.of(Map.of(
                "type", "password",
                "value", registerRequest.getPassword(),
                "temporary", false
        )));

        HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<>(body,httpHeaders);

        restTemplate.postForEntity(registerUserUri,
                httpEntity,
                String.class
                );
    }

    private String getAdminToken() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String,String> body = new HashMap<>();
        body.put("grant_type","password");
        body.put("client_id",clientId);
        body.put("username","admin");
        body.put("password","admin");

        HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(adminTokenUri,
        httpEntity,
                String.class
        );

        return responseEntity.getBody();

    }

}
