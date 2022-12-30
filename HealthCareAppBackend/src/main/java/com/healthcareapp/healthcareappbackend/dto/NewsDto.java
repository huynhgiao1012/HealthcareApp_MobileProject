package com.healthcareapp.healthcareappbackend.dto;

import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private Long id;
    private String title;
    private String image;
    private String description;
    private Date publicDate;
    private String authorName;
}
