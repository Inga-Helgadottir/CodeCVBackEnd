package com.CodeCV.BackEnd.service;

import com.CodeCV.BackEnd.dto.CreateEmployeeDTO;
import com.CodeCV.BackEnd.dto.EmployeeDTO;
import com.CodeCV.BackEnd.dto.ShiftDTO;
import com.CodeCV.BackEnd.dto.UpdatePasswordDTO;
import com.CodeCV.BackEnd.entity.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface EmployeeService {

    EmployeeDTO createEmployee(CreateEmployeeDTO employeeDto);

    Employee getEmployeeByEmail(String employeeEmail);

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById(Long employeeId);

    EmployeeDTO getYourOwnInformation(String token);

    List<EmployeeDTO> getEmployeesByShiftId(Long shiftId, String token);

    List<ShiftDTO> getEmployeeShiftsById(Long employeeId, String token);

    EmployeeDTO addEmployeeToShift(Long employeeId, Long shiftId, String token);

    EmployeeDTO updateEmployee(EmployeeDTO updatedEmployee);

    String updateEmployeePassword(UpdatePasswordDTO updatedPasswordDTO, String token);

    String deleteEmployee(Long employeeId);
}
