package com.CodeCV.BackEnd.repository;

import com.CodeCV.BackEnd.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository <Employee, Long> {
}
