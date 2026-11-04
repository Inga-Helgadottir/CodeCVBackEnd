package com.CodeCV.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private RoleDTO role;
    private List<ShiftDTO> shifts = new ArrayList<>();

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "\n     id=" + id +
                "\n     first_name=" + first_name +
                "\n     last_name=" + last_name +
                "\n     email=" + email +
                "\n     role=" + role +
                "\n     shifts=" + shifts +
                "\n}";
    }

}
