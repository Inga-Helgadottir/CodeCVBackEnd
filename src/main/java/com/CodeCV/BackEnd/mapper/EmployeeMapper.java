package com.CodeCV.BackEnd.mapper;

import com.CodeCV.BackEnd.dto.EmployeeDTO;
import com.CodeCV.BackEnd.entity.Employee;
import com.CodeCV.BackEnd.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeMapper {
    @Lazy
    @Autowired
    private final ShiftMapper shiftMapper;
    @Lazy
    @Autowired
    private final RoleMapper roleMapper;

    @Autowired
    public EmployeeMapper(@Lazy ShiftMapper shiftMapper, @Lazy RoleMapper roleMapper) {
        this.shiftMapper = shiftMapper;
        this.roleMapper = roleMapper;
    }

    public EmployeeDTO mapToEmployeeDto(Employee employee){
        if(employee != null){
            return new EmployeeDTO(
                    employee.getId(),
                    employee.getFirst_name(),
                    employee.getLast_name(),
                    employee.getEmail(),
                    roleMapper.mapToRoleDto(employee.getRole()),
                    shiftMapper.mapToShiftDtos(employee.getShifts())
            );
        }else{
            throw new ResourceNotFoundException("The employee provided is null");
        }
    }

    public List<EmployeeDTO> mapToEmployeeDtos(List<Employee> employees){
        List<EmployeeDTO> employeeDtos = new ArrayList<>();

        if(employees != null){
            if(!employees.isEmpty()){
                for(Employee employee : employees){
                    employeeDtos.add(mapToEmployeeDto(employee));
                }
            }
        }

        return employeeDtos;
    }
}
