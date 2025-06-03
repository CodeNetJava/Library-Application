package com.sau.library.service;

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


@Service
public class LoginService {

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

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
}
