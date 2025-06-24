package com.sau.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
public class Address {
    private String area;
    private String city;
    private String pincode;
    private String phoneNo;
}
