package com.sau.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    //The annotation @JsonProperty("...") is used to map
    // a JSON field name to a Java field name when they are different.
    // the names of the field in the response you can see in documentation for keyclock response
    // or check on web
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
}
