package com.healthcareapp.healthcareappbackend.dto;

import com.healthcareapp.healthcareappbackend.entity.AccountEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private Date time;
    private String description;
    private Long accountId;
}
