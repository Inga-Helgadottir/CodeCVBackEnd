package com.CodeCV.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String role_name;
    private String role_description;

    @Override
    public String toString() {
        return "RoleDTO{" +
                "\n     id=" + id +
                "\n     role_name=" + role_name +
                "\n     role_description=" + role_description +
                "\n}";
    }
}

