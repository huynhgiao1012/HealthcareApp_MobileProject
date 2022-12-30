package com.healthcareapp.healthcareappbackend.dto;

import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private String idCard;
    private String name;
    private String email;
    private String role;
    private boolean isActive;
    private String password;
    private String phone;
    private Date dob;
}
