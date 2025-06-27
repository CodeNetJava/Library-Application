package com.sau.library.controller;

import com.sau.library.dto.UserProfileRequest;
import com.sau.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //when th elogin service will be called
    //front end will call this service check if the user is present
    //from this endpoint if receive 404 then call the complete profile endpoint
    // or lese call the application dashboard
    @GetMapping("/getuser")
    public ResponseEntity<HttpStatusCode> getUser(@RequestHeader("X-User-Id") String userId
                                                  ){
        return new ResponseEntity<>(userService.getUserProfile(userId));
    }

    @PostMapping("/completeprofile")
    public void completeProfile(@RequestBody UserProfileRequest request,
                                @RequestHeader("X-User-Id")String userId,
                                @RequestHeader("X-Username")String username,
                                @RequestHeader("X-User-Roles")String roles,
                                @RequestHeader("X-First-Name")String firstName,
                                @RequestHeader("X-Last-Name")String lastName,
                                @RequestHeader("X-Email")String email
                                ){

        userService.completeProfile(
                 request,
                 userId,
                 username,
                 roles,
                firstName,
                lastName,
                 email
        );
    }

    @PutMapping("/updateprofile")
    public ResponseEntity<HttpStatusCode> updateProfile(@RequestHeader("X-User-Id")String userId, @RequestBody UserProfileRequest request){
        return new ResponseEntity<>(userService.updateProfile(userId,request));
    }

}
