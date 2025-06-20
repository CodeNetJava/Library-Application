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
    private int id;
    private String userFirstname;
    private String userlastName;
    private String email;
    private String role;
}
