package com.manu.s.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO
{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private String branch;
}
