package com.sau.library.dto;

import com.sau.library.entity.Address;

public record UserProfileRequest(
         String userRollNo,
         Address address

) {
}
