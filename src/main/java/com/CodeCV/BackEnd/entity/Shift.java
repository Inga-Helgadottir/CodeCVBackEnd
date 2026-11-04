package com.CodeCV.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "department")
    private String department;
    @Column(name = "start_time")
    private LocalDateTime start_time;
    @Column(name = "end_time")
    private LocalDateTime end_time;
    @Column(name = "amount_of_employees_needed")
    private Long amount_of_employees_needed;

   @ManyToMany(fetch = FetchType.EAGER, mappedBy = "shifts", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Employee> employees = new ArrayList<>();

    public Shift(String department, LocalDateTime start_time, LocalDateTime end_time, Long amount_of_employees_needed) {
        this.department = department;
        this.start_time = start_time;
        this.end_time = end_time;
        this.amount_of_employees_needed = amount_of_employees_needed;
    }

    public void addEmployeeToShift(Employee employee){
        this.employees.add(employee);
        areThereEnoughEmployees();
    }

    public boolean areThereEnoughEmployees(){
        return this.amount_of_employees_needed == employees.size();
    }

    public Long howManyEmployeesAreMissing(){
        return this.amount_of_employees_needed - employees.size();
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id=" + id +
                ", department='" + department + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", amount_of_employees_needed=" + amount_of_employees_needed +
                '}';
    }
}
