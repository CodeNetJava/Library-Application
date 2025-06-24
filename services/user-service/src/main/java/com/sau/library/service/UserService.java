package com.sau.library.service;

import com.sau.library.dto.UserProfileRequest;
import com.sau.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.sau.library.entity.User;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    public void completeProfile(UserProfileRequest request,
                                String userId,
                                String username,
                                String roles,
                                String firstName,
                                String lastName,
                                String email){
        User user = User.builder()
                .id(userId)
                .username(username)
                .role(roles)
                .userFirstname(firstName)
                .userlastName(lastName)
                .email(email)
                .address(request.address())
                .userRollNo(request.userRollNo())
                .build();

userRepository.save(user);
    }

    public HttpStatusCode getUserProfile(String userId) {
       Optional<User> user =  userRepository.findById(userId);
       if(user.isPresent()){
           return HttpStatus.ACCEPTED;
       }
       else
           return HttpStatus.NOT_FOUND;
    }

    public HttpStatusCode updateProfile(String userId, UserProfileRequest request) {
        User user = userRepository.findById(userId).get();
        if(user != null){
            user.setUserRollNo(request.userRollNo());
            user.setAddress(request.address());
            userRepository.save(user);
            return HttpStatus.ACCEPTED;
        }
        else
            return HttpStatus.NOT_FOUND;
    }
}
