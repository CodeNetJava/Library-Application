package com.sau.library.controller;

import com.sau.library.dto.TokenResponse;
import com.sau.library.dto.UserCredentials;
import com.sau.library.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/library/login")
public class LoginController {

    private final LoginService loginService;
@Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/tokens")
    public ResponseEntity<TokenResponse> loginLibrary(@RequestBody UserCredentials userCredentials){
        return ResponseEntity.ok(loginService.autheticateUser(userCredentials));
    }

}
