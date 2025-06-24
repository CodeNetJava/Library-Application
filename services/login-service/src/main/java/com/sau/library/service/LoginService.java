package com.sau.library.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sau.library.dto.RegisterRequest;
import com.sau.library.dto.TokenResponse;
import com.sau.library.dto.UserCredentials;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

    @Value("${keycloak.base-url}")
    private String baseUrl;

    @Value("${keycloak.realm}")
    private String realm;
    private final RestTemplate restTemplate;

    public LoginService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public TokenResponse autheticateUser(UserCredentials userCredentials) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", userCredentials.getUsername());
        body.add("password", userCredentials.getPassword());
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

    public void registerUser(RegisterRequest registerRequest, String roleName) {
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
        try {
            restTemplate.postForEntity(registerUserUri, httpEntity, String.class);
        } catch (RuntimeException e) {
            log.error("Error ocurred : "+e);
        }
        this.setRole(registerRequest,roleName);

    }


    private void setRole(RegisterRequest registerRequest, String roleName) {
        // 1. Get admin token
        String token = this.getAdminToken();

        // 2. Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON); //

        HttpEntity httpEntity = new HttpEntity<>(headers);

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            // get the userID
            String getUserIdUri = registerUserUri + "?username=" + registerRequest.getUsername();

            ResponseEntity<String> responseEntity1 =
                    restTemplate.exchange(
                            getUserIdUri,
                            HttpMethod.GET,
                            httpEntity,
                            String.class);

            JsonNode jsonNode = objectMapper.readTree(responseEntity1.getBody());
            String userId = jsonNode.get(0).get("id").asText();

            // get the client (gateway-service) UUID
            String getClientUUIDurl = baseUrl + "/admin/realms/" + realm + "/clients" + "?clientId=" + clientId;

            ResponseEntity<String> responseEntity2 =
                    restTemplate.exchange(
                            getClientUUIDurl,
                            HttpMethod.GET,
                            httpEntity,
                            String.class
                    );

            String clientUUID = objectMapper.readTree(responseEntity2.getBody()).get(0).get("id").asText();

            // get the role
            //String roleName = "consumer";
            String roleUri = baseUrl + "/admin/realms/" + realm + "/clients/" + clientUUID + "/roles/" + roleName;
            ResponseEntity<String> responseEntity3 =
                    restTemplate.exchange(
                            roleUri,
                            HttpMethod.GET,
                            httpEntity,
                            String.class
                    );
            JsonNode roleNode = objectMapper.readTree(responseEntity3.getBody());

            // set the role
            String assignRoleUri = baseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/clients/" + clientUUID;
            HttpEntity<String> httpEntity1 = new HttpEntity<>("[" + roleNode.toString() + "]", headers);
            restTemplate.postForEntity(
                    assignRoleUri,
                    httpEntity1,
                    String.class
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to register the user", e);
        }

    }

    private String getAdminToken() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("username", "savita");
        body.add("password", "test");
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, httpHeaders);

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
