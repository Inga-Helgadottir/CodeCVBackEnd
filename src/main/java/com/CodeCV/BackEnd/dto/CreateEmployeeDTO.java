package com.CodeCV.BackEnd.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDTO {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
}
