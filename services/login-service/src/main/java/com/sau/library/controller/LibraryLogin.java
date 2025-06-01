package com.sau.library.controller;

import com.sau.library.dto.TokenResponse;
import com.sau.library.dto.UserCredentials;
import com.sau.library.service.LoginService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@AllArgsConstructor
@RestController
@RequestMapping("/library/login")
public class LibraryLogin {

    private final LoginService loginService;
    @PostMapping("/tokens")
    public ResponseEntity<TokenResponse> loginLibrary(@RequestBody UserCredentials userCredentials){
        return ResponseEntity.ok(loginService.autheticateUser(userCredentials));
    }

}
