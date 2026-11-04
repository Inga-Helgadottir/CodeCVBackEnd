package com.CodeCV.BackEnd.entity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLogin {
    private String email;
    private String password;
}
