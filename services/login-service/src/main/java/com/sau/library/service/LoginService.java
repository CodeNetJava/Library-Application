package com.sau.library.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
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
        body.add("grant_type", "password");
        body.add("username", "savita");
        body.add("password", "test");
        body.add("client_secret", clientSecret);
        body.add("client_id", clientId);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(body, headers);

        ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(
                tokenUri,
                httpEntity,
                TokenResponse.class
        );

        return responseEntity.getBody();
    }

    public void registerUser(RegisterRequest registerRequest) {
        // 1. Get admin token
        String token = this.getAdminToken();

        // 2. Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON); //

        // 3. Build user payload
        Map<String, Object> user = new HashMap<>();
        user.put("username", registerRequest.getUsername());
        user.put("email", registerRequest.getEmail());
        user.put("enabled", true);
        user.put("firstName", registerRequest.getFirstName());
        user.put("lastName", registerRequest.getLastName());

        // Password credentials
        List<Map<String, Object>> credentials = new ArrayList<>();
        Map<String, Object> passwordMap = new HashMap<>();
        passwordMap.put("type", "password");
        passwordMap.put("value", registerRequest.getPassword());
        passwordMap.put("temporary", false);
        credentials.add(passwordMap);

        user.put("credentials", credentials);

        // 4. Send request
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(user, headers);

        restTemplate.postForEntity(registerUserUri, httpEntity, String.class);
    }



    private String getAdminToken() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("client_id",clientId);
        body.add("username","admin");
        body.add("password","admin");
        body.add("client_secret",clientSecret);

        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(adminTokenUri,
        httpEntity,
                String.class
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseEntity.getBody());
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract token", e);
        }

    }
}
