package com.CodeCV.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name")
    private String role_name;
    @Column(name = "role_description")
    private String role_description;

    @OneToMany(mappedBy="role", cascade = CascadeType.PERSIST)
    private List<Employee> employees = new ArrayList<>();

    public Role(String role_name, String role_description) {
        this.role_name = role_name;
        this.role_description = role_description;
    }

    public void addEmployeeToRole(Employee employee) {
        this.employees.add(employee);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role_name='" + role_name + '\'' +
                ", role_description='" + role_description + '\'' +
                '}';
    }
}

