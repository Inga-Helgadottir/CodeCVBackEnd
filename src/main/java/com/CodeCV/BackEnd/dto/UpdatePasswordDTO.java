package com.CodeCV.BackEnd.dto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {
    private String email;
    private String oldPassword;
    private String newPassword;
}
