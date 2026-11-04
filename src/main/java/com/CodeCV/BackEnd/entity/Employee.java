package com.CodeCV.BackEnd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", referencedColumnName = "id", nullable=false)
    private Role role;

    @ManyToMany
    @JoinTable(name = "employees_on_shifts",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "shift_id")
    )
    private List<Shift> shifts = new ArrayList<>();

    public Employee(String first_name, String last_name, String email, String password, Role role) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void addShiftToEmployee(Shift shift) {
        this.shifts.add(shift);
        shift.addEmployeeToShift(this);
    }

    public void setRole(Role role) {
        this.role = role;
        role.addEmployeeToRole(this);
    }
}


