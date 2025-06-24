package com.sau.library.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@Getter
@Setter

public class User {
    private String id;
    private String username;
    private String userRollNo;
    private String userFirstname;
    private String userlastName;
    private String email;
    private String role;
    private Address address;
}
